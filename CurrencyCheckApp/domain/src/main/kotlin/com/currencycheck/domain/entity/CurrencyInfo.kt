package com.currencycheck.domain.entity

data class CurrencyInfo(
    val baseCurrency: String,
    val date: String,
    val rates: Map<String, Double>
)

enum class CurrencyValues {
    EUR,
    AED,
    USD,
    BYN,
    RUB,
    JPY
}