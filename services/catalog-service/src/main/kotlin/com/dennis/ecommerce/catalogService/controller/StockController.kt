package com.dennis.ecommerce.catalogService.controller

import com.dennis.ecommerce.catalogService.dto.response.StockResponse
import com.dennis.ecommerce.catalogService.mapper.ProductMapper
import com.dennis.ecommerce.catalogService.service.interfaces.StockService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stock")
class StockController(
    private val stockService: StockService,
    private val mapper: ProductMapper
) {

    @GetMapping("/product/{productId}")
    fun getStock(@PathVariable productId: Long): ResponseEntity<StockResponse> {
        val quantity = stockService.getStock(productId)
        return ResponseEntity.ok(StockResponse(
            id = productId,
            quantity = quantity,
            isInStock = quantity > 0,
            updatedAt = java.time.LocalDateTime.now()
        ))
    }
}