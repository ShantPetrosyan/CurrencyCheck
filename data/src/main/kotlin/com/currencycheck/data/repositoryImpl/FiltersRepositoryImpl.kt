package com.currencycheck.data.repositoryImpl

import com.currencycheck.data.source.ILocalDataSource
import com.currencycheck.data.source.LocalDataSourceImpl
import com.currencycheck.domain.repositories.FiltersRepository

class FiltersRepositoryImpl(
    private val localDataSource: ILocalDataSource
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