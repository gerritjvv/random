package com.gerritjvv.random.msws;

import org.junit.Test;

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
            lastN = MsWs.getInstance().getAsLong();
            assertTrue(tmp != lastN);
        }

    }
}
