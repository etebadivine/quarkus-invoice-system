package org.generis.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import lombok.Getter
import lombok.Setter
import lombok.ToString
import org.generis.enums.Currency
import org.generis.enums.InvoiceStatus
import org.generis.enums.RecurringPeriod
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateSerializer
import org.generis.util.LocalDateTimeSerializer
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
@Table(name = "recurring_invoices")
@Getter
@Setter
@ToString
@Serializable
class RecurringInvoice : PanacheEntityBase() {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "invoice_number")
    var invoiceNumber: String? = null

    @Column(name = "invoice_title")
    var title: String? = null

    @Column(name = "sub_heading")
    var subHeading: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customerId: Customer? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    var subscription: Subscription? = null

    @Column(name = "due_date")
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    var dueDate: LocalDate? = null

    @Column(name = "created_date")
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    var createdDate: LocalDateTime? = LocalDateTime.now()

    @Column(name = "tax")
    var tax: Double? = 0.00

    @Column(name = "discount")
    var discount: Double? = 0.00

    @Column(name = "status")
    var status: InvoiceStatus? = InvoiceStatus.CREATED

    @Column(name = "sub_total")
    var subTotal: Double? = null

    @Column(name = "total_amount")
    var totalAmount: Double? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_period")
    var recurringPeriod: RecurringPeriod? = null

    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    @Column(name = "last_generated_date")
    var lastGeneratedDate: LocalDate? = null


    fun generateInvoiceNumber(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        return "INV-${currentDateTime.format(formatter)}"

    }
}