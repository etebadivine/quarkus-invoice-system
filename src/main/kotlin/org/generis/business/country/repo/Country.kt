package org.generis.business.country.repo

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "countries")
class Country : PanacheEntityBase() {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    var id: String? = null

    @Column(name = "country_name")
    var countryName: String? = null

    @Column(name = "country_currency")
    var currency: String? = null

    @Column(name = "currency_code")
    var currencyCode: String? = null

    @Column(name = "exchange_rate")
    var exchangeRate: Double? = null
}