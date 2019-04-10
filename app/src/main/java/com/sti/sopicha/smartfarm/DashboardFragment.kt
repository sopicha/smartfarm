package com.sti.sopicha.smartfarm

import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.budiyev.android.circularprogressbar.CircularProgressBar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.sti.sopicha.smartfarm.helper.FirebaseCallback
import com.sti.sopicha.smartfarm.helper.FirebaseTask
import com.sti.sopicha.smartfarm.model.Data
import me.itangqi.waveloadingview.WaveLoadingView
import java.lang.StringBuilder

class DashboardFragment: Fragment() {

    private val TAG = "sensordata"
    lateinit var sharedPreferences: SharedPreferences
    private  val FLOW_SWITCH_KEY1 = "app_flow_switch1"
    private  val FLOW_SWITCH_KEY2 = "app_flow_switch2"
    private  val FLOW_SWITCH_KEY3 = "app_flow_switch3"

    lateinit var tempProg: CircularProgressBar
    lateinit var tempProgText: TextView
    lateinit var humidProg: WaveLoadingView
    lateinit var humidProgText: TextView
    lateinit var lightProgText: TextView
    lateinit var moistProgText: TextView
    lateinit var firebaseTask: FirebaseTask
    lateinit var waterFlowId1: Switch
    lateinit var waterFlowId2: Switch
    lateinit var waterFlowId3: Switch
    private var data: Data? = null
    lateinit var typeface: Typeface

    companion object {

        fun newInstance(): DashboardFragment = DashboardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        // init UI view
        typeface = Typeface.createFromAsset(context!!.assets,"Lato-Light.ttf")
        tempProg = view.findViewById(R.id.temp_prog)
        tempProgText = view.findViewById(R.id.temp_prog_text)
        humidProg = view.findViewById(R.id.humid_prog)
        humidProgText = view.findViewById(R.id.humid_prog_text)
        lightProgText = view.findViewById(R.id.light_value)
        moistProgText = view.findViewById(R.id.mois_value)
        waterFlowId1 = view.findViewById(R.id.id1)
        waterFlowId2 = view.findViewById(R.id.id2)
        waterFlowId3 = view.findViewById(R.id.id3)

        //set font typeface in case of sdk lower than 26
        waterFlowId1.typeface = typeface
        waterFlowId2.typeface = typeface
        waterFlowId3.typeface = typeface


        FirebaseApp.initializeApp(this.requireContext())

        firebaseTask = FirebaseTask(data, FirebaseDatabase.getInstance(), "Command", "Data")
        firebaseTask.readData(object : FirebaseCallback {
            override fun onCallback(data: Data?) {
                tempProg.progress = data!!.temperature!!.toFloat()
                tempProgText.text = StringBuilder().append(data.temperature).append("°C")

                humidProg.progressValue = data.humidity!!.toInt()
                humidProgText.text = StringBuilder().append(data.humidity).append("%")

                lightProgText.text = StringBuilder().append(data.light).append("%")
                moistProgText.text = StringBuilder().append(data.soil).append("%")
            }
        })

        waterFlowId1.isChecked = getSwitchState(FLOW_SWITCH_KEY1)
        waterFlowId2.isChecked = getSwitchState(FLOW_SWITCH_KEY2)
        waterFlowId3.isChecked = getSwitchState(FLOW_SWITCH_KEY3)

        setSwitchOnClick(R.string.switch_water_flow_id_1,waterFlowId1,FLOW_SWITCH_KEY1)
        setSwitchOnClick(R.string.switch_water_flow_id_2,waterFlowId2,FLOW_SWITCH_KEY2)
        setSwitchOnClick(R.string.switch_water_flow_id_3,waterFlowId3,FLOW_SWITCH_KEY3)

    }

    private fun getSwitchState(key: String): Boolean{
        val switchState = sharedPreferences.getBoolean(key, false)
        return switchState
    }

    private fun setSwitchOnClick(resID:Int,switch:Switch, key:String){
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                firebaseTask.writeCommand(getString(resID), 0)
                sharedPreferences.edit().putBoolean(key, true).apply()
            } else {
                firebaseTask.writeCommand(getString(resID), 1)
                sharedPreferences.edit().putBoolean(key, false).apply()
            }
        }
    }
}