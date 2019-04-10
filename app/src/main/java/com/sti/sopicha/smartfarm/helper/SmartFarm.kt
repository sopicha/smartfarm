package com.sti.sopicha.smartfarm.helper

import android.util.Log
import android.app.Application
import android.content.Context
import android.content.res.Configuration


class SmartFarm : Application() {

    companion object {
        lateinit var localeManager: LocaleManager
    }

    private val TAG = "App"
    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager.setLocale(base))
        Log.d(TAG, "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage())
    }

}
