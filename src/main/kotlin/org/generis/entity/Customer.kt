package org.generis.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.enums.Currency
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "customers")
class Customer : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "customer_name")
    var name: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "phone_number")
    var phoneNumber: String? = null

    @Column(name = "country")
    var country: String? = null

    @Column(name = "city")
    var city: String? = null

    @Column(name = "tax_number")
    var taxNumber: String? = null

    @Column(name = "transaction_currency")
    @Enumerated(EnumType.STRING)
    var currency: Currency? = null

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null
}

