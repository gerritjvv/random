package com.gerritjvv.random.msws;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the Init random number initialiser
 */
public class InitTest {

    @Test
    public void sameRandomNumberForSameN(){

        long n = System.nanoTime();


        long s1 = Init.randDigits(n);
        long s2 = Init.randDigits(n);
        long s3 = Init.randDigits(n);

        assertEquals(s1, s2);
        assertEquals(s2, s3);
    }

    /**
     * This is by far not a proper test for randomness but more here to show
     * the developer that using {@link Init#randDigits()} returns a different rand value every time.
      */
    @Test
    public void differentRandomForDifferentN(){

        long lastN = 0;

        for(int i = 0; i < 10; i++){
            long tmp = lastN;
            lastN = Init.randDigits();

            assertTrue(lastN != tmp);
        }
    }

}
