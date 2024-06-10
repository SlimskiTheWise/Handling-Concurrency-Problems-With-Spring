package com.example.demo.facade

import com.example.demo.repository.RedisRepository
import com.example.demo.service.IStockService
import com.example.demo.service.StockService
import org.springframework.stereotype.Service

@Service
class LettuceLockStockFacade(
    private val redisRepository: RedisRepository,
    private val stockService: StockService
) : IStockService {
    override fun decrease(id: Long, quantity: Long) {
        while (!redisRepository.lock(id)) {
            Thread.sleep(100)
        }

        try {
            stockService.decreaseForNamedLocking(id, quantity)
        } finally {
            redisRepository.unlock(id)
        }
    }

}