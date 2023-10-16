package com.currencycheck.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.currencycheck.data.local.dao.FavoriteCurrenciesDao
import com.currencycheck.data.local.entity.FavoriteEntity

@Database(
    version = 1,
    entities = [FavoriteEntity::class],
    exportSchema = false
)
abstract class CurrencyDataBase : RoomDatabase() {

    abstract fun favoritesDao(): FavoriteCurrenciesDao

    companion object {
        private const val DATABASE_NAME = "currency_info.db"

        fun create(context: Context): CurrencyDataBase {
            return Room.databaseBuilder(
                context,
                CurrencyDataBase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}