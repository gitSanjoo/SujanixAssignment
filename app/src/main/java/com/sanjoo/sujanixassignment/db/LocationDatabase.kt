package com.sanjoo.sujanixassignment.db

import androidx.room.Database

@Database(
    entities = [DataLocations::class],
    version = 1
)
abstract class LocationDatabase {

    abstract fun getLocationDao():LocationDao

    companion object{

    }
}