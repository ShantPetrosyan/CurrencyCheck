package com.currencycheck.domain.usecases

import com.currencycheck.domain.entity.FavoriteCurrencyInfo
import com.currencycheck.domain.repositories.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IFavoritesUseCase {
    fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteCurrencyInfo>>
    fun getFavorites(): Flow<List<FavoriteCurrencyInfo>>
    suspend fun updateFavoriteStatus(isActive: Boolean, currencyFrom: String, currencyTo: String): Boolean
}

class FavoritesUseCaseImpl(
    private val repository: FavoritesRepository
) : IFavoritesUseCase {
    override fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteCurrencyInfo>> {
        return repository.getFavoritesFor(currencyFrom) ?: flowOf(emptyList())
    }

    override fun getFavorites(): Flow<List<FavoriteCurrencyInfo>> {
        return repository.getFavoriteCurrencies() ?: flowOf(emptyList())
    }

    override suspend fun updateFavoriteStatus(
        isActive: Boolean,
        currencyFrom: String,
        currencyTo: String
    ): Boolean {
        return repository.updateFavoriteStatus(isActive, currencyFrom, currencyTo)
    }
}