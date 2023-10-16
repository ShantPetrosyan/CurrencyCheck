package com.currencycheck.presentation.ui.favorites

import androidx.compose.runtime.Immutable
import com.currencycheck.presentation.base.UiEvent
import com.currencycheck.presentation.ui.favorites.data.FavoritesViewModelData

@Immutable
sealed class FavoritesUIEvent : UiEvent {
    data class ShowData(val favoritesViewModelDataList: List<FavoritesViewModelData>) : FavoritesUIEvent()
    data class ShowError(val errorMessage: String) : FavoritesUIEvent()
    data class ShowMainLoading(val toShow: Boolean) : FavoritesUIEvent()
    data class ShowItemLoading(val toShow: Boolean) : FavoritesUIEvent()
}