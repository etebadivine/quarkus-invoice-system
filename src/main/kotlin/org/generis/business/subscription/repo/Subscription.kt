package org.generis.business.subscription.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.business.customer.repo.Customer
import org.generis.business.subscription.enums.SubscriptionState
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
@Table(name = "subscriptions")
class Subscription : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "subscription_number")
    var subscriptionNumber: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    var customerId: Customer? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subscription", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    var items: MutableList<SubscriptionItem> = mutableListOf()

    @Column(name = "start_date")
    var startDate: LocalDate? = null

    @Column(name = "end_date")
    var endDate: LocalDate? = null

    @Column(name = "recurring_period")
    var recurringPeriod: Long? = null

    @Column(name = "next_invoiceDate")
    var nextInvoiceDate: LocalDate? = null

    @Column(name = "subscription_status")
    @Enumerated(EnumType.STRING)
    var status: SubscriptionState = SubscriptionState.ACTIVE

    @Column(name = "total_amount")
    var totalAmount: Double? =  0.00

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null

    fun generateSubscriptionNumber(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        return "SUB-${currentDateTime.format(formatter)}"

    }
}