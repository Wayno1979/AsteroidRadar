package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DataFilter
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.api.getTodaysDateFormatted
import com.udacity.asteroidradar.api.getWeekDateFormatted
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asAsteroid
import com.udacity.asteroidradar.database.asDatabaseAsteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    private val _dataFilter = MutableLiveData(DataFilter.WEEKLY)
    private val dataFilter : LiveData<DataFilter>
        get() = _dataFilter

    private val _startDate = getTodaysDateFormatted()
    private val _endDate = getWeekDateFormatted()


    val asteroids : LiveData<List<Asteroid>> = Transformations.switchMap(dataFilter) { filter ->
        when(filter){
            DataFilter.WEEKLY ->
                Transformations.map(database.asteroidDatabaseDao.getThisWeeksAsteroids(_startDate,_endDate)){ dbAsteroid ->
                    dbAsteroid.asAsteroid()
                }

            DataFilter.TODAY ->
                Transformations.map(database.asteroidDatabaseDao.getTodaysAsteroids(_startDate)){ dbAsteroid ->
                    dbAsteroid.asAsteroid()
                }

            DataFilter.SAVED ->
                Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()){ dbAsteroid ->
                    dbAsteroid.asAsteroid()
                }
        }
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

    fun updateFilter(dataFilter: DataFilter){
        _dataFilter.value = dataFilter
    }
}