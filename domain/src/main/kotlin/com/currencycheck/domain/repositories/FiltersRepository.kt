package com.currencycheck.domain.repositories

interface FiltersRepository {
    fun getSelectedFilterPosition(): Int
    fun updateSelectedFilterPosition(position: Int): Int
    fun getSelectedMainCurrency(): String
    fun updateMainCurrency(currency: String)
}