package com.currencycheck.presentation.ui.favorites

import com.currencycheck.presentation.base.UiState
import com.currencycheck.presentation.ui.favorites.data.FavoritesViewModelData

data class FavoritesState(
    val favoritesViewModelDataList: List<FavoritesViewModelData> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState {
    companion object {
        fun initial() = FavoritesState(
            isLoading = true
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, favoritesViewModelDataList: ${favoritesViewModelDataList}, is error: ${error != null}"
    }
}