package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DataFilter
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidsDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application)  : AndroidViewModel(application) {

    private val database = getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    val asteroids = asteroidsRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: MutableLiveData<PictureOfDay?>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        getListOfAsteroids()
        getPictureOfDay()
    }

    private fun getListOfAsteroids() {
        viewModelScope.launch {
            asteroidsRepository.getAllAsteroids()
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = asteroidsRepository.getPictureOfTheDay()
            } catch (e: Exception) {
                _pictureOfDay.value = null
            }
        }
    }

    fun onFilterChanged(dataFilter: DataFilter) {
        //todo: implement filter in repository
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}