package com.currencycheck.presentation.ui.main

import androidx.compose.runtime.Immutable
import com.currencycheck.presentation.base.UiEvent
import com.currencycheck.presentation.ui.main.data.MainViewModelData

@Immutable
sealed class CurrencyUIEvent : UiEvent {
    data class ShowData(val currenciesViewModelData: MainViewModelData?) : CurrencyUIEvent()
    data class ShowError(val errorMessage: String, val errorCode: Int) : CurrencyUIEvent()
    data class ShowLoading(val toShow: Boolean) : CurrencyUIEvent()
}