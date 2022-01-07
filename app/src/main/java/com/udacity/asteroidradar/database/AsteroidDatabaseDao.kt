package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(allAsteroids: List<DatabaseAsteroid>)

    @Query("SELECT * FROM asteroids_table ORDER by closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table where closeApproachDate = :date ORDER by closeApproachDate")
    fun getTodaysAsteroids(date: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate")
    fun getThisWeeksAsteroids(startDate:String, endDate:String): LiveData<List<DatabaseAsteroid>>
}