package com.currencycheck.domain.di

import com.currencycheck.domain.usecases.*
import org.koin.dsl.module

private val useCasesModule = module {
    single<ICurrentCurrenciesUseCase> { CurrentCurrenciesUseCaseImpl(get()) }
    single<IFavoritesUseCase> { FavoritesUseCaseImpl(get()) }
    single<IFilterUseCase> { FilterUseCaseImpl(get()) }
}

val domainModule = listOf(useCasesModule)