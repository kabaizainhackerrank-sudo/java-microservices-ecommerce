package com.dennis.ecommerce.catalogService.repository

import com.dennis.ecommerce.catalogService.domain.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>{
    // Buscar todas las categorías raíz (sin padre)
    fun findByParentIsNull(): List<Category>

    // Buscar hijos de una categoría
    fun findByParentId(parentId: Long): List<Category>

    // Verificar si existe por nombre
    fun existsByName(name: String): Boolean
}