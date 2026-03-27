package com.dennis.ecommerce.catalogService.dto.response

data class CategoryResponse(
    val id: Long,
    val name: String,
    val parentId: Long?,
    val parentName: String?,
    val children: List<CategoryResponse>
)