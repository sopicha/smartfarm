package com.sti.sopicha.smartfarm

import android.util.Log
import com.google.firebase.database.*
import com.sti.sopicha.smartfarm.model.Data
import com.google.firebase.database.DataSnapshot



class FirebaseTask(var dt: Data?,dat: FirebaseDatabase,val refCmd: String,val refDt: String){
    private val TAG = "sensordata"
    var database: FirebaseDatabase? = null
    var refCommand: DatabaseReference? = null
    var refData: DatabaseReference? = null
    lateinit var query: Query
    var data: Data? = null
    init {
        this.data = dt
        this.database = dat
        this.refCommand = dat.getReference(refCmd)
        this.refData = dat.getReference(refDt)
    }

    fun writeCommand(ID:String, value: Int){
        refCommand!!.child(ID).setValue(value)
    }

    fun readData(): Data?{


        query =  refData!!.orderByKey().limitToLast(1)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read user!")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                  //  data = dataSnapshot.getValue(Data::class.java::class.java!!)
                for (child in dataSnapshot.children) {
                    data= Data(child.child("humidity").value!!.toString(),
                            child.child("light").value!!.toString(),
                            child.child("soil").value!!.toString(),
                            child.child("temperature").value!!.toString(),
                            child.child("time").value!!.toString())
                    Log.d(TAG, data!!.humidity.toString())
                }
            }

        })

        return data
    }
}