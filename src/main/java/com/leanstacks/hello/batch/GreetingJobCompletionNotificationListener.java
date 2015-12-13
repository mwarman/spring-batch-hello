package com.leanstacks.hello.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.leanstacks.hello.model.Greeting;

@Component
public class GreetingJobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(GreetingJobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GreetingJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("!!! JOB FINISHED! Time to verify the results");

            List<Greeting> results = jdbcTemplate.query("SELECT language, text FROM greetings",
                    new RowMapper<Greeting>() {
                        @Override
                        public Greeting mapRow(ResultSet rs, int row) throws SQLException {
                            return new Greeting(rs.getString(1), rs.getString(2));
                        }
                    });

            for (Greeting greeting : results) {
                logger.info("Found <" + greeting + "> in the database.");
            }

        }
    }

}
