package com.example.demo.service

import com.example.demo.repository.StockRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class OptimisticLockStockService (private val stockRepo: StockRepository) {
    @Transactional
    fun decreaseStock(id: Long, quantity: Long) {
        val stock = stockRepo.findByIdWithOptimisticLock(id)
            ?: throw IllegalArgumentException("Stock with id $id not found")
        stock.decreaseQuantity(quantity)
        stockRepo.save(stock)
    }

}