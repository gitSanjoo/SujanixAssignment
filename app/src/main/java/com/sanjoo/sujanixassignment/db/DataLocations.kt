package com.sanjoo.sujanixassignment.db

import androidx.room.Entity

@Entity
data class DataLocations(
    val date:String,
    val time:String,
    val area:String
)
