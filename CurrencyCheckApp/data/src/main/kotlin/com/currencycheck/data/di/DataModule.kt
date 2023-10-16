package com.currencycheck.data.di

import android.content.Context
import com.currencycheck.data.local.db.CurrencyDataBase
import com.currencycheck.data.local.sharedpref.PrefsManager
import com.currencycheck.data.local.sharedpref.PrefsManager.Companion.PREF_NAME
import com.currencycheck.data.remote.CurrencyApi
import com.currencycheck.data.repositoryImpl.CurrencyRepositoryImpl
import com.currencycheck.data.repositoryImpl.FavoritesRepositoryImpl
import com.currencycheck.data.repositoryImpl.FiltersRepositoryImpl
import com.currencycheck.data.source.LocalDataSource
import com.currencycheck.data.source.RemoteDataSource
import com.currencycheck.domain.helpers.BASE_URL
import com.currencycheck.domain.repositories.CurrencyRepository
import com.currencycheck.domain.repositories.FavoritesRepository
import com.currencycheck.domain.repositories.FiltersRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

private val sharedPrefsModule = module {
    single { get<Context>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }
    single { PrefsManager(get()) }
}

private val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { LocalDataSource(get(), get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<FiltersRepository> { FiltersRepositoryImpl(get()) }

    single<CurrencyApi> {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create()
    }
}

private val databaseModule = module {
    single {
        CurrencyDataBase.create(get())
    }
    single { get<CurrencyDataBase>().favoritesDao() }
}

val dataModules = listOf(databaseModule, sharedPrefsModule, repositoryModule)