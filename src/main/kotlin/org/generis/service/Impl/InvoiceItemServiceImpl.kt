package org.generis.service.Impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.dto.CreateInvoiceItemDto
import org.generis.dto.InvoiceItemDto
import org.generis.entity.InvoiceItem
import org.generis.entity.Product
import org.generis.service.InvoiceItemService
import org.modelmapper.ModelMapper


@Singleton
@Transactional
class InvoiceItemServiceImpl: InvoiceItemService {

    @Inject
    var entityManager: EntityManager? = null

    private val modelMapper = ModelMapper()

    override fun create(createInvoiceItemDto: CreateInvoiceItemDto): InvoiceItem {
        val product = entityManager?.find(Product::class.java, createInvoiceItemDto.productId)
            ?: throw IllegalArgumentException("Invalid productId")

        val total = createInvoiceItemDto.quantity * product.unitPrice!!

        val invoiceItem = InvoiceItem()
        invoiceItem.productId = product
        invoiceItem.quantity = createInvoiceItemDto.quantity
        invoiceItem.totalAmount = total

        entityManager!!.persist(invoiceItem)

        return invoiceItem
    }

    override fun get(id: String): InvoiceItem? {
        return entityManager?.find(InvoiceItem::class.java, id)
    }

}