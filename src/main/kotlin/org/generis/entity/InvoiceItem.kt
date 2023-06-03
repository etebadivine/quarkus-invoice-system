package org.generis.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import kotlinx.serialization.Serializable
import lombok.*
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateTimeSerializer
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@ToString
@Serializable
class InvoiceItem : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var productId: Product? = null

    @Column(name = "quantity")
    var quantity: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null

    @Column(name = "total_amount")
    var totalAmount: Double? = null

    @Column(name = "created_date")
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    var createdDate: LocalDateTime = LocalDateTime.now()

 }