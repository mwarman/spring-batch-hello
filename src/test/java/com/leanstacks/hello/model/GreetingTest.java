package com.leanstacks.hello.model;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.leanstacks.hello.AbstractTest;

/**
 * Unit tests for the Greeting class.
 * 
 * @author Matt Warman
 */
@Transactional
public class GreetingTest extends AbstractTest {

    public static final String LANGUAGE_EN = "en";

    public static final String TEXT_HELLO = "hello";

    @Test
    public void testGreeting() {

        Greeting entity = new Greeting(LANGUAGE_EN, TEXT_HELLO);

        Assert.assertNotNull("failure - expected entity not null", entity);
        Assert.assertNotNull("failure - expected language attribute not null", entity.getLanguage());
        Assert.assertEquals("failure - expected language attribute match", LANGUAGE_EN, entity.getLanguage());
        Assert.assertNotNull("failure - expected text attribute not null", entity.getText());
        Assert.assertEquals("failure - expected text attribute match", TEXT_HELLO, entity.getText());

    }

}
