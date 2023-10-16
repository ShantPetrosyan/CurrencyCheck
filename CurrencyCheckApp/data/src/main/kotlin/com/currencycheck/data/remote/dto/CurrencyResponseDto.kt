package com.currencycheck.data.remote.dto

import com.squareup.moshi.Json

data class CurrencyResponseDto(
    @field:Json(name = "base")
    val baseCurrency: String,
    val date: String,
    val rates: Map<String, Double>
)
