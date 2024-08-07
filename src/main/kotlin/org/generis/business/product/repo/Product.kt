package org.generis.business.product.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.generis.business.product.enums.ProductState
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime


@Entity
@Table(name = "products")
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
    @Enumerated(value = EnumType.STRING)
    var productState: ProductState? = null

    @CreationTimestamp
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null
}

