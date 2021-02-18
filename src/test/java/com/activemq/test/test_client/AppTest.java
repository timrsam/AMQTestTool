package com.activemq.test.test_client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
		
    @Test
    public void shouldAnswerWithTrue() throws Exception
    {
    	assertEquals(Handler.mainTester(true), 0);
    }
}
