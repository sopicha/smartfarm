package com.sti.sopicha.smartfarm

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                toolbar.title = "Smart Farm Summary Dashboard"
                val dashboardFragment = DashboardFragment.newInstance()
                openFragment(dashboardFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_webcam -> {
                toolbar.title = "Smart Farm Webcam"
                //message.setText(R.string.title_webcam)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                toolbar.title = "Setting"
                //message.setText(R.string.title_setting)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = supportActionBar!!
        val dashboardFragment = DashboardFragment.newInstance()
        openFragment(dashboardFragment)
        toolbar.title = "Smart Farm Summary Dashboard"

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
