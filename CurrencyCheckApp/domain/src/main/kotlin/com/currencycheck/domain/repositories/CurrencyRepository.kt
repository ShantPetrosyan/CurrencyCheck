package com.currencycheck.domain.repositories

import com.currencycheck.domain.entity.CurrencyInfo
import com.currencycheck.domain.entity.CurrencyValues
import com.currencycheck.domain.util.Resource

interface CurrencyRepository {
    suspend fun getCurrencyData(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyInfo>
}