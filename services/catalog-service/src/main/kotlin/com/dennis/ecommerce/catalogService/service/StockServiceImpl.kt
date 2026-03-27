package com.dennis.ecommerce.catalogService.service

import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.entity.Stock
import com.dennis.ecommerce.catalogService.exception.NotFoundException
import com.dennis.ecommerce.catalogService.repository.StockRepository
import com.dennis.ecommerce.catalogService.service.interfaces.StockService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)// este en la clase significa que tosos los metodos por defecto sera de lectura, el metodo que tenga otro anula este
class StockServiceImpl(private val stockRepository: StockRepository) : StockService {


    private val log = LoggerFactory.getLogger(StockService::class.java)

    override fun getStock(productId: Long): Int {
        val stock = stockRepository.findByProductId(productId)
            ?: throw NotFoundException("Stock no encontrado para producto $productId")
        return stock.quantity
    }

    @Transactional
    override fun decrementStock(productId: Long, quantity: Int) {
        val stock = stockRepository.findByProductId(productId)
            ?: throw NotFoundException("Stock no encontrado para producto $productId")

        stock.decrement(quantity)
        stockRepository.save(stock)
        log.info("Stock decrementado: productId=$productId qty=$quantity nuevoStock=${stock.quantity}")
    }

    @Transactional
    override fun incrementStock(productId: Long, quantity: Int) {
        val stock = stockRepository.findByProductId(productId)
            ?: throw NotFoundException("Stock no encontrado para producto $productId")

        stock.increment(quantity)
        stockRepository.save(stock)
        log.info("Stock incrementado: productId=$productId qty=$quantity nuevoStock=${stock.quantity}")
    }

    @Transactional
    override fun create(product: Product, initialQuantity: Int): Stock {
        val stock = Stock(product = product, quantity = initialQuantity)
        val saved = stockRepository.save(stock)
        log.info("Stock creado: productId=${product.id} quantity=$initialQuantity")
        return saved
    }
}