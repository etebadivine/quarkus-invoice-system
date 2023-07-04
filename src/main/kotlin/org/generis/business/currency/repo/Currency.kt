package org.generis.business.currency.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "currencies")
class Currency  : PanacheEntityBase() {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "currency_name")
    var currencyName: String? = null

    @Column(name = "exchange_rate")
    var exchangeRate: Double? = null

    @Column(name = "country")
    var country: String? = null

    @Column(name = "currency_code")
    var currencyCode: String? = null
}