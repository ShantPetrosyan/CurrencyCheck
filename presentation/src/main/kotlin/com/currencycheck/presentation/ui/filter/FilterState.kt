package com.currencycheck.presentation.ui.filter

import com.currencycheck.presentation.base.UiState
import com.currencycheck.presentation.ui.filter.data.FilterViewModelData

data class FilterState(
    val filterViewModelDataList: List<FilterViewModelData> = listOf(),
    val filterApplied: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState {
    companion object {
        fun initial() = FilterState(
            isLoading = true
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, filterViewModelDataList: ${filterViewModelDataList}, is error: ${error != null}"
    }
}