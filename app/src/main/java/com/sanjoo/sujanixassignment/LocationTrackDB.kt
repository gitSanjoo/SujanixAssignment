package com.sanjoo.sujanixassignment

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationTrackEntity::class], version = 1)
abstract class LocationTrackDB : RoomDatabase() {
    abstract fun tracksDao(): LocationTrackDao

    companion object{
        const val DB_NAME="location_track_db"
    }

}

