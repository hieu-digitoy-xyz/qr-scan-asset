package com.tool.scanqrasset.api

import com.tool.scanqrasset.model.Asset
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("Asset")
    suspend fun getAssets(): List<Asset>

    @GET("Asset")
    suspend fun getAsset(@Query("partId") id: String): Asset

}