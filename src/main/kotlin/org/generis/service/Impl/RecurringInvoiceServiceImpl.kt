package org.generis.service.Impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.dto.*
import org.generis.entity.*
import org.generis.exception.ServiceException
import org.generis.service.RecurringInvoiceService
import org.generis.service.SubscriptionService
import org.generis.util.JacksonUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Singleton
@Transactional
class RecurringInvoiceServiceImpl: RecurringInvoiceService {

    @Inject
    var entityManager: EntityManager? = null

    override fun create(createRecurringInvoiceDto: CreateRecurringInvoiceDto): RecurringInvoice {
        // Retrieve the customer based on the customerId from the database
        val customer = entityManager?.find(Customer::class.java, createRecurringInvoiceDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        val subscription = entityManager?.find(Subscription::class.java, createRecurringInvoiceDto.subscription)
            ?: throw IllegalArgumentException("Invalid subscription Id")

        // Create the Invoice instance
        val invoice = RecurringInvoice()
        invoice.invoiceNumber = invoice.generateInvoiceNumber()
        invoice.title = createRecurringInvoiceDto.title
        invoice.subHeading = createRecurringInvoiceDto.subHeading
        invoice.dueDate =
            LocalDate.parse(createRecurringInvoiceDto.dueDate, DateTimeFormatter.ofPattern(JacksonUtils.datePattern))
        invoice.createdDate = LocalDateTime.now()
        invoice.customerId = customer
        invoice.subscription = subscription
        invoice.tax = createRecurringInvoiceDto.tax
        invoice.discount = createRecurringInvoiceDto.discount
        invoice.subTotal = 0.00

        var totalAmount = 0.00
        for (item in subscription.items) {
            totalAmount += item.totalAmount ?: 0.00
        }
        invoice.totalAmount = totalAmount

        // Persist the invoice in the database
        entityManager?.persist(invoice)

        return invoice

    }

    override fun get(id: String): RecurringInvoiceDto? {
        val recurringInvoice = entityManager?.find(RecurringInvoice::class.java, id)
        return recurringInvoice?.toDto()

    }

    override fun getAllIds(): List<String>? {
        val invoices = entityManager?.createQuery("SELECT r FROM RecurringInvoice r", RecurringInvoice::class.java)
            ?.resultList
        return invoices?.map { it.id!! }
    }

    override fun getAll(): List<RecurringInvoiceDto> {
        val recurringInvoices = entityManager?.createQuery("SELECT r FROM RecurringInvoice r", RecurringInvoice::class.java)?.resultList
        return recurringInvoices?.map { it.toDto() } ?: throw ServiceException(-1, "No recurring invoices found")
    }

    private fun RecurringInvoice.toDto(): RecurringInvoiceDto {
        return RecurringInvoiceDto(
            id = this.id,
            title = this.title,
            subHeading = this.subHeading,
            customerId = this.customerId?.id,
            invoiceNumber = this.invoiceNumber,
            subscription = this.subscription,
            status = this.status,
            createdDate = this.createdDate,
            dueDate = this.dueDate,
            recurringPeriod = this.recurringPeriod,
            tax = this.tax,
            discount = this.discount,
            subTotal = this.subTotal,
            totalAmount = this.totalAmount
        )
    }
}
