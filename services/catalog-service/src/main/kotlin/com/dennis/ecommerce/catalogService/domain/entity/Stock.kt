package com.dennis.ecommerce.catalogService.domain.entity

import com.dennis.ecommerce.catalogService.exception.BusinessException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "stock")
@EntityListeners(AuditingEntityListener::class)
class Stock (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    var product: Product,

    @Column(nullable = false)
    var quantity: Int = 0,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
){
    val isInStock: Boolean
        get() = quantity > 0

    fun decrement(qty: Int) {
        if (quantity < qty) {
            throw BusinessException(
                "Stock insuficiente para producto ${product.id}. " +
                        "Disponible: $quantity, solicitado: $qty"
            )
        }
        quantity -= qty
    }

    fun increment(qty: Int) {
        quantity += qty
    }

    override fun toString() = "Stock(id=$id, quantity=$quantity)"
    override fun equals(other: Any?) = other is Stock && id == other.id
    override fun hashCode() = id.hashCode()
}