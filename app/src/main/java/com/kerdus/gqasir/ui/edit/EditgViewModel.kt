package com.kerdus.gqasir.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.request.ProdukUpdateRequest
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import kotlinx.coroutines.launch

class EditgProdukViewModel(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<ProdukResponseItem>()
    val result: LiveData<ProdukResponseItem> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun updateProduk(id: Int, request: ProdukUpdateRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updated = repository.updateProduk(id, request)
                _result.value = updated
                println("Update sukses: $updated")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Update gagal: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
