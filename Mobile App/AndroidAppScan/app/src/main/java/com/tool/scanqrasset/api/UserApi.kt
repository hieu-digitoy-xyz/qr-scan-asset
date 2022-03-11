package com.tool.scanqrasset.api

import com.tool.scanqrasset.model.Asset
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("Asset")
    suspend fun getAsset(@Query("id") id: String): Asset

    @GET("Asset/name")
    suspend fun getAssets(@Query("name") name: String): List<Asset>
}