package com.currencycheck.app.di

import android.content.Context
import android.content.SharedPreferences
import com.currencycheck.data.local.dao.FavoriteCurrenciesDao
import com.currencycheck.data.local.db.CurrencyDataBase
import com.currencycheck.data.local.sharedpref.PrefsManager
import com.currencycheck.data.remote.CurrencyApi
import com.currencycheck.data.repositoryImpl.CurrencyRepositoryImpl
import com.currencycheck.data.repositoryImpl.FavoritesRepositoryImpl
import com.currencycheck.data.repositoryImpl.FiltersRepositoryImpl
import com.currencycheck.data.source.ILocalDataSource
import com.currencycheck.data.source.IRemoteDataSource
import com.currencycheck.data.source.LocalDataSourceImpl
import com.currencycheck.data.source.RemoteDataSourceImpl
import com.currencycheck.domain.helpers.BASE_URL
import com.currencycheck.domain.repositories.CurrencyRepository
import com.currencycheck.domain.repositories.FavoritesRepository
import com.currencycheck.domain.repositories.FiltersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun providesCurrencyApi(): CurrencyApi {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create()
    }

    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PrefsManager.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun providesPrefsManager(sharedPref: SharedPreferences): PrefsManager {
        return PrefsManager(sharedPref)
    }

    @Provides
    fun providesRemoteDataSource(api: CurrencyApi): IRemoteDataSource {
        return RemoteDataSourceImpl(api)
    }

    @Provides
    fun providesCurrencyDataBase(@ApplicationContext context: Context): CurrencyDataBase {
        return CurrencyDataBase.create(context)
    }

    @Provides
    fun providesFavoriteCurrenciesDao(currencyDb: CurrencyDataBase): FavoriteCurrenciesDao {
        return currencyDb.favoritesDao()
    }

    @Provides
    fun providesLocalDataSourceImpl(
        favoriteCurrenciesDao: FavoriteCurrenciesDao,
        prefsManager: PrefsManager
    ): ILocalDataSource {
        return LocalDataSourceImpl(favoriteCurrenciesDao, prefsManager)
    }

    @Provides
    fun providesCurrencyRepository(
        remoteDataStore: IRemoteDataSource
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(remoteDataStore)
    }

    @Provides
    fun providesFavoritesRepository(
        localDataSource: ILocalDataSource
    ): FavoritesRepository {
        return FavoritesRepositoryImpl(localDataSource)
    }

    @Provides
    fun bindsFiltersRepository(
        localDataSource: ILocalDataSource
    ): FiltersRepository {
        return FiltersRepositoryImpl(localDataSource)
    }
}