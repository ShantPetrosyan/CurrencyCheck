package com.currencycheck.data.local.dao

import androidx.room.*
import com.currencycheck.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoriteCurrenciesDao {
    @Query("SELECT * FROM favorites WHERE currency_from LIKE :currencyFromName AND currency_to LIKE :currencyToName")
    abstract fun getByName(
        currencyFromName: String,
        currencyToName: String
    ): Flow<FavoriteEntity>?

    @Query("SELECT * FROM favorites")
    abstract fun getAllFavorites(): Flow<List<FavoriteEntity>>?

    @Query("SELECT * FROM favorites WHERE currency_from LIKE :currencyFromName")
    abstract fun getByNameFrom(currencyFromName: String): Flow<List<FavoriteEntity>>?

    @Query("DELETE FROM favorites")
    abstract suspend fun deleteAllFavorites(): Int

    @Query("DELETE FROM favorites WHERE currency_from LIKE :currencyFromName AND currency_to LIKE :currencyToName")
    abstract suspend fun deleteByName(currencyFromName: String, currencyToName: String): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(item: FavoriteEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: FavoriteEntity): Long

    @Delete
    abstract suspend fun delete(item: FavoriteEntity): Int
}