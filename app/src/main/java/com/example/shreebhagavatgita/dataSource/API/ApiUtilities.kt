package com.example.shreebhagavatgita.dataSource.API

import androidx.room.util.foreignKeyCheck
import okhttp3.OkHttpClient
import okio.Okio
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    private val headers = mapOf(

        "Accept" to "application/json",
        "x-rapidapi-key" to "2ea12331d8msh72719fc9b5726b2p130e3djsn173b747fbaba",
        "x-rapidapi-host" to "bhagavad-gita3.p.rapidapi.com"
    )

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().apply {
                headers.forEach{ (key, value) -> addHeader(key,value) }
            }.build()
            chain.proceed(newRequest)
        }
    }.build()

    val api : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://bhagavad-gita3.p.rapidapi.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }



}