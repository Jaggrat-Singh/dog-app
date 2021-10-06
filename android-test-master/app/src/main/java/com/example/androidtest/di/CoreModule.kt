package com.example.androidtest.di

import com.example.androidtest.BuildConfig
import com.example.androidtest.repo.Repo
import com.example.androidtest.repo.RepoImpl
import com.example.androidtest.rest.RestApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class CoreModule {

    companion object {
        private const val BASE_URL = "https://api.thedogapi.com/"

        private const val TIMEOUT_CONNECT_IN_SEC = 10

        private const val TIMEOUT_READ_IN_SEC = 15

        private const val TIMEOUT_WRITE_IN_SEC = 15
    }

    @Provides
    @Singleton
    fun provideRepo(restApi: RestApi): Repo {
        return RepoImpl(restApi)
    }


    @Singleton
    @Provides
    fun provideRestApi(okHttpClient: OkHttpClient, converter: Converter.Factory): RestApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converter)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(RestApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logger)
        }

        builder.connectTimeout(TIMEOUT_CONNECT_IN_SEC.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ_IN_SEC.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE_IN_SEC.toLong(), TimeUnit.SECONDS)

        return builder.build()
    }

    @Provides
    internal fun provideGsonConverter(): Converter.Factory = GsonConverterFactory.create(GsonBuilder().create())
}