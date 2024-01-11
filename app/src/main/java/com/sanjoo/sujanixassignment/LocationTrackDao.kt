package com.sanjoo.sujanixassignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationTrackDao {

    @Query("SELECT * FROM location_tracks")
    fun getAllTracks(): List<LocationTrackEntity>

    @Query("SELECT * FROM location_tracks WHERE start_time = :startTime")
    fun getTrack(startTime: Long): LocationTrackEntity

    @Insert
    fun addTrack(trackEntity: LocationTrackEntity)
}