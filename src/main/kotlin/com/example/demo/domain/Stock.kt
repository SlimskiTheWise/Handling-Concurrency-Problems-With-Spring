package com.example.demo.domain

import jakarta.persistence.*

@Entity()
class Stock (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false)
    var quantity: Long,
) {
    fun decreaseQuantity(quantity: Long) {
        if (this.quantity <= 0) throw IllegalArgumentException("Quantity must be greater than zero")

        this.quantity -= quantity
    }
}