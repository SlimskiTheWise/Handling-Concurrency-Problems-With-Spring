package com.example.demo.facade

import com.example.demo.repository.LockRepository
import com.example.demo.service.IStockService
import com.example.demo.service.StockService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NamedLockStockFacade(
    private val lockRepository: LockRepository,
    private val stockService: StockService
): IStockService {
    @Transactional
    override fun decrease(id: Long, quantity: Long) {
        try {
            lockRepository.getLock(id.toString())
            stockService.decrease(id, quantity)
        } finally {
            lockRepository.releaseLock(id.toString())
        }
    }
}