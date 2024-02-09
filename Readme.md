<h1>BjornDB</h1>

<p>BjornDB is a JVM based KV Database, which is fast and thread-safe. BjornDB is 20x faster for reads and 10x faster for writes than Bitcask, which has a similar architecture.<p></p>

<p>BjornDB adopts a hash-based design, deliberately ignoring range queries to prioritize throughput and latency. As a result, it is faster at writes than LevelDB (Google) and RocksDB (Facebook).</p>

BjornDB is designed with educated tradeoffs to achieve high performance:

1. BjornDB requires that all keys be held in memory, a necessary compromise for their design. With the capability to accommodate keys up to 32KB in size, BjornDB efficiently manages to store over 32,000 keys within just 1GB of RAM

2. Maximum key size is 32768 bytes or 32KB.

3. Maximum value size is 2,147,483,647 bytes or 2.14 GB.
4. BjornDB does not support range queries.

<h4>Currently, in the process of acquiring a licence and publishing a maven dependency.</h4>

Benchmarks
---
<hr>
    
    iterations: 100,000
    cpu: 1
    memory: 1GB
    key-size: 8 bytes
    value-size: 100 bytes

Potential Improvements
-----
<hr>

Add delete Key

will add new improvements or additional functionalities in future