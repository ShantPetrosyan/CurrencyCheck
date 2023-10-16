package com.currencycheck.domain.repositories

import com.currencycheck.domain.entity.FavoriteCurrencyInfo
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCurrencies(): Flow<List<FavoriteCurrencyInfo>>?
    fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteCurrencyInfo>>?
    suspend fun updateFavoriteStatus(isActive: Boolean, currencyFrom: String, currencyTo: String): Boolean
}