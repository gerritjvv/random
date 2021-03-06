/**************************************************************************\
 *                                                                          *
 *  Middle Square Weyl Sequence Random Number Generator                     *
 *                                                                          *
 *  msws() - returns a 32 bit unsigned int [0,0xffffffff]                   *
 *                                                                          *
 *  The state vector consists of three 64 bit words:  x, w, and s           *
 *                                                                          *
 *  x - random output [0,0xffffffff]                                        *
 *  w - Weyl sequence (period 2^64)                                         *
 *  s - an odd constant                                                     *
 *                                                                          *
 *  The Weyl sequence is added to the square of x.  The middle is extracted *
 *  by shifting right 32 bits:                                              *
 *                                                                          *
 *  x *= x; x += (w += s); return x = (x>>32) | (x<<32);                    *
 *                                                                          *
 *  The constant s should be set to a random 64-bit pattern.  The utility   *
 *  init_rand_digits in init.h may be used to initialize the constant.      *
 *  This utility generates constants with different hexadecimal digits.     *
 *  This assures sufficient change in the Weyl sequence on each iteration   *
 *  of the RNG.                                                             *
 *                                                                          *
 *  Note:  This version of the RNG includes an idea proposed by             *
 *  Richard P. Brent (creator of the xorgens RNG).  Brent suggested         *
 *  adding the Weyl sequence after squaring instead of before squaring.     *
 *  This provides a basis for uniformity in the output.                     *
 *                                                                          *
 *  Copyright (c) 2014-2019 Bernard Widynski                                *
 *                                                                          *
 *  This code can be used under the terms of the GNU General Public License *
 *  as published by the Free Software Foundation, either version 3 of the   *
 *  License, or any later version. See the GPL license at URL               *
 *  http://www.gnu.org/licenses                                             *
 *                                                                          *
 \**************************************************************************/

package com.gerritjvv.random.msws;

import java.util.Random;
import java.util.function.IntSupplier;

/**
 * Usage: <br>
 * <pre>
 *         MsWs.getInstance().getAsInt()
 *     </pre>
 * Not threadsafe. The getInstance() methods returns a thread local instance.
 */
public class MsWs extends Random implements IntSupplier {


    private static final ThreadLocal<MsWs> msWsThreadLocal =
            ThreadLocal.withInitial(() -> new MsWs(Init.randDigits()));

    private long x, w, s;

    private MsWs(long randDigits) {
        this.x = randDigits;
        this.w = randDigits;
        this.s = randDigits;
    }

    @Override
    public int getAsInt() {
        x *= x;
        x += (w += s);
        x = (x >> 32) | (x << 32);
        return (int) x;
    }

    /**
     * Gets a thread local initialised instance
     *
     * @return a thread local instance
     */
    public static MsWs getInstance() {
        return msWsThreadLocal.get();
    }

    @Override
    public synchronized void setSeed(long seed) {
        this.s = Init.randDigits(seed);
        this.x = s;
        this.w = s;
    }

    @Override
    protected int next(int bits) {
        assert bits <= 32;

        return getAsInt();
    }

}
