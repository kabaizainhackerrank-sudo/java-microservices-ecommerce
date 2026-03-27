package com.dennis.ecommerce.catalogService.service.interfaces

import com.dennis.ecommerce.catalogService.domain.entity.Category

interface CategoryService {
    fun create(name: String, parentId: Long?): Category
    fun update(id: Long, name: String, parentId: Long?): Category
    fun delete(id: Long)
    fun findAll(): List<Category>
    fun findById(id: Long): Category
    fun findRootCategories(): List<Category>
    fun findChildren(parentId: Long): List<Category>
}