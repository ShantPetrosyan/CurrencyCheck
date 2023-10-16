package com.currencycheck.presentation.ui.connection

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}
