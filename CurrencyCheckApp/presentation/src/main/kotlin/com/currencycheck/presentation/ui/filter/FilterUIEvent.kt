package com.currencycheck.presentation.ui.filter

import androidx.compose.runtime.Immutable
import com.currencycheck.presentation.base.UiEvent
import com.currencycheck.presentation.ui.filter.data.FilterViewModelData

@Immutable
sealed class FilterUIEvent : UiEvent {
    data class ShowData(val filterViewModelDataList: List<FilterViewModelData>) : FilterUIEvent()
    data class ShowError(val errorMessage: String) : FilterUIEvent()
    object CompleteSavingFilters: FilterUIEvent()
}