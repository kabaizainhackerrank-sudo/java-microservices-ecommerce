package com.dennis.ecommerce.catalogService.service.interfaces

import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.entity.Stock

interface StockService {
    fun decrementStock(productId: Long, quantity: Int)
    fun incrementStock(productId: Long, quantity: Int)
    fun getStock(productId: Long): Int
    fun create(product: Product, initialQuantity: Int): Stock
}