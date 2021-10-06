package com.example.androidtest.repo

import com.example.androidtest.models.Breed
import com.example.androidtest.rest.RestApi
import io.reactivex.Single
import retrofit2.Response

public class RepoImpl(private val restApi: RestApi) : Repo {

    override fun getDogList(): Single<Response<List<Breed>>> {
       return restApi.getDogList()
    }

    override fun searchDog(keyword: String): Single<Response<List<Breed>>> {
        return restApi.searchDog(keyword)
    }
}