package com.sti.sopicha.smartfarm

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import com.budiyev.android.circularprogressbar.CircularProgressBar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.sti.sopicha.smartfarm.model.Data
import kotlinx.android.synthetic.*
import me.itangqi.waveloadingview.WaveLoadingView
import java.lang.StringBuilder

class DashboardFragment: Fragment() {

    private val TAG = "sensordata"

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

        firebaseTask = FirebaseTask(data,FirebaseDatabase.getInstance(),"Command","Data")
        firebaseTask.readData(object : FirebaseCallback {
            override fun onCallback(data: Data?) {
                tempProg.progress = data!!.temperature!!.toFloat()
                tempProgText.text = StringBuilder().append(data.temperature).append("°C")

                humidProg.progressValue = data.humidity!!.toInt()
                humidProgText.text = StringBuilder().append(data.humidity).append("%")

                lightProgText.text = StringBuilder().append(data.light).append("%")
                moistProgText.text = StringBuilder().append(data.soil).append("%")

                Log.d(TAG, data!!.humidity.toString())
            }
        })

        waterFlowId1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_1), 0)
            else
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_1), 1)
        }

        waterFlowId2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_2), 0)
            else
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_2), 1)
        }

        waterFlowId3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_3), 0)
            else
                firebaseTask.writeCommand(getString(R.string.switch_water_flow_id_3), 1)
        }
    }
}