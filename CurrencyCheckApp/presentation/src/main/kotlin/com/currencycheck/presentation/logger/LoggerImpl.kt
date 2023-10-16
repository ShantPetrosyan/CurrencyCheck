package com.currencycheck.presentation.logger

import android.util.Log
import com.currencycheck.domain.logger.CurrencyLogger

class LoggerImpl : CurrencyLogger {

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun w(tag: String, message: String?, throwable: Throwable?) {
        if (message == null && throwable != null) Log.w(tag, throwable)
        if (message != null && throwable == null) Log.w(tag, message)
        Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) Log.e(tag, message)
        Log.e(tag, message, throwable)
    }
}