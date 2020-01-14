# Pure Java implementation of the Middle Square Weyl Sequence method

## Introduction

```java

long randN = MsWs.getInstance().getAsInt();

// or using the Random class
Random rnd = MsWs.getInstance();
rnd.nextLong();

```

## Benchmarks

These were run on my own macbook pro.
For your own delight, please run the benchmarks on a machine of your choosing.

That said: The benchmarks look very interesting MSWS is ~3.5 times faster.


```bash

## JRERandom == new Random(), MsWsNextInt is the middle square weyl random number generator
# Run complete. Total time: 00:04:05
Benchmark                   Mode  Cnt          Score         Error  Units
MsWsBenchTest.JRERandom    thrpt   60  120762889.792 ±  818461.652  ops/s
MsWsBenchTest.MsWsNextInt  thrpt   60  327446204.873 ± 3085282.287  ops/s
120762889.792 ±(99.9%) 818461.652 ops/s
327446204.873 ±(99.9%) 3085282.287 ops/s
```

## Licence

The original C implementation is published on `GPL`, I have publishes this code under `GPLv3`.
 