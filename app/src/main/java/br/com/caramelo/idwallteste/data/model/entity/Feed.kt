package br.com.caramelo.idwallteste.data.model.entity

import com.google.gson.annotations.SerializedName

class Feed(
    @SerializedName("category") val category: String?,
    @SerializedName("list") val list: ArrayList<String>?
)