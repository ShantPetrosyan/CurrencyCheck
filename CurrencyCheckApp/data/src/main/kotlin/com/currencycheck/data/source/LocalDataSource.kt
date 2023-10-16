package com.currencycheck.data.source

import com.currencycheck.data.local.dao.FavoriteCurrenciesDao
import com.currencycheck.data.local.entity.FavoriteEntity
import com.currencycheck.data.local.sharedpref.PrefsManager
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val dao: FavoriteCurrenciesDao,
    private val prefsManager: PrefsManager
) {
    fun getAllFavorites(): Flow<List<FavoriteEntity>>? = dao.getAllFavorites()

    fun getFavoritesFor(currencyFrom: String): Flow<List<FavoriteEntity>>? =
        dao.getByNameFrom(currencyFrom)

    fun getSelectedFilterPosition(): Int {
        return prefsManager.getSelectedFilterPosition()
    }

    fun updateSelectedFilterPosition(position: Int): Int {
        return prefsManager.updateSelectedFilterPosition(position)
    }

    fun getSelectedMainCurrency(): String {
        return prefsManager.getSelectedMainCurrency()
    }

    fun updateMainCurrency(currency: String) {
        prefsManager.updateMainCurrency(currency)
    }

    suspend fun insert(item: FavoriteEntity): Long = dao.insert(item)

    suspend fun deleteByName(currencyFromName: String, currencyToName: String): Boolean =
        dao.deleteByName(currencyFromName, currencyToName) > 0
}