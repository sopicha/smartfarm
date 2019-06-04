package com.sti.sopicha.smartfarm

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.sti.sopicha.smartfarm.helper.SmartFarm


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar
    lateinit var sharedPreferences: SharedPreferences
    private  val APP_LANGUAGE_KEY = "app_language"
    private  val FRAGMENT_KEY = "app_fragment"
    private val TAG = "sensordata"
    lateinit var storedValue:String
    private val dashFragmentRef: String = "dash"
    private val webFragmentRef: String = "web"
    private val settFragmentRef: String = "sett"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                toolbar.title = getString(R.string.actionbar_dashboard)
                sharedPreferences.edit().putString(FRAGMENT_KEY, dashFragmentRef).apply()
                val dashboardFragment = DashboardFragment.newInstance()
                openFragment(dashboardFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_webcam -> {
                toolbar.title = getString(R.string.actionbar_webcam)
                sharedPreferences.edit().putString(FRAGMENT_KEY, webFragmentRef).apply()
                val cameraFragment = CameraFragment.newInstance()
                openFragment(cameraFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                toolbar.title = getString(R.string.actionbar_setting)
                sharedPreferences.edit().putString(FRAGMENT_KEY, settFragmentRef).apply()
                val settingFragment = SettingFragment.newInstance()
                openFragment(settingFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        storedValue = sharedPreferences.getString(APP_LANGUAGE_KEY, "en")
        setContentView(R.layout.activity_main)
        toolbar = supportActionBar!!


        navig.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onResume() {
        super.onResume()
        val fragment = sharedPreferences.getString(FRAGMENT_KEY, "dash")
        when(fragment){
            dashFragmentRef->{
                val dashboardFragment = DashboardFragment.newInstance()
                openFragment(dashboardFragment)
                toolbar.title = getString(R.string.actionbar_dashboard)
                navig.menu.findItem(R.id.navigation_dashboard).isChecked = true
            }
            webFragmentRef->{
                val cameraFragment = CameraFragment.newInstance()
                openFragment(cameraFragment)
                toolbar.title = getString(R.string.actionbar_webcam)
                navig.menu.findItem(R.id.navigation_webcam).isChecked = true
            }
            settFragmentRef->{
                val settingFragment = SettingFragment.newInstance()
                openFragment(settingFragment)
                toolbar.title = getString(R.string.actionbar_setting)
                navig.menu.findItem(R.id.navigation_setting).isChecked = true
            }
        }

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SmartFarm.localeManager.setLocale(newBase))

    }


    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        //transaction.addToBackStack(null)
        transaction.commit()
    }

}
