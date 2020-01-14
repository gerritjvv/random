package com.gerritjvv.random.msws;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertTrue;

@BenchmarkMode(Mode.Throughput)
public class MsWsBenchTest {

    /**
     * not a randomness test, and is here to show usage
     */
    @Benchmark
    @Fork(3)
    public int MsWsNextInt(RndState rndState) {
        return rndState.mswsRnd.nextInt();
    }


    /**
     * not a randomness test, and is here to show usage
     */
    @Benchmark
    @Fork(3)
    public int JRERandom(RndState rndState) {
        return rndState.jreRnd.nextInt();
    }



    @Test
    public void runTest() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MsWsBenchTest.class.getSimpleName())
                .build();
        Collection<RunResult> runResults = new Runner(opt).run();
        for(RunResult runResult : runResults) {
            System.out.println(runResult.getAggregatedResult().getPrimaryResult().toString());
        }
    }

    @org.openjdk.jmh.annotations.State(Scope.Thread)
    public static class RndState {
        public Random mswsRnd = MsWs.getInstance();
        public Random jreRnd = new Random();
    }

}
