package com.dennis.ecommerce.catalogService.service

import com.dennis.ecommerce.catalogService.domain.entity.Category
import com.dennis.ecommerce.catalogService.exception.DuplicateResourceException
import com.dennis.ecommerce.catalogService.exception.NotFoundException
import com.dennis.ecommerce.catalogService.repository.CategoryRepository
import com.dennis.ecommerce.catalogService.service.interfaces.CategoryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryServiceImpl(private val categoryRepository: CategoryRepository): CategoryService {

    private val log = LoggerFactory.getLogger(CategoryService::class.java)

    // ── Queries ───────────────────────────────────────────

    override fun findAll(): List<Category> =
        categoryRepository.findByParentIsNull()

    override fun findById(id: Long): Category =
        categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Categoría $id no encontrada") }

    override fun findRootCategories(): List<Category> =
        categoryRepository.findByParentIsNull()

    override fun findChildren(parentId: Long): List<Category> {
        findById(parentId) // valida que existe
        return categoryRepository.findByParentId(parentId)
    }

    @Transactional
    override fun create(name: String, parentId: Long?): Category {
        if (categoryRepository.existsByName(name)) {
            throw DuplicateResourceException("Ya existe una categoría con el nombre '$name'")
        }

        val parent = parentId?.let { findById(it) }

        val category = Category(
            name = name,
            parent = parent
        )

        val saved = categoryRepository.save(category)
        log.info("Categoría creada: id=${saved.id} name=${saved.name}")
        return saved
    }

    @Transactional
    override fun update(id: Long, name: String, parentId: Long?): Category {
        val category = findById(id)

        category.name = name
        category.parent = parentId?.let { findById(it) }

        val saved = categoryRepository.save(category)
        log.info("Categoría actualizada: id=${saved.id}")
        return saved
    }

    @Transactional
    override fun delete(id: Long) {
        val category = findById(id)
        categoryRepository.delete(category)
        log.info("Categoría eliminada: id=$id")
    }
}