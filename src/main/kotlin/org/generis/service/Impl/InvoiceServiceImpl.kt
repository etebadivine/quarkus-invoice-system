package org.generis.service.Impl

import jakarta.inject.*
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.dto.*
import org.generis.entity.*
import org.generis.exception.ServiceException
import org.generis.service.InvoiceService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Singleton
@Transactional
class InvoiceServiceImpl: InvoiceService {

    @Inject
    lateinit var entityManager: EntityManager

    override fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice {
        // Retrieve the customer based on the customerId from the database
        val customer = entityManager.find(Customer::class.java, createInvoiceDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        val currency = entityManager!!.find(Currency::class.java, createInvoiceDto.currency)
            ?:  throw ServiceException(-1, "No currency found")

        // Create the Invoice instance
        val invoice = Invoice()
        invoice.invoiceNumber = invoice.generateInvoiceNumber()
        invoice.title = createInvoiceDto.title
        invoice.subHeading = createInvoiceDto.subHeading
        invoice.dueDate =
            LocalDate.parse(createInvoiceDto.dueDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        invoice.createdDate = LocalDateTime.now()
        invoice.customerId = customer
        invoice.tax = createInvoiceDto.tax
        invoice.discount = createInvoiceDto.discount
        invoice.currency = currency
        invoice.subTotal = 0.00
        invoice.totalAmount = 0.00

        // Calculate the subtotal and total based on the invoice items
        var subtotal = 0.00
        for (itemDto in createInvoiceDto.items) {
            // Retrieve the product based on the productId from the database
            val product = entityManager.find(Product::class.java, itemDto.productId)
                ?: throw IllegalArgumentException("Invalid productId")

            // Calculate the total for the invoice item based on the product price and quantity
            val itemTotal = product.unitPrice?.times(itemDto.quantity!!)?.times(currency.exchangeRate!!)

            // Create the InvoiceItem instance
            val invoiceItem = InvoiceItem()
            invoiceItem.productId = product
            invoiceItem.quantity = itemDto.quantity
            invoiceItem.totalAmount = itemTotal
            invoiceItem.invoice = invoice

            // Add the invoice item to the invoice
            invoice.items.add(invoiceItem)

            // Update the subtotal
            if (itemTotal != null) {
                subtotal += itemTotal
            }
        }

        // Apply tax and discount to calculate the total
        val discountPercent = createInvoiceDto.discount
        val discountAmount = subtotal * (discountPercent?.div(100.0)!!)

        val taxPercent = createInvoiceDto.tax
        val taxAmount = subtotal * (taxPercent?.div(100.0)!!)

        // Apply tax and discount to calculate the total
        invoice.subTotal = subtotal
        invoice.totalAmount = subtotal + taxAmount - discountAmount

        // Save the invoice to the database
        entityManager.persist(invoice)

        return invoice

    }

    override fun getInvoice(id: String): Invoice? {
        return entityManager.find(Invoice::class.java, id)
    }

    override fun getAllInvoices(): List<Invoice> {
        return entityManager.createQuery("SELECT i FROM Invoice i", Invoice::class.java)?.resultList
            ?: throw ServiceException(-1, "No invoices found")
    }

    override fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice {
        val invoice = entityManager.find(Invoice::class.java, updateInvoiceStatusDto.invoiceId)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        invoice.status = updateInvoiceStatusDto.status

        return entityManager.merge(invoice)
    }

    override fun getInvoiceByCustomerId(customerId: String): List<Invoice> {

        val query: TypedQuery<Invoice> = entityManager.createQuery(
            "SELECT i FROM Invoice i WHERE i.customerId.id = :customerId",
            Invoice::class.java
        )
        query.setParameter("customerId", customerId)

        return query.resultList
    }


    override fun deleteInvoice(id: String) {
        val invoice = entityManager.find(Invoice::class.java, id)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        entityManager.remove(invoice)
    }
}

