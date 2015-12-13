package com.leanstacks.hello.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.leanstacks.hello.model.Greeting;

public class GreetingItemProcessor implements ItemProcessor<Greeting, Greeting> {

    private static final Logger logger = LoggerFactory.getLogger(GreetingItemProcessor.class);

    @Override
    public Greeting process(Greeting greeting) throws Exception {
        final String lang = greeting.getLanguage().toUpperCase();
        final String text = greeting.getText().toUpperCase();

        final Greeting processedGreeting = new Greeting(lang, text);

        logger.info("Processed ({}} into ({})", greeting, processedGreeting);

        return processedGreeting;
    }

}
