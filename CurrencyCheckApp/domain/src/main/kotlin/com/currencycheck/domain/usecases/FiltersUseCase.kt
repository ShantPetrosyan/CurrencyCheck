package com.currencycheck.domain.usecases

import com.currencycheck.domain.repositories.FiltersRepository

interface IFilterUseCase {
    fun getSelectedFilter(): Int
    fun updateSelectedFilters(selectedFilterPosition: Int): Int
    fun getSelectedMainCurrency(): String
    fun updateMainCurrency(currency: String)
}

class FilterUseCaseImpl(
    private val repository: FiltersRepository
) : IFilterUseCase {

    override fun getSelectedFilter(): Int {
        return repository.getSelectedFilterPosition()
    }

    override fun updateSelectedFilters(selectedFilterPosition: Int): Int {
        return repository.updateSelectedFilterPosition(selectedFilterPosition)
    }

    override fun getSelectedMainCurrency(): String {
        return repository.getSelectedMainCurrency()
    }

    override fun updateMainCurrency(currency: String) {
        repository.updateMainCurrency(currency)
    }
}