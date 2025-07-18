package com.kerdus.gqasir.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerdus.gqasir.Event
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loginResult = MutableLiveData<Event<String>>() // for token/message
    val loginResult: LiveData<Event<String>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(username, password)

                val userModel = UserModel(
                    username = username,
                    token = response.token,
                    isLogin = true
                )
                repository.saveSession(userModel)

                _loginResult.value = Event(response.message)
            } catch (e: Exception) {
                _errorMessage.value = Event(e.message ?: "Terjadi kesalahan")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
