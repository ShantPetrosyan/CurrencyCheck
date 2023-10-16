package com.currencycheck.presentation.ui.main

import com.currencycheck.presentation.base.UiState
import com.currencycheck.presentation.ui.main.data.MainViewModelData

data class CurrencyState(
    val currencyModelData: MainViewModelData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val errorCode: Int = -1
) : UiState {
    companion object {
        fun initial() = CurrencyState(
            isLoading = true
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, currencyModelData: ${currencyModelData}, is error: ${error != null}"
    }
}