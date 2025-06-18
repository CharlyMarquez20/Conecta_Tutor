package com.camc.proyecto_redesiii

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.90:3001/"

    val gson = GsonBuilder()
        .setLenient() // Permite JSON menos estricto
        .create()

    val instance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(APIService::class.java)
}