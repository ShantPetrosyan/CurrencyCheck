package com.currencycheck.domain.usecases

import com.currencycheck.domain.entity.CurrencyInfo
import com.currencycheck.domain.entity.CurrencyValues
import com.currencycheck.domain.repositories.CurrencyRepository
import com.currencycheck.domain.util.Resource

interface ICurrentCurrenciesUseCase {
    suspend fun getCurrentCurrencies(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyInfo>
}

class CurrentCurrenciesUseCaseImpl(
    private val repository: CurrencyRepository
) : ICurrentCurrenciesUseCase {
    override suspend fun getCurrentCurrencies(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyInfo> {
        return repository.getCurrencyData(currencies, mainCurrency)
    }
}