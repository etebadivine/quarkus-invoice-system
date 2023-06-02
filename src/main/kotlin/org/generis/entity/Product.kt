package org.generis.entity

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.*
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import org.generis.enums.ProductState
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateTimeSerializer
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@Serializable
class Product : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "unit_price")
    var unitPrice: Double? = null

    @Column(name = "product_name")
    var productName: String? = null

    @Column(name = "description")
    var description: String? = null

    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    var isRecurring: ProductState? = null

    @Column(name = "recurring_period")
    var recurringPeriod: Long? = null

    @Column(name = "created_date")
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    var createdDate: LocalDateTime = LocalDateTime.now()
}

