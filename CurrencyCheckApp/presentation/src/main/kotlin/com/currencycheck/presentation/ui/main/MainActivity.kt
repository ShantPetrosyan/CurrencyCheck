package com.currencycheck.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.Dp
import com.currencycheck.presentation.navigation.NavGraph
import com.currencycheck.presentation.ui.theme.CurrencyCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyCheckTheme {
                NavGraph(Dp(0f))
            }
        }
    }
}