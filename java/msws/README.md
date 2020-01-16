# Pure Java PRNG of the Generator Middle Square Weyl Sequence method in Pure Java

## Introduction


The <a href="https://en.wikipedia.org/wiki/Middle-square_method">Middle Square Weyl Sequence PRNG</a> is one of the fastest methods of <a href="https://en.wikipedia.org/wiki/Pseudorandom_number_generator">psuedo random number generation</a> available.

This implementation takes directly from the C implementation and makes a few modifications to make it Java friendly.

The original paper can be found at <a href="https://arxiv.org/abs/1704.00358">https://arxiv.org/abs/1704.00358</a>.

```java
Random rnd = MsWs.getInstance();
rnd.nextLong();
```

## How fast is it?

Its about 3.5 times faster than the standard Java Random class.

### Benchmarks

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

The original C implementation is published on `GPLv3`, and thus the Java code is under `GPLv3` also.
 
