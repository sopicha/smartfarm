package com.sti.sopicha.smartfarm

import com.sti.sopicha.smartfarm.model.Data

interface FirebaseCallback {
    fun onCallback(data: Data?)
}