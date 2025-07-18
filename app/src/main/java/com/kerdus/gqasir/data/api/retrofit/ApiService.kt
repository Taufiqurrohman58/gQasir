package com.kerdus.gqasir.data.api.retrofit

import com.kerdus.gqasir.Product
import com.kerdus.gqasir.data.api.response.LoginResponse
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginRequest(
    val username: String,
    val password: String
)


interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse


    @GET("api/produk")
    suspend fun getProduk(
        @Header("Authorization") token: String
    ): List<ProdukResponseItem>

    @GET("produk")
    fun getProductsByCategory(
        @Query("kategori") kategori: String,
        @Header("Authorization") token: String
    ): Call<List<Product>>
}

