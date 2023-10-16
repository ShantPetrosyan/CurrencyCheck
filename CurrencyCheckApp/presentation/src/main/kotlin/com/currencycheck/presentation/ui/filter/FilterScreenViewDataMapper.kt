package com.currencycheck.presentation.ui.filter

import android.content.Context
import com.currencycheck.presentation.R
import com.currencycheck.presentation.ui.filter.data.FilterViewModelData
import javax.inject.Inject

class FilterScreenViewDataMapper @Inject constructor(var context: Context) {
    fun toModel(selectedFilterPosition: Int): List<FilterViewModelData> {
        return getFilterList(selectedFilterPosition, context)
    }

    fun getFilterList(selectedFilterPosition: Int, context: Context) =
        mutableListOf<FilterViewModelData>().apply {
            add(
                FilterViewModelData(
                    context.getString(R.string.start_to_end),
                    selectedFilterPosition == 0
                )
            )
            add(
                FilterViewModelData(
                    context.getString(R.string.end_to_start),
                    selectedFilterPosition == 1
                )
            )
            add(
                FilterViewModelData(
                    context.getString(R.string.ascending),
                    selectedFilterPosition == 2
                )
            )
            add(
                FilterViewModelData(
                    context.getString(R.string.descending),
                    selectedFilterPosition == 3
                )
            )
        }
}