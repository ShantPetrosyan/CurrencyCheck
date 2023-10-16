package com.currencycheck.presentation.ui.main.data

data class MainViewModelData(
    val baseCurrency: String,
    val date: String,
    val rates: List<RateInfo>
)

data class RateInfo(
    val currency: String,
    val rate: Double,
    var isFavorite: Boolean = false
)
