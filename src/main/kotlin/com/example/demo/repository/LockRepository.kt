package com.example.demo.repository

import com.example.demo.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LockRepository : JpaRepository<Stock, Long> {
    @Query("SELECT GET_LOCK(:key, 3)", nativeQuery = true)
    fun getLock(@Param("key") key: String): Int?

    @Query("SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    fun releaseLock(@Param("key") key: String): Int?
}



