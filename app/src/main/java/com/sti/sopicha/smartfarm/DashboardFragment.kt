package com.sti.sopicha.smartfarm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class DashboardFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard, container, false)

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}