package com.currencycheck.domain.entity

data class FavoriteCurrencyInfo(
    var currencyFrom: String,
    var currencyTo: String,
    var currencyAmount: Double = 0.0
)