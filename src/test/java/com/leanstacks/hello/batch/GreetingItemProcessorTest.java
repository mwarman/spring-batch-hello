package com.leanstacks.hello.batch;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.leanstacks.hello.AbstractTest;
import com.leanstacks.hello.model.Greeting;
import com.leanstacks.hello.model.GreetingTest;

/**
 * Unit tests for the GreetingItemProcessor class.
 * 
 * @author Matt Warman
 */
@Transactional
public class GreetingItemProcessorTest extends AbstractTest {

    @Test
    public void testProcess() {

        Exception exception = null;
        Greeting postProcessedEntity = null;

        Greeting preProcessedEntity = new Greeting(GreetingTest.LANGUAGE_EN, GreetingTest.TEXT_HELLO);

        GreetingItemProcessor processor = new GreetingItemProcessor();

        try {
            postProcessedEntity = processor.process(preProcessedEntity);
        } catch (Exception e) {
            exception = e;
        }

        Assert.assertNull("failure - expected exception to be null", exception);
        Assert.assertNotNull("failure - expected post-processed entity not null", postProcessedEntity);
        Assert.assertEquals("failure - expected language attribute match",
                preProcessedEntity.getLanguage().toUpperCase(), postProcessedEntity.getLanguage());
        Assert.assertEquals("failure - expected text attribute match", preProcessedEntity.getText().toUpperCase(),
                postProcessedEntity.getText());

    }

}
