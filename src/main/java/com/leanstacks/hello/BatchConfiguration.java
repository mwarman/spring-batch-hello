package com.leanstacks.hello;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.leanstacks.hello.batch.GreetingItemProcessor;
import com.leanstacks.hello.model.Greeting;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public ItemReader<Greeting> reader() {
        FlatFileItemReader<Greeting> reader = new FlatFileItemReader<Greeting>();
        reader.setResource(new ClassPathResource("greeting-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Greeting>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "language", "text" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Greeting>() {
                    {
                        setTargetType(Greeting.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Greeting, Greeting> processor() {
        return new GreetingItemProcessor();
    }

    @Bean
    public ItemWriter<Greeting> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Greeting> writer = new JdbcBatchItemWriter<Greeting>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Greeting>());
        writer.setSql("INSERT INTO greetings (language, text) VALUES (:language, :text)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Step importGreetingStep(StepBuilderFactory stepBuilderFactory, ItemReader<Greeting> reader,
            ItemWriter<Greeting> writer, ItemProcessor<Greeting, Greeting> processor) {
        return stepBuilderFactory.get("importGreetingStep").<Greeting, Greeting> chunk(5).reader(reader)
                .processor(processor).writer(writer).build();
    }

    @Bean
    public Job importGreetingJob(JobBuilderFactory jobs, Step importGreetingStep, JobExecutionListener listener) {
        return jobs.get("importGreetingJob").incrementer(new RunIdIncrementer()).listener(listener)
                .flow(importGreetingStep).end().build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
