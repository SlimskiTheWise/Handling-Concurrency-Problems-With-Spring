package com.example.demo.service

import com.example.demo.domain.Stock
import com.example.demo.repository.StockRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class StockService (val stockRepository: StockRepository) {

    @Transactional
    fun decrease (id: Long, quantity: Long) {
        val stock:Stock = stockRepository.findById(id).orElseThrow()
        stock.decreaseQuantity(quantity)

        stockRepository.saveAndFlush(stock)
    }
}