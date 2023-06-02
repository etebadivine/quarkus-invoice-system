package org.generis.enums

enum class Currency(val displayName: String, val exchangeRate: Double) {
    GHANA("Ghana Currency", 1.0),
    USD("American Dollar", 0.18),
    KENYA("Kenyan Shilling", 19.38),
    ZAMBIA("Zambian Kwacha", 20.17),
    COTEDIVOIRE("Côte d'Ivoire Franc", 99.47)
}