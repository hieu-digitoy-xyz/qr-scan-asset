package com.tool.scanqrasset.api

import com.tool.scanqrasset.model.Asset
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserService {
    companion object {
        const val BASE_URL: String = "http://13.125.214.163:7778/api/"
//        const val BASE_URL: String = "http://192.168.1.11:5001/v1/api/"
    }

    private val userApi: UserApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApi = retrofit.create(UserApi::class.java)
    }

    suspend fun getAsset(id: String): Asset {
        return userApi.getAsset(id)
    }
}