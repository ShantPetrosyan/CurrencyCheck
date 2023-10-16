package com.currencycheck.data.repositoryImpl

import com.currencycheck.data.local.entity.FavoriteEntity
import com.currencycheck.data.mappers.toFavoritesList
import com.currencycheck.data.source.LocalDataSource
import com.currencycheck.domain.entity.FavoriteCurrencyInfo
import com.currencycheck.domain.repositories.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val localDataSource: LocalDataSource
) : FavoritesRepository {
    override fun getFavoriteCurrencies(): Flow<List<FavoriteCurrencyInfo>>? {
        return localDataSource.getAllFavorites()?.map { it.toFavoritesList() }
    }

    override fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteCurrencyInfo>>? {
        return localDataSource.getFavoritesFor(currencyFrom)?.map { it.toFavoritesList() }
    }

    override suspend fun updateFavoriteStatus(
        isActive: Boolean,
        currencyFrom: String,
        currencyTo: String
    ): Boolean {
        return if (isActive) {
            localDataSource.insert(
                FavoriteEntity(
                    currencyFromName = currencyFrom,
                    currencyToName = currencyTo
                )
            ) >= 0
        } else {
            localDataSource.deleteByName(currencyFrom, currencyTo)
        }
    }
}