package com.kerdus.gqasir.ui.home

import androidx.lifecycle.*
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import kotlinx.coroutines.launch

class ProdukViewModel(private val repository: Repository) : ViewModel() {
    private val _produk = MutableLiveData<List<ProdukResponseItem>>()
    val produk: LiveData<List<ProdukResponseItem>> = _produk

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadProduk(category: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allProduk = repository.getProduk()
                _produk.value = if (category.isNullOrEmpty() || category == "all") {
                    allProduk
                } else {
                    allProduk.filter { it.category.equals(category, ignoreCase = true) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}

class ProdukViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdukViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProdukViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}