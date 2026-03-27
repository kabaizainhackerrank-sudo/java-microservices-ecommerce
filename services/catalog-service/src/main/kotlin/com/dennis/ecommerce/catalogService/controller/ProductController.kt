package com.dennis.ecommerce.catalogService.controller

import com.dennis.ecommerce.catalogService.dto.request.ProductRequest
import com.dennis.ecommerce.catalogService.dto.request.ProductStatusRequest
import com.dennis.ecommerce.catalogService.dto.request.ProductUpdateRequest
import com.dennis.ecommerce.catalogService.dto.response.ProductResponse
import com.dennis.ecommerce.catalogService.dto.response.ProductSummaryResponse
import com.dennis.ecommerce.catalogService.mapper.ProductMapper
import com.dennis.ecommerce.catalogService.service.interfaces.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService,
    private val mapper: ProductMapper
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<ProductSummaryResponse>> {
        val products = productService.findAll()
        return ResponseEntity.ok(products.map { mapper.toSummaryResponse(it) })
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ProductResponse> {
        val product = productService.findById(id)
        return ResponseEntity.ok(mapper.toResponse(product))
    }

    @GetMapping("/sku/{sku}")
    fun getBySku(@PathVariable sku: String): ResponseEntity<ProductResponse> {
        val product = productService.findBySku(sku)
        return ResponseEntity.ok(mapper.toResponse(product))
    }

    @GetMapping("/search")
    fun search(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) minPrice: java.math.BigDecimal?,
        @RequestParam(required = false) maxPrice: java.math.BigDecimal?
    ): ResponseEntity<List<ProductSummaryResponse>> {
        val products = productService.search(
            name = name,
            categoryId = categoryId,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
        return ResponseEntity.ok(products.map { mapper.toSummaryResponse(it) })
    }

    @PostMapping
    fun create(@Valid @RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
        val product = productService.create(
            name = request.name,
            description = request.description,
            price = request.price,
            sku = request.sku,
            categoryId = request.categoryId,
            initialStock = request.initialStock
        )
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mapper.toResponse(product))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.update(
            id = id,
            name = request.name,
            description = request.description,
            price = request.price,
            categoryId = request.categoryId
        )
        return ResponseEntity.ok(mapper.toResponse(product))
    }

    @PatchMapping("/{id}/status")
    fun changeStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductStatusRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.changeStatus(id, request.status)
        return ResponseEntity.ok(mapper.toResponse(product))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}