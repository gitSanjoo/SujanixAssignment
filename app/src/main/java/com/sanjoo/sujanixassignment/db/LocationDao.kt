package com.sanjoo.sujanixassignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sanjoo.sujanixassignment.db.DataLocations

@Dao
interface LocationDao {
    @Insert
    fun addLocation(locations: DataLocations)

    @Query("SELECT * FROM DataLocations")
    fun getAllLocations():List<DataLocations>
}