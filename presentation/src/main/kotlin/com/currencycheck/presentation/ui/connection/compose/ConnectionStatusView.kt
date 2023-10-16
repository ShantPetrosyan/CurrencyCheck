package com.currencycheck.presentation.ui.connection.compose

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.currencycheck.presentation.R
import com.currencycheck.presentation.ui.connection.ConnectionState
import com.currencycheck.presentation.ui.connection.currentConnectivityState
import com.currencycheck.presentation.ui.connection.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus() {
    // This will cause re-composition on every network state change
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available

    val context = LocalContext.current

    if (!isConnected) {
        Toast.makeText(
            context,
            stringResource(id = R.string.no_internet_connection),
            Toast.LENGTH_LONG
        ).show()
    }
}