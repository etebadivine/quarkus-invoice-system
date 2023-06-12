package org.generis.service.Impl

import io.quarkus.mailer.Attachment
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.inject.*
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Email
import org.generis.dto.*
import org.generis.entity.*
import org.generis.exception.ServiceException
import org.generis.service.InvoiceService
import org.generis.util.JacksonUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Singleton
@Transactional
class InvoiceServiceImpl: InvoiceService {

    @Inject
    var entityManager: EntityManager? = null

//    @Inject
//    private lateinit var mailer: Mailer


    override fun createInvoice(createInvoiceDto: CreateInvoiceDto): Invoice {
        // Retrieve the customer based on the customerId from the database
        val customer = entityManager?.find(Customer::class.java, createInvoiceDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        // Create the Invoice instance
        val invoice = Invoice()
            invoice.invoiceNumber = invoice.generateInvoiceNumber()
            invoice.title = createInvoiceDto.title
            invoice.subHeading = createInvoiceDto.subHeading
            invoice.dueDate = LocalDate.parse(createInvoiceDto.dueDate, DateTimeFormatter.ofPattern(JacksonUtils.datePattern))
            invoice.createdDate = LocalDateTime.now()
            invoice.customerId = customer
            invoice.tax = createInvoiceDto.tax
            invoice.discount = createInvoiceDto.discount
            invoice.currency = createInvoiceDto.currency
            invoice.subTotal = 0.00
            invoice.totalAmount = 0.00

        // Calculate the subtotal and total based on the invoice items
        var subtotal =  0.00
        for (itemDto in createInvoiceDto.items) {
            // Retrieve the product based on the productId from the database
            val product = entityManager?.find(Product::class.java, itemDto.productId)
                ?: throw IllegalArgumentException("Invalid productId")

            // Calculate the total for the invoice item based on the product price and quantity
            val itemTotal = product.unitPrice?.times(itemDto.quantity!!)?.times(createInvoiceDto.currency.exchangeRate)

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
        entityManager!!.persist(invoice)

        return invoice

    }

    override fun getInvoice(id: String): InvoiceDto? {
        val invoice = entityManager?.find(Invoice::class.java, id)
        return invoice?.toDto()
    }

    override fun getAllInvoices(): List<InvoiceDto> {
        val invoices = entityManager?.createQuery("SELECT i FROM Invoice i", Invoice::class.java)?.resultList
        return invoices?.map { it.toDto() } ?: throw ServiceException(-1, "No invoices found")
    }

    override fun updateInvoiceStatus(updateInvoiceStatusDto: UpdateInvoiceStatusDto): Invoice {
        val invoice = entityManager?.find(Invoice::class.java, updateInvoiceStatusDto.invoiceId)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        invoice.status = updateInvoiceStatusDto.status

        return entityManager!!.merge(invoice)
    }

//    override fun sendMail(invoice: Invoice) {
//        mailer.send(Mail.withText("kofidvyn@gmail.com",
//            "A simple email from quarkus #${invoice.invoiceNumber}",
//            "This is my body."));
//
//    }

    override fun deleteInvoice(id: String) {
        val invoice = entityManager?.find(Invoice::class.java, id)
            ?: throw IllegalArgumentException("Invalid invoiceId")

        entityManager!!.remove(invoice)
    }

    // Utility method to convert Invoice entity to InvoiceDto
    private fun Invoice.toDto(): InvoiceDto {
        return InvoiceDto(
            id = this.id,
            title = this.title,
            subHeading = this.subHeading,
            customerId = this.customerId?.id,
            invoiceNumber = this.invoiceNumber,
            currency = this.currency,
            items = this.items.map { it.toDto() },
            status = this.status,
            createdDate = this.createdDate,
            dueDate = this.dueDate,
            tax = this.tax,
            discount = this.discount,
            subTotal = this.subTotal,
            totalAmount = this.totalAmount
        )
    }

    private fun InvoiceItem.toDto(): InvoiceItemDto {
        return InvoiceItemDto(
            id = this.id,
            productId = this.productId?.id,
            quantity = this.quantity,
            totalAmount = this.totalAmount
        )
    }


}

