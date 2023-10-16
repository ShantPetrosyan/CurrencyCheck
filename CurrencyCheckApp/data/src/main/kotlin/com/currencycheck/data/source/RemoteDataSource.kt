package com.currencycheck.data.source

import com.currencycheck.data.remote.CurrencyApi
import com.currencycheck.data.remote.dto.CurrencyResponseDto
import com.currencycheck.domain.entity.CurrencyValues
import com.currencycheck.domain.helpers.API_KEY
import com.currencycheck.domain.util.Resource
import retrofit2.HttpException

interface DataSource {
    suspend fun getCurrentCurrencies(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyResponseDto>
}

class RemoteDataSource(private val api: CurrencyApi) : DataSource {
    override suspend fun getCurrentCurrencies(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyResponseDto> {
        return try {
            Resource.Success(
                data = api.getCurrencyData(
                    apiKey = API_KEY,
                    symbols = currencies,
                    baseCurrency = mainCurrency
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.", errorCode = if (e is HttpException) e.code() else 0)
        }
    }
}