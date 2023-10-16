package com.currencycheck.data.source

import com.currencycheck.data.local.dao.FavoriteCurrenciesDao
import com.currencycheck.data.local.entity.FavoriteEntity
import com.currencycheck.data.local.sharedpref.PrefsManager
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    fun getAllFavorites(): Flow<List<FavoriteEntity>>?
    fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteEntity>>?
    fun getSelectedFilterPosition(): Int
    fun updateSelectedFilterPosition(position: Int): Int
    fun getSelectedMainCurrency(): String
    fun updateMainCurrency(currency: String)
    suspend fun insert(item: FavoriteEntity): Long
    suspend fun deleteByName(currencyFromName: String, currencyToName: String): Boolean
}

class LocalDataSourceImpl(
    private val dao: FavoriteCurrenciesDao,
    private val prefsManager: PrefsManager
) : ILocalDataSource {
    override fun getAllFavorites(): Flow<List<FavoriteEntity>>? = dao.getAllFavorites()

    override fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteEntity>>? =
        dao.getByNameFrom(currencyFrom)

    override fun getSelectedFilterPosition(): Int {
        return prefsManager.getSelectedFilterPosition()
    }

    override fun updateSelectedFilterPosition(position: Int): Int {
        return prefsManager.updateSelectedFilterPosition(position)
    }

    override fun getSelectedMainCurrency(): String {
        return prefsManager.getSelectedMainCurrency()
    }

    override fun updateMainCurrency(currency: String) {
        prefsManager.updateMainCurrency(currency)
    }

    override suspend fun insert(item: FavoriteEntity): Long = dao.insert(item)

    override suspend fun deleteByName(currencyFromName: String, currencyToName: String): Boolean =
        dao.deleteByName(currencyFromName, currencyToName) > 0
}