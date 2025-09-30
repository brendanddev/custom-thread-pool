# Custom Thread Pool
## Brendan Dileo

A Java implementation of a custom thread pool that demonstrates how task execution, worker management, and graceful shutdown work under the hood.

This project was built from scratch to better understand concurrency in Java without relying on `Executors.newFixedThreadPool()` or similar high-level abstractions.

## Features

- Fixed-size pool of worker threads.
- Blocking queue for pending tasks.
- Graceful shutdown (`shutdown()`) and immediate shutdown (`shutdownNow()`).
- Demo with real-time monitoring of:
  - Queue size
  - Worker thread states
  - Task start/finish logs

---

## Project Structure

```
src/main/java/com/brendanddev/threadpool/
├── BlockingQueue.java # Simple blocking queue for tasks
├── CustomRunnable.java # Wrapper for submitted tasks
├── CustomThreadPool.java # Thread pool implementation
├── Worker.java # Worker thread logic
└── Main.java # Demo runner with monitoring
```