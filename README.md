# Custom Thread Pool

A custom thread pool implementation in Java, built from scratch to explore and understand the **internal workings of thread pools**. This project focuses on implementing the core mechanisms manually, rather than relying on Java’s built-in concurrency utilities.

---

## Features

- **Custom TaskQueue** – A thread-safe FIFO queue implemented from scratch using `synchronized`, `wait()`, and `notifyAll()`.
- **WorkerThread Management** – Multiple worker threads continuously dequeue and execute tasks.  
- **Graceful Shutdown** – Supports a clean shutdown where queued tasks are completed using the **poison pill pattern**.
- **Immediate Shutdown** – Can stop all active tasks and return unexecuted tasks immediately.
- **Exception Handling** – Worker threads catch exceptions from tasks to prevent thread death.

---

### References & Inspiration
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Baeldung: Building Thread Pools](https://www.baeldung.com/thread-pool-java-and-guava)
- [Poison Pill Pattern](https://java-design-patterns.com/patterns/poison-pill/)
