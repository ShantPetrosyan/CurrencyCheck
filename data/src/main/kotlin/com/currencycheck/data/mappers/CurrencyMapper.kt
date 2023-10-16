package com.currencycheck.data.mappers

import com.currencycheck.data.local.entity.FavoriteEntity
import com.currencycheck.data.remote.dto.CurrencyResponseDto
import com.currencycheck.domain.entity.CurrencyInfo
import com.currencycheck.domain.entity.FavoriteCurrencyInfo

fun CurrencyResponseDto.toCurrencyInfo(): CurrencyInfo {
    return CurrencyInfo(
        baseCurrency = baseCurrency,
        date = date,
        rates = rates
    )
}

fun List<FavoriteEntity>.toFavoritesList() = map {
    it.toFavoriteCurrencyInfo()
}

fun FavoriteEntity.toFavoriteCurrencyInfo() = FavoriteCurrencyInfo(
    currencyFrom = this.currencyFromName,
    currencyTo = this.currencyToName
)