package com.currencycheck.data.remote

import com.currencycheck.data.remote.dto.CurrencyResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/exchangerates_data/latest")
    suspend fun getCurrencyData(
        @Header("apikey") apiKey: String,
        @Query("symbols") symbols: String,
        @Query("base") baseCurrency: String
    ): CurrencyResponseDto
}