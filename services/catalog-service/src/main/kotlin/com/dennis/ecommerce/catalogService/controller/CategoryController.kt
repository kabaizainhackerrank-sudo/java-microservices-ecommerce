package com.dennis.ecommerce.catalogService.controller

import com.dennis.ecommerce.catalogService.dto.request.CategoryRequest
import com.dennis.ecommerce.catalogService.dto.response.CategoryResponse
import com.dennis.ecommerce.catalogService.mapper.ProductMapper
import com.dennis.ecommerce.catalogService.service.interfaces.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService,
    private val mapper: ProductMapper
) {

    @GetMapping
    fun getAll(): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.findAll()
        return ResponseEntity.ok(categories.map { mapper.toCategoryResponse(it) })
    }

    @GetMapping("/roots")
    fun getRoots(): ResponseEntity<List<CategoryResponse>> {
        val roots = categoryService.findRootCategories()
        return ResponseEntity.ok(roots.map { mapper.toCategoryResponse(it) })
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        val category = categoryService.findById(id)
        return ResponseEntity.ok(mapper.toCategoryResponse(category))
    }

    @GetMapping("/{id}/children")
    fun getChildren(@PathVariable id: Long): ResponseEntity<List<CategoryResponse>> {
        val children = categoryService.findChildren(id)
        return ResponseEntity.ok(children.map { mapper.toCategoryResponse(it) })
    }

    @PostMapping
    fun create(@Valid @RequestBody request: CategoryRequest): ResponseEntity<CategoryResponse> {
        val category = categoryService.create(
            name = request.name,
            parentId = request.parentId
        )
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mapper.toCategoryResponse(category))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.update(
            id = id,
            name = request.name,
            parentId = request.parentId
        )
        return ResponseEntity.ok(mapper.toCategoryResponse(category))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.delete(id)
        return ResponseEntity.noContent().build()
    }
}