package ir.softap.mefit.ui.common.core

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import java.util.*

/**
 *
 */
class LocaleManager private constructor(private val application: Application) {

    /**
     *
     */
    companion object {

        private const val CURRENT_LOCALE = "currentLocale"

        private const val LANG_FA = "fa"
        private const val LANG_EN = "EN"

        val FA = Locale(LANG_FA)
        val EN = Locale(LANG_EN)

        val DEFAULT_APP_LOCALE = FA //initial app locale

        private var INSTANCE: LocaleManager? = null

        /**
         *
         */
        fun with(application: Application): LocaleManager {
            if (INSTANCE == null) {
                INSTANCE = LocaleManager(application)
                return INSTANCE!!
            }
            return INSTANCE!!
        }

        fun get() = INSTANCE!!
    }

    lateinit var currentLocale: Locale

    /**
     *
     */
    init {
        loadLocale()
        setLocale()
    }

    /**
     *
     */
    private fun loadLocale() {
        val prefs = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
        val language = prefs.getString(CURRENT_LOCALE, null)
        currentLocale = if (language != null) Locale(language) else DEFAULT_APP_LOCALE
    }

    /**
     *
     */
    private fun setLocale() {
        val config = application.baseContext.resources.configuration
        Locale.setDefault(currentLocale)
        config.locale = currentLocale
        val configuration = application.resources.configuration
        configuration.setLayoutDirection(currentLocale)
        //  resources.updateConfiguration(configuration, resources.displayMetrics)
        application.baseContext.resources.updateConfiguration(configuration, application.baseContext.resources.displayMetrics)
    }

    /**
     *
     */
    fun changeLocale(locale: Locale) {
        currentLocale = locale
        val prefs = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
        prefs.edit { putString(CURRENT_LOCALE, locale!!.language) }
        setLocale()
    }

    /**
     *
     */
    fun onConfigurationChanged() {
        setLocale()
    }

}