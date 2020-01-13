/**************************************************************************\
 *                                                                          *
 *   init_rand_digits(n)                                                    *
 *                                                                          *
 *   This routine creates a random 64-bit pattern which can be used to      *
 *   initialize the constant s in the state vector.  The pattern is chosen  *
 *   so that the 8 upper hexadecimal digits are different and also that the *
 *   8 lower digits are different.  1 is ored into the result to make the   *
 *   constant odd.  Roughly half the bits will be set to one and roughly    *
 *   half the bits will be set to zero.  This assures sufficient change in  *
 *   the Weyl sequence on each iteration of the RNG.                        *
 *                                                                          *
 *   The input parameter n is a 64-bit number in the range of 0 to 2^64-1.  *
 *   A set of random digits will be created for this input value.  This     *
 *   routine has been verified to produce unique constants for input values *
 *   from 0 to 3 billion.  For input values greater than 3 billion, some    *
 *   previously generated constants may occur at random.                    *
 *                                                                          *
 *   The number of constants available has been computed exactly.  For the  *
 *   upper 8 digits, there will be 16*15*14*13*12*11*10*9 or 518918400      *
 *   possibilities.  The number of lower 8 digits is less because the last  *
 *   digit is odd.  This turns out to be 380540160.  Multiplying these      *
 *   provides 197469290962944000 or about 0.2 quintillion possible          *
 *   constants.  Since the stream length is 2^64 this will provide about    *
 *   2^121 random numbers in total.                                         *
 *                                                                          *
 *   One may use this routine as follows to initialize the state:           *
 *                                                                          *
 *   #include "init.h"                                                      *
 *                                                                          *
 *   x = w = s = init_rand_digits(n);                                       *
 *                                                                          *
 *   Alternatively, one may use init_rand_digits offline to create an       *
 *   include file of constants which could be compiled into the the program.*
 *   This would provide the fastest initialization of the state.  This may  *
 *   useful for certain time critical applications which require a fast     *
 *   initialization. The seed directory contains a program which creates    *
 *   a seed.h file which may be used as shown below.                        *
 *                                                                          *
 *   uint64_t seed[] = {                                                    *
 *   #include "seed.h"                                                      *
 *   };                                                                     *
 *                                                                          *
 *   x = w = s = seed[n];                                                   *
 *                                                                          *
 *   See streams_example for example usage.                                 *
 *                                                                          *
 \**************************************************************************/

package com.gerritjvv.random.msws;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.function.LongSupplier;

/**
 * Contains methods and data from the init.h c lib file.
 */
public class Init {


    /* 30 sconst values.  Each provide 100 million unique outputs */

    private static final long[] sconst = {
            0x8b5ad4ce914ecdf7L, 0xdbc8915f4b1cd961L, 0x3a16e0c51fa593d9L, 0x1794da529ec6d70bL,
            0x8fc49b2a752f643bL, 0xde07a518fba03571L, 0xb1d2e4762d58906bL, 0x478f6219da719b05L,
            0x41857dc34a2fdc05L, 0xb9425ed8e351a06fL, 0x9235eb64c35eab7dL, 0x91f0e7b8e0536af7L,
            0x4f0581abb194f75bL, 0xdab4e53c95408d1fL, 0xf23ba0c5410ceb3bL, 0x912a0b4ce102a36dL,
            0x92a73b40b46a2e71L, 0x46ca273b5fde168dL, 0xf9b8ad61743910b5L, 0x490ceb3d865e4bc9L,
            0xa12e0dcfbf6471cfL, 0xa54c91db6dc0fe37L, 0x08c3564a5c031727L, 0xe3296d17c14795bdL,
            0x5387014db793f24fL, 0x6d47af052931fe47L, 0xd138c9ef735c0e8fL, 0xa790fbc8ebf02d3bL,
            0x4a1b027867c953fbL, 0x49a180de9567182dL,
    };

    /**
     * Return a random 64 bit pattern seeded with a value from {@link SecureRandom#getInstanceStrong}
     *
     * @return
     */
    public static final long randDigits() {
        try {
            return new LocalMsWsi(SecureRandom.getInstanceStrong().nextLong()).getAsLong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * For secure applications use  {@link #randDigits()} which will use SecureRandom.getInstanceStrong.
     *
     * @param n will always return the same random number for the same value of N.
     * @return
     */
    public static final long randDigits(long n) {
        return new LocalMsWsi(n).getAsLong();
    }

    private static final class LocalMsWsi implements LongSupplier {
        /* local copy of msws rng */
        long xi, wi, si;

        final long n;

        private LocalMsWsi(long n) {
            this.n = Math.abs(n);
        }

        private int mswsi() {

            xi *= xi;
            xi += (wi += si);
            xi = (xi >> 32) | (xi << 32);

            return (int) xi;
        }


        @Override
        public long getAsLong() {
            int a, c, i, j, k, m;
            long r, t, u;

            // intialize state for the local msws rng
            r = n / 100000000;
            t = n % 100000000;
            si = sconst[(int) (r % 30)];
            r /= 30;
            xi = wi = t * si + r * si * 100000000;

            /* get 8 different random digits */

            for (m = 0, a = 0, c = 0; m < 32; ) {
                j = mswsi();                /* get 32-bit random word */
                for (i = 0; i < 32; i += 4) {
                    k = (j >> i) & 0xf;        /* get a nibble */
                    if ((c & (1 << k)) == 0) { /* verify not used previously */
                        c |= (1 << k);
                        a |= (k << m);          /* add nibble to output */
                        m += 4;
                        if (m >= 32) break;
                    }
                }
            }

            u = a;
            u <<= 32;               /* save in upper 32 bits */

            /* get 8 different random digits */

            for (m = 0, a = 0, c = 0; m < 32; ) {
                j = mswsi();                /* get 32-bit random word */
                for (i = 0; i < 32; i += 4) {
                    k = (j >> i) & 0xf;        /* get a nibble */
                    if ((c & (1 << k)) == 0) { /* verify not used previously */
                        c |= (1 << k);
                        a |= (k << m);          /* add nibble to output */
                        m += 4;
                        if (m >= 32) break;
                    }
                }
            }

            return u | a | 1;              /* set least significant bit to 1 */
        }
    }
}
