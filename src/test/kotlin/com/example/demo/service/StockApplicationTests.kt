package com.example.demo.service

import com.example.demo.domain.Stock
import com.example.demo.facade.LettuceLockStockFacade
import com.example.demo.facade.NamedLockStockFacade
import com.example.demo.facade.OptimisticLockStockFacade
import com.example.demo.repository.StockRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
class StockApplicationTests(
    @Autowired val stockRepository: StockRepository,
    @Autowired val stockService: StockService,
    @Autowired val pessimisticLockStockService: PessimisticLockStockService,
    @Autowired val optimisticLockStockFacade: OptimisticLockStockFacade,
    @Autowired val namedLockStockFacade: NamedLockStockFacade,
    @Autowired val lettuceLockStockFacade: LettuceLockStockFacade,
) {
    fun <T : IStockService> sendRequests(service: T) {
        val threadCount = 100
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    service.decrease(1L, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
    }

    @BeforeEach
    fun seedStock() {
        stockRepository.save(Stock(productId = 1L, quantity = 100L))
    }

    @AfterEach
    fun deleteStock() {
        stockRepository.deleteAll()
    }

    @Test
    fun decrease_quantity() {
        stockService.decrease(1L, 1L)

        // 100 - 1 = 99
        val stock: Stock = stockRepository.findById(1L).orElseThrow()

        assertThat(stock.quantity).isEqualTo(99L)
    }

    @Test
    fun decrease_quantity_multiple_times_at_once() {
        sendRequests(stockService)

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }

    @Test
    fun decrease_quantity_multiple_times_at_once_pessimistic_lock() {
        sendRequests(pessimisticLockStockService)

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }

    @Test
    fun decrease_quantity_multiple_times_at_once_optimistic_lock() {
        sendRequests(optimisticLockStockFacade)

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }

    @Test
    fun decrease_quantity_multiple_times_at_once_named_lock() {
        sendRequests(namedLockStockFacade)

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }

    @Test
    fun decrease_quantity_multiple_times_at_spin_lock() {
        sendRequests(lettuceLockStockFacade)

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }
}