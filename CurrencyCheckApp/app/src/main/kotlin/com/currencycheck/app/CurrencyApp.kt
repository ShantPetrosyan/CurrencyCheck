package com.currencycheck.app

import android.app.Application
import com.currencycheck.data.di.dataModules
import com.currencycheck.domain.di.domainModule
import com.currencycheck.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CurrencyApp)
            modules(dataModules + domainModule + presentationModule)
        }
    }
}