package org.generis.business.invoice.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.*
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.currency.repo.Currency
import org.generis.business.customer.repo.Customer
import org.generis.business.invoice.dto.CreateInvoiceDto
import org.generis.business.invoice.dto.UpdateInvoiceStatusDto
import org.generis.business.invoice.repo.Invoice
import org.generis.business.invoice.repo.InvoiceItem
import org.generis.business.product.repo.Product
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Transactional
@ApplicationScoped
class InvoiceServiceImpl: InvoiceService {

    @Inject
    lateinit var entityManager: EntityManager

    override fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice? {
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
        invoice.currency = currency
        invoice.tax = createInvoiceDto.tax
        invoice.discount = createInvoiceDto.discount

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
        val discountAmount = subtotal * (discountPercent?.div(100)?: 0.0)

        val taxPercent = createInvoiceDto.tax
        val taxAmount = subtotal * (taxPercent?.div(100)?: 0.0)

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

    override fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice? {
        val invoice = entityManager.find(Invoice::class.java, updateInvoiceStatusDto.invoiceId)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        invoice.status = updateInvoiceStatusDto.status

        return entityManager.merge(invoice)
    }


    override fun deleteInvoice(id: String) {
        val invoice = entityManager.find(Invoice::class.java, id)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        entityManager.remove(invoice)
    }
}

