package org.generis.entity

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.*
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import org.generis.enums.Currency
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateTimeSerializer
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
@Serializable
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

    @Column(name = "created_date")
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    var createdDate: LocalDateTime = LocalDateTime.now()
}

