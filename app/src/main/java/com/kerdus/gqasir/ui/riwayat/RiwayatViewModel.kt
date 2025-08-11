package com.kerdus.gqasir.ui.riwayat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.response.RiwayatResponseItem
import kotlinx.coroutines.launch

class RiwayatViewModel(private val repository: Repository) : ViewModel() {

    private val _riwayatList = MutableLiveData<List<RiwayatResponseItem>>()
    val riwayatList: LiveData<List<RiwayatResponseItem>> = _riwayatList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError


    fun fetchRiwayat() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _riwayatList.value = repository.getRiwayatTransaksi()
                _isError.value = false
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun deleteTransaksi(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.deleteTransaksi(id)
            onResult(result)
        }
    }
}
