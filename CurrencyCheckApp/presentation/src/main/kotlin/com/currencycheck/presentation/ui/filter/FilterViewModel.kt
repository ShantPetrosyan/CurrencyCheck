package com.currencycheck.presentation.ui.filter

import androidx.lifecycle.viewModelScope
import com.currencycheck.domain.usecases.IFilterUseCase
import com.currencycheck.presentation.base.BaseViewModel
import com.currencycheck.presentation.base.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterUseCase: IFilterUseCase,
    private val filterMapper: FilterScreenViewDataMapper
) : BaseViewModel<FilterState, FilterUIEvent>() {

    private val reducer = FilterReducer(FilterState.initial())

    override val state: StateFlow<FilterState>
        get() = reducer.state

    init {
        loadFiltersInfo()
    }

    private fun loadFiltersInfo() {
        viewModelScope.launch {
            val selectedFilterIndex = filterUseCase.getSelectedFilter()
            sendEvent(FilterUIEvent.ShowData(filterMapper.toModel(selectedFilterIndex)))
        }
    }

    fun updateSelectedFilter(selectedFilterPosition: Int) {
        viewModelScope.launch {
            filterUseCase.updateSelectedFilters(selectedFilterPosition)
            sendEvent(FilterUIEvent.CompleteSavingFilters)
        }
    }

    private fun sendEvent(event: FilterUIEvent) {
        reducer.sendEvent(event)
    }

    private class FilterReducer(initial: FilterState) :
        Reducer<FilterState, FilterUIEvent>(initial) {
        override fun reduce(oldState: FilterState, event: FilterUIEvent) {
            when (event) {
                is FilterUIEvent.ShowError -> {
                    setState(oldState.copy(error = event.errorMessage))
                }
                is FilterUIEvent.ShowData -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            filterViewModelDataList = event.filterViewModelDataList
                        )
                    )
                }
                is FilterUIEvent.CompleteSavingFilters -> {
                    setState(
                        oldState.copy(
                            filterApplied = true
                        )
                    )
                }
            }
        }
    }
}