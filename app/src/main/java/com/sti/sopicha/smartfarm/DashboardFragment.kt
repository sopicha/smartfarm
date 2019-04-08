package com.sti.sopicha.smartfarm

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.budiyev.android.circularprogressbar.CircularProgressBar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.sti.sopicha.smartfarm.model.Data
import kotlinx.android.synthetic.*
import me.itangqi.waveloadingview.WaveLoadingView

class DashboardFragment: Fragment() {

    private val TAG = "sensordata"

    lateinit var tempProg: CircularProgressBar
    lateinit var tempProgText: TextView
    lateinit var humidProg: WaveLoadingView
    lateinit var lightProgText: TextView
    lateinit var moistProgText: TextView
    lateinit var firebaseTask: FirebaseTask
    lateinit var waterFlowId1: Switch
    lateinit var waterFlowId2: Switch
    lateinit var waterFlowId3: Switch
    private var data: Data? = null

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempProg = view.findViewById<CircularProgressBar>(R.id.temp_prog)
        tempProgText = view.findViewById(R.id.temp_prog_text)
        humidProg = view.findViewById(R.id.humid_prog)
        lightProgText = view.findViewById(R.id.light_value)
        moistProgText = view.findViewById(R.id.mois_value)
        waterFlowId1 = view.findViewById(R.id.id1)
        waterFlowId2 = view.findViewById(R.id.id2)
        waterFlowId3 = view.findViewById(R.id.id3)
        FirebaseApp.initializeApp(this.requireContext())

        firebaseTask = FirebaseTask(data,FirebaseDatabase.getInstance(),"Command","Data")
        data = firebaseTask.readData()

        if(data != null)
            Log.d(TAG, data!!.humidity.toString())
        else
            Log.d(TAG, "No data")

        //tempProg.progress = 30f

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