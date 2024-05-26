package com.example.demo.service

import com.example.demo.domain.Stock
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
    @Autowired val stockService: StockService
) {

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
        val threadCount = 100
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    stockService.decrease(1L, 1L)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        val stock: Stock = stockRepository.findById(1L).orElseThrow()
        //should be 100 - (1 * 100) = 0
        assertThat(stock.quantity).isEqualTo(0)
    }
}