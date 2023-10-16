package com.currencycheck.presentation.di

import android.content.Context
import com.currencycheck.domain.logger.CurrencyLogger
import com.currencycheck.presentation.logger.LoggerImpl
import com.currencycheck.presentation.ui.filter.FilterScreenViewDataMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltDataModule {
    @Provides
    fun providesLoggerImpl(): CurrencyLogger {
        return LoggerImpl()
    }

    @Provides
    fun providesFilterScreenViewDataMapper(@ApplicationContext context: Context): FilterScreenViewDataMapper {
        return FilterScreenViewDataMapper(context)
    }
}