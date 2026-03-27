package com.dennis.ecommerce.catalogService.domain.entity

import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener::class)
class Product (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:Column(nullable = false, length = 255)
    var name: String,

    @field:Column(columnDefinition = "TEXT")
    var description: String? = null,

    @field:Column(nullable = false, precision = 12, scale = 2)
    var price: BigDecimal,

    @field:Column(nullable =false, unique = true, length = 100)
    var sku: String,

    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false, length = 20)
    var status: ProductStatus = ProductStatus.ACTIVE,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @field:OneToOne(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var stock: Stock?= null,

    @field:CreatedDate
    @field:Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @field:LastModifiedDate
    @field:Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
){
    override fun toString() = "Product(id=$id, sku=$sku)"
    override fun equals(other: Any?) = other is Product && id == other.id
    override fun hashCode() = id.hashCode()
}