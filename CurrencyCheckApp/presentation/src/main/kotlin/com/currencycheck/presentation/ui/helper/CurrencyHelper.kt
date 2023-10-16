package com.currencycheck.presentation.ui.helper

import android.content.Context
import com.currencycheck.domain.entity.CurrencyValues
import com.currencycheck.presentation.R

object CurrencyHelper {
    fun getErrorMessage(context: Context, errorCode: Int, errorMessage: String): Any {
        return when (errorCode) {
            400 -> {
                context.getString(R.string.bad_request)
            }
            401 -> {
                context.getString(R.string.unauthorized)
            }
            404 -> {
                context.getString(R.string.not_found)
            }
            429 -> {
                context.getString(R.string.too_many_requests)
            }
            in 500..599 -> {
                context.getString(R.string.bad_request)
            }
            0 -> {
                errorMessage.ifEmpty { context.getString(R.string.unknownError) }
            }
            else -> {
                context.getString(R.string.unknownError)
            }
        }
    }

    fun getInitialCurrencies(mainCurrency: String): String {
        return CurrencyValues.values().filter { it.name != mainCurrency }.joinToString(",")
    }

    fun getAvailableCurrencies(): List<String> {
        return mutableListOf<String>().apply {
            add(CurrencyValues.EUR.name)
            add(CurrencyValues.USD.name)
            add(CurrencyValues.AED.name)
            add(CurrencyValues.BYN.name)
            add(CurrencyValues.RUB.name)
            add(CurrencyValues.JPY.name)
        }
    }

    fun sortRates(rates: Map<String, Double>, filterType: Int): Map<String, Double> {
        return when (filterType) {
            FilterType.START_TO_END_ALPHABET.ordinal -> {
                rates.toList().sortedBy { (key, _) -> key }.toMap()
            }
            FilterType.END_TO_START_ALPHABET.ordinal -> {
                rates.toList().sortedByDescending { (key, _) -> key }.toMap()
            }
            FilterType.ASCENDING.ordinal -> {
                rates.toList().sortedBy { (_, value) -> value }.toMap()
            }
            FilterType.DESCENDING.ordinal -> {
                rates.toList().sortedByDescending { (_, value) -> value }.toMap()
            }
            else -> {
                rates.toList().sortedBy { (key, _) -> key }.toMap()
            }
        }

    }
}

enum class FilterType {
    START_TO_END_ALPHABET,
    END_TO_START_ALPHABET,
    ASCENDING,
    DESCENDING
}