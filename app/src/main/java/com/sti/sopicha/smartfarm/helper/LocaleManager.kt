package com.sti.sopicha.smartfarm.helper

import android.os.Build.VERSION_CODES.N
import android.content.res.Resources
import java.util.Locale
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.content.res.Configuration


class LocaleManager(context: Context) {

    val LANGUAGE_ENGLISH: String = "en"
    val LANGUAGE_THAI: String = "th"
    private val LANGUAGE_KEY: String = "language_key"
    private val prefs: SharedPreferences

    val language: String
        get() = prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH)

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setLocale(c: Context): Context {
        return updateResources(c, language)
    }

    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(language)
        return updateResources(c, language)
    }

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(language: String) {
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit()
    }

    private fun updateResources(con: Context, language: String): Context {
        var context = con
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.getResources()
        val config = Configuration(res.getConfiguration())
        if (Utility.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.getDisplayMetrics())
        }
        return context
    }

    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (Utility.isAtLeastVersion(N)) config.locales.get(0) else config.locale
    }

}