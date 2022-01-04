package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidsApi
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    enum class AsteroidApiStatus { LOADING, ERROR, DONE }

    private val _status = MutableLiveData<AsteroidApiStatus>()

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    //todo: database
    //todo: repository

    init {
        getListOfAsteroids()
        getPictureOfDay()
    }

    private fun getListOfAsteroids() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                val asteroidsList = AsteroidsApi.getAsteroidList()
                if (asteroidsList.isNotEmpty()) {
                    _asteroids.value = asteroidsList
                }
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception){
                _status.value = AsteroidApiStatus.ERROR
                _asteroids.value = ArrayList()
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = AsteroidsApi.getPictureOfTheDay()
            } catch (e: Exception) {
                _pictureOfDay.value = null
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}