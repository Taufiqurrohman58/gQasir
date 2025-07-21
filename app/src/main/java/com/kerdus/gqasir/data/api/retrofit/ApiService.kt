package com.kerdus.gqasir.data.api.retrofit

import com.kerdus.gqasir.Product
import com.kerdus.gqasir.data.api.request.ProdukCreateRequest
import com.kerdus.gqasir.data.api.request.ProdukUpdateRequest
import com.kerdus.gqasir.data.api.request.TransaksiRequest
import com.kerdus.gqasir.data.api.response.LoginResponse
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @PUT("api/produk/{id}")
    suspend fun updateProduk(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body produk: ProdukUpdateRequest
    ): ProdukResponseItem

    @POST("api/produk")
    suspend fun postProduk(
        @Header("Authorization") token: String,
        @Body request: ProdukCreateRequest
    ): Response<Unit>

    @DELETE("api/produk/{id}")
    suspend fun deleteProduk(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @POST("api/transaksi")
    suspend fun postTransaksi(
        @Header("Authorization") token: String,
        @Body transaksi: TransaksiRequest
    ): Response<Unit>

}

