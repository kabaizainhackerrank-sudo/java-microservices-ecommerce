package com.dennis.ecommerce.catalogService.service

import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.entity.Stock
import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import com.dennis.ecommerce.catalogService.exception.DuplicateResourceException
import com.dennis.ecommerce.catalogService.exception.NotFoundException
import com.dennis.ecommerce.catalogService.messaging.event.ProductCreatedEvent
import com.dennis.ecommerce.catalogService.messaging.event.ProductDeactivatedEvent
import com.dennis.ecommerce.catalogService.messaging.publisher.ProductEventPublisher
import com.dennis.ecommerce.catalogService.repository.ProductRepository
import com.dennis.ecommerce.catalogService.repository.StockRepository
import com.dennis.ecommerce.catalogService.service.interfaces.CategoryService
import com.dennis.ecommerce.catalogService.service.interfaces.ProductService
import com.dennis.ecommerce.catalogService.service.interfaces.StockService
import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional(readOnly = true)
class ProductServiceImpl (
    private val productRepository: ProductRepository,
    private val stockRepository: StockService,
    private val categoryService: CategoryService,
    private val productEventPublisher: ProductEventPublisher
) : ProductService {

    private val log = LoggerFactory.getLogger(ProductService::class.java)

    override fun findAll(): List<Product> = productRepository.findAll()

    override fun findById(id: Long): Product = productRepository.findById(id).orElseThrow {
        NotFoundException("Producto $id no encontrado")
    }

    override fun findBySku(sku: String): Product = productRepository.findBySku(sku)
            ?: throw NotFoundException("Producto con SKU '$sku' no encontrado")

    override fun search(
        name: String?,
        categoryId: Long?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?
    ): List<Product> =
        productRepository.search(
            name = name,
            categoryId = categoryId,
            minPrice = minPrice,
            maxPrice = maxPrice,
            status = ProductStatus.ACTIVE
        )

    @Transactional
    override fun create(
        name: String,
        description: String?,
        price: BigDecimal,
        sku: String,
        categoryId: Long,
        initialStock: Int
    ): Product {
        if (productRepository.existsBySku(sku)) {
            throw DuplicateResourceException("Ya existe un producto con SKU '$sku'")
        }

        val category = categoryService.findById(categoryId)

        val product = Product(
            name = name,
            description = description,
            price = price,
            sku = sku,
            category = category
        )

        val saved = productRepository.save(product)

        // Crear stock inicial y se asigna al producto
        saved.stock = stockRepository.create(product,initialStock)

        // Publicar evento
        productEventPublisher.publishProductCreated(
            ProductCreatedEvent(
                productId = saved.id,
                name = saved.name,
                categoryId = saved.category.id,
                price = saved.price,
                stockAvailable = initialStock
            )
        )

        log.info("Producto creado: id=${saved.id} sku=${saved.sku}")
        return saved
    }

    @Transactional
    override fun update(
        id: Long,
        name: String,
        description: String?,
        price: BigDecimal,
        categoryId: Long
    ): Product {
        val product = findById(id)
        val category = categoryService.findById(categoryId)

        product.name = name
        product.description = description
        product.price = price
        product.category = category

        val saved = productRepository.save(product)
        log.info("Producto actualizado: id=${saved.id}")
        return saved
    }

    @Transactional
    override fun changeStatus(id: Long, status: ProductStatus): Product {
        val product = findById(id)
        product.status = status

        val saved = productRepository.save(product)

        // Si se desactiva publicar evento
        if (status == ProductStatus.INACTIVE) {
            productEventPublisher.publishProductDeactivated(
                ProductDeactivatedEvent(
                    productId = saved.id,
                    name = saved.name
                )
            )
        }

        log.info("Estado cambiado: id=${saved.id} status=$status")
        return saved
    }

    @Transactional
    override fun delete(id: Long) {
        val product = findById(id)
        productRepository.delete(product)
        log.info("Producto eliminado: id=$id")
    }
}