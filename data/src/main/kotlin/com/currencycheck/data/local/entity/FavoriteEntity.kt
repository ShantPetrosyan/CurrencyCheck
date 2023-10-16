package com.currencycheck.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "currency_from")
    var currencyFromName: String = "",

    @ColumnInfo(name = "currency_to")
    var currencyToName: String = ""
)