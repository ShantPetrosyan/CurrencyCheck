package com.currencycheck.presentation.ui.favorites

import com.currencycheck.domain.entity.FavoriteCurrencyInfo
import com.currencycheck.presentation.ui.favorites.data.FavoritesViewModelData

object FavoritesScreenViewDataMapper {
    fun toModel(favoriteCurrencyInfo: FavoriteCurrencyInfo): FavoritesViewModelData {
        favoriteCurrencyInfo.apply {
            return FavoritesViewModelData(
                currencyFrom = this.currencyFrom,
                currencyTo = this.currencyTo,
                currencyAmount = this.currencyAmount
            )
        }
    }

    fun toModel(favoriteCurrencyInfo: List<FavoriteCurrencyInfo>): List<FavoritesViewModelData> {
        return favoriteCurrencyInfo.map {
            toModel(it)
        }
    }
}