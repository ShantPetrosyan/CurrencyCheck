package com.currencycheck.presentation.ui.favorites

import androidx.lifecycle.viewModelScope
import com.currencycheck.domain.usecases.ICurrentCurrenciesUseCase
import com.currencycheck.domain.usecases.IFavoritesUseCase
import com.currencycheck.domain.util.Resource
import com.currencycheck.presentation.base.BaseViewModel
import com.currencycheck.presentation.base.Reducer
import com.currencycheck.presentation.ui.main.MainScreenViewDataMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesUseCase: IFavoritesUseCase,
    private val currenciesUseCase: ICurrentCurrenciesUseCase
) : BaseViewModel<FavoritesState, FavoritesUIEvent>() {

    private val reducer = FavoritesReducer(FavoritesState.initial())

    override val state: StateFlow<FavoritesState>
        get() = reducer.state

    init {
        loadFavoritesInfo()
    }

    private fun loadFavoritesInfo() {
        viewModelScope.launch {
            sendEvent(FavoritesUIEvent.ShowMainLoading(true))

            favoritesUseCase.getFavorites().collect { result ->
                val favoriteList = result.toMutableList()
                val currencyPairs = mutableMapOf<String, String>()
                result.map {
                    currencyPairs[it.currencyFrom] =
                        if (currencyPairs[it.currencyFrom].isNullOrEmpty()) it.currencyTo else currencyPairs[it.currencyFrom] + "," + it.currencyTo
                }

                currencyPairs.map { currencyPairsItem ->
                    val job = async {
                        when (val result =
                            currenciesUseCase.getCurrentCurrencies(
                                currencyPairsItem.value, currencyPairsItem.key
                            )) {
                            is Resource.Success -> {
                                val data = MainScreenViewDataMapper.toModel(
                                    result.data
                                )
                                favoriteList.forEachIndexed { index, item ->
                                    if (item.currencyFrom == data?.baseCurrency) {
                                        favoriteList[index].currencyAmount =
                                            data.rates.firstOrNull { it.currency == favoriteList[index].currencyTo }?.rate
                                                ?: 0.0
                                    }
                                }
                            }
                            is Resource.Error -> {
                            }
                        }
                    }

                    job.await()
                }

                sendEvent(FavoritesUIEvent.ShowData(FavoritesScreenViewDataMapper.toModel(result)))
            }
        }
    }

    private fun sendEvent(event: FavoritesUIEvent) {
        reducer.sendEvent(event)
    }

    private class FavoritesReducer(initial: FavoritesState) :
        Reducer<FavoritesState, FavoritesUIEvent>(initial) {
        override fun reduce(oldState: FavoritesState, event: FavoritesUIEvent) {
            when (event) {
                is FavoritesUIEvent.ShowMainLoading -> {
                    setState(oldState.copy(isLoading = true))
                }
                is FavoritesUIEvent.ShowItemLoading -> {
                    setState(oldState.copy(isLoading = true))
                }
                is FavoritesUIEvent.ShowError -> {
                    setState(oldState.copy(error = event.errorMessage))
                }
                is FavoritesUIEvent.ShowData -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            favoritesViewModelDataList = event.favoritesViewModelDataList
                        )
                    )
                }
            }
        }
    }
}