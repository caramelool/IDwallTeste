package br.com.caramelo.idwallteste.data.model.response

import br.com.caramelo.idwallteste.data.model.entity.User
import com.google.gson.annotations.SerializedName

class AuthResponse(
    @SerializedName("user") val user: User?
)