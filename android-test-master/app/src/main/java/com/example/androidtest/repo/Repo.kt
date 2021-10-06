package com.example.androidtest.repo

import com.example.androidtest.models.Breed
import io.reactivex.Single
import retrofit2.Response

interface Repo {

    fun getDogList(): Single<Response<List<Breed>>>
    fun searchDog(keyword: String): Single<Response<List<Breed>>>
}