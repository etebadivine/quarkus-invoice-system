package org.generis.business.invoice.repo

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.business.invoice.repo.Invoice
import org.generis.business.product.repo.Product
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "invoice_items")
class InvoiceItem : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @ManyToOne(fetch = FetchType.LAZY,cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "product_id")
    var productId: Product? = null

    @Column(name = "quantity")
    var quantity: Int? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    var invoice: Invoice? = null

    @Column(name = "total_amount")
    var totalAmount: Double? = null

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null

}