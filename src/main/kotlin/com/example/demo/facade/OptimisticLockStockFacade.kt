package com.example.demo.facade

import com.example.demo.service.IStockService
import com.example.demo.service.OptimisticLockStockService
import org.springframework.stereotype.Service

@Service
class OptimisticLockStockFacade(private val optimisticLockStockService: OptimisticLockStockService) : IStockService {
    override fun decrease(id: Long, quantity: Long) {
        while (true) {
            try {
                optimisticLockStockService.decreaseStock(id, quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(1)
            }
        }
    }
}