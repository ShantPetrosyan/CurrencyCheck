package com.currencycheck.data.repositoryImpl

import com.currencycheck.data.source.LocalDataSource
import com.currencycheck.domain.repositories.FiltersRepository

class FiltersRepositoryImpl(
    private val localDataSource: LocalDataSource
) : FiltersRepository {
    override fun getSelectedFilterPosition(): Int {
        return localDataSource.getSelectedFilterPosition()
    }

    override fun updateSelectedFilterPosition(position: Int): Int {
        return localDataSource.updateSelectedFilterPosition(position)
    }

    override fun getSelectedMainCurrency(): String {
        return localDataSource.getSelectedMainCurrency()
    }

    override fun updateMainCurrency(currency: String) {
        return localDataSource.updateMainCurrency(currency)
    }
}