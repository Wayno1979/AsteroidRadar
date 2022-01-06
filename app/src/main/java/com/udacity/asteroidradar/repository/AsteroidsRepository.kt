package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asAsteroid
import com.udacity.asteroidradar.database.asDatabaseAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDatabaseDao.getAllAsteroids()) {
            it.asAsteroid()
        }

    suspend fun getAllAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidsApi.getAsteroidList()
            database.asteroidDatabaseDao.insertAllAsteroids(asteroids.asDatabaseAsteroid())
        }
    }

    suspend fun getPictureOfTheDay(): PictureOfDay {
        lateinit var pictureOfTheDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfTheDay = AsteroidsApi.getPictureOfTheDay()
        }
        return pictureOfTheDay
    }
}