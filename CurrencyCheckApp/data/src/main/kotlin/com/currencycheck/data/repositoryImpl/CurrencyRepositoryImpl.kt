package com.currencycheck.data.repositoryImpl

import com.currencycheck.data.mappers.toCurrencyInfo
import com.currencycheck.data.source.IRemoteDataSource
import com.currencycheck.data.source.RemoteDataSourceImpl
import com.currencycheck.domain.entity.CurrencyInfo
import com.currencycheck.domain.repositories.CurrencyRepository
import com.currencycheck.domain.util.Resource

class CurrencyRepositoryImpl(
    private val currencyDataSource: IRemoteDataSource
) : CurrencyRepository {
    override suspend fun getCurrencyData(
        currencies: String,
        mainCurrency: String
    ): Resource<CurrencyInfo> {
        val result = currencyDataSource.getCurrentCurrencies(currencies, mainCurrency)

        return when (result) {
            is Resource.Success -> {
                result.data?.let {
                    Resource.Success(data = it.toCurrencyInfo())
                }?: run {
                    Resource.Error(
                        message = "Connection Error",
                        errorCode = 0
                    )
                }
            }
            is Resource.Error -> {
                Resource.Error(
                    message = result.message ?: "Unknown error",
                    errorCode = result.errorCode
                )
            }
        }
    }
}