package br.com.caramelo.idwallteste.data.model.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("token") val token: String?
)