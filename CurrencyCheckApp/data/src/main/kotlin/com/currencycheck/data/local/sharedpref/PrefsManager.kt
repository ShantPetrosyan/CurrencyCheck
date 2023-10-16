package com.currencycheck.data.local.sharedpref

import android.content.SharedPreferences

class PrefsManager(private val sharedPref: SharedPreferences) {

    fun updateSelectedFilterPosition(position: Int): Int {
        sharedPref.edit().putInt(FILTER_POSITION, position).apply()
        return position
    }

    fun getSelectedFilterPosition(): Int {
        return sharedPref.getInt(FILTER_POSITION, 0)
    }

    fun getSelectedMainCurrency(): String {
        return sharedPref.getString(MAIN_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }

    fun updateMainCurrency(currency: String) {
        sharedPref.edit().putString(MAIN_CURRENCY, currency).apply()
    }

    companion object {
        const val PREF_NAME = "FILTER_SETTINGS"
        private const val FILTER_POSITION = "filter_position"
        private const val MAIN_CURRENCY = "main_currency"
        private const val DEFAULT_CURRENCY = "EUR"
    }
}