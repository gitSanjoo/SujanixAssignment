package com.sanjoo.sujanixassignment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "location_tracks")
@TypeConverters(Converters::class)
data class LocationTrackEntity (
    @PrimaryKey
    @ColumnInfo(name="start_time")
    val startTimestamp:Long,

    @ColumnInfo(name="end_time")
    val endTimestamp:Long,

    @ColumnInfo(name = "location_data")
    val locationData:ArrayList<LocationData>
)