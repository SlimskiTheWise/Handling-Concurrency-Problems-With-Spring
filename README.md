# Handling Concurrency Problems

This project is a sample project designed to demonstrate and practice handling concurrency problems.

## What are Concurrency Problems?

When a server receives multiple requests from clients that require simultaneous database lookups and updates,
concurrency
problems might occur.

For example, if a server receives two requests to subtract the `quantity` property in the `Stock` table by 1, it will
first look up the current quantity and then subtract. During this process, let's say one thread selects the `Stock`
table where the `quantity` is 100, and another thread does the same. If both threads then subtract 1, the final quantity
will end up being 99 instead of 98. This is called a [race condition](https://en.wikipedia.org/wiki/Race_condition).

## Using Synchronized Keyword

In Kotlin, you can use the synchronized function to achieve synchronization similar to the `synchronized` keyword in
Java.
This ensures that only one thread can execute the critical section of code at a time.

```kotlin
fun decrease(id: Long, quantity: Long) {
    synchronized(this) {
        val stock: Stock = stockRepository.findById(id).orElseThrow()
        stock.decreaseQuantity(quantity)
        stockRepository.saveAndFlush(stock)
    }
}

```

### Potential Problems of the Solution Above

When you use `synchronized(this)` within a method, it locks the instance of the class, ensuring that only one thread can
execute that block at a time. However, the combination of `@Transactional` and `synchronized` can lead to issues:

- The `@Transactional` annotation can cause the method to be executed in a different proxy context, which might not
  respect
  the synchronized block properly.
- The transaction management might not behave as expected if the method is not being executed within the same thread
  context, leading to potential inconsistencies.

Moreover, using `synchronized(this)` only works effectively when there is a single server process. This synchronization
mechanism ensures consistency within one process by preventing concurrent access to the synchronized block. However, if
there are multiple servers or processes, `synchronized(this)` will not prevent concurrent access across different
processes, leading to potential data inconsistencies. In distributed systems, relying solely on in-process
synchronization is insufficient for ensuring data integrity.

## Solution

To properly handle concurrency in a transactional context, you can use other mechanisms like:

### Pessimistic Locking

Lock the database row to ensure that only one transaction can modify it at a time.

### Optimistic Locking

Use versioning to ensure that only one transaction can commit changes based on the version number.

### Named Locking

Named locking (also known as application-level locking) uses a named lock to control access to a resource. It is useful
for ensuring that only one transaction at a time can access a specific resource across different transactions and even
across different instances of the application. 