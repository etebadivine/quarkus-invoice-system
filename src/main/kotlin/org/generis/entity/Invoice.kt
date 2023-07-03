package org.generis.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.enums.InvoiceStatus
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
@Table(name = "invoices")
class Invoice : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "invoice_title")
    var title: String? = null

    @Column(name = "sub_heading")
    var subHeading: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    var customerId: Customer? = null

    @Column(name = "invoice_number")
    var invoiceNumber: String? = null

    @JoinColumn(name = "transaction_currency")
    @ManyToOne(fetch = FetchType.EAGER)
    var currency: Currency? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    val items: MutableList<InvoiceItem> = mutableListOf()

    @Column(name = "invoice_status")
    @Enumerated(EnumType.STRING)
    var status: InvoiceStatus = InvoiceStatus.CREATED

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null

    @Column(name = "due_date")
    var dueDate: LocalDate? = null

    @Column(name = "tax")
    var tax: Double? = 0.00

    @Column(name = "discount")
    var discount: Double? = 0.00

    @Column(name = "sub_total")
    var subTotal: Double? = 0.00

    @Column(name = "total_amount")
    var totalAmount: Double? =  0.00

    fun generateInvoiceNumber(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        return "INV-${currentDateTime.format(formatter)}"

    }
}


