package com.sti.sopicha.smartfarm

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sti.sopicha.smartfarm.helper.SmartFarm


class SettingFragment: Fragment(){
    private val TAG = "sensordata"
    lateinit var sharedPreferences:SharedPreferences
    lateinit var radioGroup: RadioGroup
    private  val APP_LANGUAGE_KEY = "app_language"
    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }

    private var viewSetting:View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewSetting = inflater.inflate(R.layout.setting, container, false)
        return viewSetting
    }

    @SuppressWarnings("deprecation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val storedValue = sharedPreferences.getString(APP_LANGUAGE_KEY, "en")
        radioGroup = view.findViewById(R.id.lang_radiogroup)

        when(storedValue){
            "en" -> {
                radioGroup.check(R.id.eng_radiobutt)
            }
            "th" ->{
                radioGroup.check(R.id.thai_radiobutt)
            }

        }

        // Get radio group selected item using on checked change listener
        radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val context:Context = this.context!!
                    val radio: RadioButton = view.findViewById(checkedId)
                    when(radio.text) {
                        getString(R.string.eng_lang)->{
                            sharedPreferences.edit().putString(APP_LANGUAGE_KEY, "en").apply()
                            SmartFarm.localeManager.setNewLocale(context, SmartFarm.localeManager.LANGUAGE_ENGLISH)
                            activity!!.recreate()

                        }
                        getString(R.string.thai_lang)->{
                            sharedPreferences.edit().putString(APP_LANGUAGE_KEY, "th").apply()
                            SmartFarm.localeManager.setNewLocale(context, SmartFarm.localeManager.LANGUAGE_THAI)
                            activity!!.recreate()
                        }
                    }

                })

    }
}