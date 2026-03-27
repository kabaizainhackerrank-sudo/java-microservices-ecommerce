package com.dennis.ecommerce.catalogService.mapper

import com.dennis.ecommerce.catalogService.domain.entity.Category
import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.entity.Stock
import com.dennis.ecommerce.catalogService.dto.response.CategoryResponse
import com.dennis.ecommerce.catalogService.dto.response.ProductResponse
import com.dennis.ecommerce.catalogService.dto.response.ProductSummaryResponse
import com.dennis.ecommerce.catalogService.dto.response.StockResponse
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    fun toResponse(product: Product): ProductResponse =
        ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            sku = product.sku,
            status = product.status,
            category = toCategoryResponse(product.category),
            stock = product.stock?.let { toStockResponse(it) },
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )

    fun toSummaryResponse(product: Product): ProductSummaryResponse =
        ProductSummaryResponse(
            id = product.id,
            name = product.name,
            price = product.price,
            sku = product.sku,
            status = product.status,
            categoryName = product.category.name,
            stockAvailable = product.stock?.quantity ?: 0
        )

    fun toCategoryResponse(category: Category): CategoryResponse =
        CategoryResponse(
            id = category.id,
            name = category.name,
            parentId = category.parent?.id,
            parentName = category.parent?.name,
            children = category.children.map { toCategoryResponse(it) }
        )

    fun toStockResponse(stock: Stock): StockResponse =
        StockResponse(
            id = stock.id,
            quantity = stock.quantity,
            isInStock = stock.isInStock,
            updatedAt = stock.updatedAt
        )
}