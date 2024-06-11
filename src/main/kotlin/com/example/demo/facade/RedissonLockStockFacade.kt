package com.example.demo.facade

import com.example.demo.service.IStockService
import com.example.demo.service.StockService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedissonLockStockFacade(private val redissonClient: RedissonClient, private val stockService: StockService): IStockService {
    override fun decrease(id: Long, quantity: Long) {
        val lock = redissonClient.getLock(id.toString())

        try {
            val availability: Boolean = lock.tryLock(15, 1, TimeUnit.SECONDS)
            if (!availability) {
                println("failed to get lock")
                return
            }

            stockService.decreaseForNamedLocking(id, quantity)
        } catch (e:Exception){
            throw RuntimeException("failed to get lock")
        } finally {
            lock.unlock()
        }
    }
}