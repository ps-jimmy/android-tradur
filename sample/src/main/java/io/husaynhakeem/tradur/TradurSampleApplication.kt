package io.husaynhakeem.tradur

import android.app.Application


class TradurSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Tradur.init(getString(R.string.google_translate_api_key))
    }
}