package com.kerdus.gqasir.data

import com.kerdus.gqasir.data.api.response.LoginResponse
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import com.kerdus.gqasir.data.api.retrofit.ApiService
import com.kerdus.gqasir.data.api.retrofit.LoginRequest
import com.kerdus.gqasir.data.pref.UserModel
import com.kerdus.gqasir.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun login(username: String, password: String): LoginResponse {
        val request = LoginRequest(username, password)
        return apiService.login(request)
    }

    suspend fun getProduk(): List<ProdukResponseItem> {
        val token = userPreference.getSession().first().token
        return apiService.getProduk("Bearer $token")
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
