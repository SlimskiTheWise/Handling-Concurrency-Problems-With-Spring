package com.example.demo.service

import com.example.demo.domain.Stock
import com.example.demo.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation

@Service
class StockService (val stockRepository: StockRepository): IStockService {
    override fun decrease(id: Long, quantity: Long) {
        synchronized(this) {
            val stock: Stock = stockRepository.findById(id).orElseThrow()
            stock.decreaseQuantity(quantity)
            stockRepository.saveAndFlush(stock)
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun decreaseForNamedLocking(id: Long, quantity: Long) {
        val stock: Stock = stockRepository.findById(id).orElseThrow()
        stock.decreaseQuantity(quantity)
        stockRepository.saveAndFlush(stock)
    }
}