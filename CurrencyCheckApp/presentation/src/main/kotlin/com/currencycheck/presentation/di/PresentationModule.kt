package com.currencycheck.presentation.di

import com.currencycheck.domain.logger.CurrencyLogger
import com.currencycheck.presentation.logger.LoggerImpl
import com.currencycheck.presentation.ui.favorites.FavoritesViewModel
import com.currencycheck.presentation.ui.filter.FilterScreenViewDataMapper
import com.currencycheck.presentation.ui.filter.FilterViewModel
import com.currencycheck.presentation.ui.main.MainCurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single<CurrencyLogger> { LoggerImpl() }
    single { FilterScreenViewDataMapper(get()) }

    viewModel { MainCurrencyViewModel(get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { FilterViewModel(get(), get()) }
}