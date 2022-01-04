package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface IAsteroidsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        //@Query("start_date") startDate: String,
        //@Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String) : PictureOfDay
}

object AsteroidsApi {
    private val retrofitService : IAsteroidsApiService by lazy {
        retrofit.create(IAsteroidsApiService::class.java)
    }

    suspend fun getAsteroidList() : List<Asteroid> {
        //val response = retrofitService.getAsteroids("","", API_KEY)
        val response = retrofitService.getAsteroids(API_KEY)
        val jsonObject = JSONObject(response)
        return parseAsteroidsJsonResult(jsonObject)
    }

    suspend fun getPictureOfTheDay() : PictureOfDay {
        return retrofitService.getPictureOfDay(API_KEY)
    }
}