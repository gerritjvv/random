package com.gerritjvv.random.msws;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MsWsTest {

    /**
     * not a randomness test, and is here to show usage
     */
    @Test
    public void simpleGetTest() {

        long lastN = 0;
        for(int i = 0; i < 1000; i++) {
            long tmp = lastN;
            Random rnd = MsWs.getInstance();
            lastN = rnd.nextLong();
            System.out.println(lastN);
            assertTrue(tmp != lastN);
        }

    }
}
