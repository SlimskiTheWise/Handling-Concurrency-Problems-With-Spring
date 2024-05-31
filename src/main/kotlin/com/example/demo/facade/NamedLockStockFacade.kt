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
) : IStockService {

    @Transactional
    override fun decrease(id: Long, quantity: Long) {
        try {
            val lockResult = lockRepository.getLock(id.toString())
            println("Lock acquisition attempt for id: $id -> success: $lockResult")
            if (lockResult == 1) {
                stockService.decreaseForNamedLocking(id, quantity)
            }
        } catch (e: Exception) {
            println("Exception during lock acquisition or processing for id: $id -> ${e.message}")
            throw e
        } finally {
            val lockReleased = lockRepository.releaseLock(id.toString()) == 1
            println("Lock released for id: $id -> success: $lockReleased")
        }
    }
}