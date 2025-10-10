# Custom Thread Pool

A custom thread pool implementation in Java, built from scratch to explore and understand the **internal workings of thread pools**. This project focuses on implementing the core mechanisms manually, rather than relying on Java’s built-in concurrency utilities.

---

## Features

- **Custom TaskQueue** – A thread-safe FIFO queue implemented from scratch using `synchronized`, `wait()`, and `notifyAll()`.
- **WorkerThread Management** – Multiple worker threads continuously dequeue and execute tasks.  
- **Graceful Shutdown** – Supports a clean shutdown where queued tasks are completed using the **poison pill pattern**.
- **Immediate Shutdown** – Can stop all active tasks and return unexecuted tasks immediately.
- **Rejection Policies** – Handles tasks submitted after shutdown with configurable strategies:
  - **ABORT_POLICY** – Throws a `RuntimeException`.
  - **DISCARD_POLICY** – Silently discards the task.
  - **CALLER_RUNS_POLICY** – Runs the task in the calling thread.
  - **DISCARD_OLDEST_POLICY** – Removes the oldest queued task to make room for the new task.
- **Exception Handling** – Worker threads catch exceptions from tasks to prevent thread death.
- **Custom Thread Factory** – Allows naming threads and configuring priorities.

---

## Project Structure
```
custom-thread-pool/
├─ pom.xml
├─ README.md
├─ src/
│ ├─ main/java/com/brendanddev/threadpool/
│ │ ├─ CustomThreadPool.java
│ │ ├─ TaskQueue.java
│ │ ├─ WorkerThread.java
│ │ ├─ CustomThreadFactory.java
│ │ ├─ Main.java
│ │ └─ policies/
│ │ ├─ RejectionHandler.java
│ │ └─ RejectionHandlers.java
├─ target/...
```
---

## Usage

1. **Compile and run:**
```bash
mvn clean compile exec:java
```

---

### References & Inspiration
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Baeldung: Building Thread Pools](https://www.baeldung.com/thread-pool-java-and-guava)
- [Poison Pill Pattern](https://java-design-patterns.com/patterns/poison-pill/)
- [Thread Pool Policies](https://medium.com/@ankithahjpgowda/policies-of-threadpoolexecutor-in-java-75f22fd6f637)
- [Java Thread Pool Execution and Rejection Policies](https://medium.com/@umeshcapg/understanding-java-thread-pool-execution-and-rejection-policies-b97eeb58094a)
