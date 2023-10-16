package com.currencycheck.app.di

import com.currencycheck.domain.repositories.CurrencyRepository
import com.currencycheck.domain.repositories.FavoritesRepository
import com.currencycheck.domain.repositories.FiltersRepository
import com.currencycheck.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun providesCurrentCurrenciesUseCaseImpl(repository: CurrencyRepository): ICurrentCurrenciesUseCase {
        return CurrentCurrenciesUseCaseImpl(repository)
    }

    @Provides
    fun providesFavoritesUseCaseImpl(repository: FavoritesRepository): IFavoritesUseCase {
        return FavoritesUseCaseImpl(repository)
    }

    @Provides
    fun providesFilterUseCaseImpl(repository: FiltersRepository): IFilterUseCase {
        return FilterUseCaseImpl(repository)
    }
}