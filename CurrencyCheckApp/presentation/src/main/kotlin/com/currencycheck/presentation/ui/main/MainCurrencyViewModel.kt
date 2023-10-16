package com.currencycheck.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.currencycheck.domain.usecases.ICurrentCurrenciesUseCase
import com.currencycheck.domain.usecases.IFavoritesUseCase
import com.currencycheck.domain.usecases.IFilterUseCase
import com.currencycheck.domain.util.Resource
import com.currencycheck.presentation.base.BaseViewModel
import com.currencycheck.presentation.base.Reducer
import com.currencycheck.presentation.ui.helper.CurrencyHelper.getInitialCurrencies
import com.currencycheck.presentation.ui.main.MainScreenViewDataMapper.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCurrencyViewModel @Inject constructor(
    private val currenciesUseCase: ICurrentCurrenciesUseCase,
    private val favoritesUseCase: IFavoritesUseCase,
    private val filterUseCase: IFilterUseCase
) : BaseViewModel<CurrencyState, CurrencyUIEvent>() {

    private val reducer = MainReducer(CurrencyState.initial())

    override val state: StateFlow<CurrencyState>
        get() = reducer.state

    init {
        loadCurrencyInfo(getDefaultCurrency())
    }

    fun getDefaultCurrency(): String {
        return filterUseCase.getSelectedMainCurrency()
    }

    private fun sendEvent(event: CurrencyUIEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: CurrencyState) :
        Reducer<CurrencyState, CurrencyUIEvent>(initial) {
        override fun reduce(oldState: CurrencyState, event: CurrencyUIEvent) {
            when (event) {
                is CurrencyUIEvent.ShowLoading -> {
                    setState(oldState.copy(isLoading = true))
                }
                is CurrencyUIEvent.ShowError -> {
                    setState(oldState.copy(error = event.errorMessage, errorCode = event.errorCode))
                }
                is CurrencyUIEvent.ShowData -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currencyModelData = event.currenciesViewModelData
                        )
                    )
                }
            }
        }
    }

    fun updateFavoriteStatus(isActive: Boolean, currencyFrom: String, currencyTo: String) {
        viewModelScope.launch {
            favoritesUseCase.updateFavoriteStatus(isActive, currencyFrom, currencyTo)
        }
    }

    fun loadCurrencyInfo(initialCurrency: String) {
        viewModelScope.launch {
            sendEvent(CurrencyUIEvent.ShowLoading(true))

            filterUseCase.updateMainCurrency(initialCurrency)
            val currencyList = getInitialCurrencies(initialCurrency)

            when (val result =
                currenciesUseCase.getCurrentCurrencies(
                    currencyList,
                    initialCurrency
                )) {
                is Resource.Success -> {
                    val selectedFilterType = filterUseCase.getSelectedFilter()
                    favoritesUseCase.getFavoritesFor(initialCurrency).collect { favorites ->
                        sendEvent(CurrencyUIEvent.ShowData(toModel(result.data, favorites, selectedFilterType)))
                    }
                }
                is Resource.Error -> {
                    sendEvent(CurrencyUIEvent.ShowError(result.message ?: "", result.errorCode))
                }
            }
        }
    }

    companion object {
        const val TO_FILTER_KEY = "to_resort_data"
    }
}