package com.currencycheck.presentation.ui.main

import com.currencycheck.domain.entity.CurrencyInfo
import com.currencycheck.domain.entity.FavoriteCurrencyInfo
import com.currencycheck.presentation.ui.helper.CurrencyHelper.sortRates
import com.currencycheck.presentation.ui.main.data.MainViewModelData
import com.currencycheck.presentation.ui.main.data.RateInfo

object MainScreenViewDataMapper {
    fun toModel(
        currencyInfo: CurrencyInfo?,
        favorites: List<FavoriteCurrencyInfo> = emptyList(),
        selectedFilterType: Int = 0
    ): MainViewModelData? {
        currencyInfo?.apply {
            val rates = sortRates(this.rates, selectedFilterType)
            val updatedViaFavorite = addFavorites(currencyInfo.baseCurrency, rates, favorites)
            return MainViewModelData(
                baseCurrency = this.baseCurrency,
                date = this.date,
                rates = updatedViaFavorite
            )
        }

        return null
    }

    private fun addFavorites(
        baseCurrency: String,
        rates: Map<String, Double>,
        favorites: List<FavoriteCurrencyInfo>
    ): List<RateInfo> {
        val result = mutableListOf<RateInfo>()
        rates.map { rateItem ->
            val item = RateInfo(rateItem.key, rateItem.value)
            if (favorites.firstOrNull { it.currencyFrom == baseCurrency && it.currencyTo == rateItem.key } != null) {
                item.isFavorite = true
            }
            result.add(item)
        }

        return result
    }
}