package com.sti.sopicha.smartfarm.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Data{
    var humidity: String? = null
    var light: String? = null
    var soil:String? = null
    var temperature:String? = null
    var time: String? = null

    constructor(){
        // Default constructor required for calls to DataSnapshot.getValue(Read.class)
    }

    constructor(humidity:String?, light:String?, soil:String?, temperature:String?){
        this.humidity = humidity
        this.light = light
        this.soil = soil
        this.temperature = temperature
        //this.time = time
    }
}