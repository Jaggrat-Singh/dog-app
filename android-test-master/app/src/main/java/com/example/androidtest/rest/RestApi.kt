package com.example.androidtest.rest

import com.example.androidtest.models.Breed
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
    @GET("/v1/breeds")
    fun getDogList(
        @Query ("x-api-key") apiKey: String = "49578bcd-001b-436b-8404-aeb0af34fcd0")
            : Single<Response<List<Breed>>>

    @GET("/v1/breeds/search")
    fun searchDog(
        @Query ("q") keyword: String,
        @Query ("x-api-key") apiKey: String = "49578bcd-001b-436b-8404-aeb0af34fcd0")
            : Single<Response<List<Breed>>>
}