package br.com.caramelo.idwallteste.data.model.request

import com.google.gson.annotations.SerializedName

class AuthRequest(
        @SerializedName("email") val email: String
)