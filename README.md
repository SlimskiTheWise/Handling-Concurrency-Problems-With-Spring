# Handling Concurrency Problems

This project is a sample project designed to demonstrate and practice handling concurrency problems.

## What are Concurrency Problems?

When a server receives multiple requests from clients that require simultaneous database lookups and updates, concurrency
problems might occur.

For example, if a server receives two requests to subtract the `quantity` property in the `Stock` table by 1, it will
first look up the current quantity and then subtract. During this process, let's say one thread selects the `Stock`
table where the `quantity` is 100, and another thread does the same. If both threads then subtract 1, the final quantity
will end up being 99 instead of 98. This is called a [race condition](https://en.wikipedia.org/wiki/Race_condition).

