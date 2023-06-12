package org.generis.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import lombok.*
import org.generis.enums.SubscriptionFrequency
import org.generis.util.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate


@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@ToString
@Serializable
class Subscription : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    var customerId: Customer? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subscription", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    var items: MutableList<SubscriptionItem> = mutableListOf()

    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "start_date")
    var startDate: LocalDate? = null

    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    @Column(name = "end_date")
    var endDate: LocalDate? = null

    @Column(name = "subscription_frequency")
    var frequency: Long? = null

    @Column(name = "total_amount")
    var totalAmount: Double? = null
}