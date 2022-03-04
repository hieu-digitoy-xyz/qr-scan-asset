package com.tool.scanqrasset.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Asset(
    @SerializedName("partId") val partId: String,
    @SerializedName("description") val description: String,
    @SerializedName("useFor") val useFor: String,
    @SerializedName("storageBin") val storageBin: String,
    @SerializedName("stockAmount") val stockAmount: Int,
    @SerializedName("search") val search: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("local") val local: Boolean,
    @SerializedName("import") val import: Boolean,
    @SerializedName("note") val note: String,
    @SerializedName("alpSop") val alp: String,
    @SerializedName("note2") val note2: String,
    @SerializedName("supplier") val supplier: String,
) : Serializable