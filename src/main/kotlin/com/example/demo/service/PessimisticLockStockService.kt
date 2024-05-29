package com.example.demo.service

import com.example.demo.repository.StockRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PessimisticLockStockService(private val stockRepository: StockRepository) : IStockService {
    @Transactional
    override fun decrease(id: Long, quantity: Long) {
        val stock = stockRepository.findByIdWithPessimisticLock(id)
            ?: throw IllegalArgumentException("Stock not found")
        stock.decreaseQuantity(quantity)
        stockRepository.saveAndFlush(stock)
    }
}