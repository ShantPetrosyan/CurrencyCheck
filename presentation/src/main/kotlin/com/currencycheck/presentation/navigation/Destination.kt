package com.currencycheck.presentation.navigation

interface Destination {
    val route: String
}

object MainScreen : Destination {
    override val route: String = "currentCurrencyScreen"
}

object FilterOptions : Destination {
    override val route: String = "filterOptionsScreen"
}