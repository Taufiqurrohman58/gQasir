package com.kerdus.gqasir.ui.add

import androidx.lifecycle.ViewModel
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.request.ProdukCreateRequest

class AddViewModel(private val repository: Repository) : ViewModel() {
    suspend fun postProduk(request: ProdukCreateRequest): Boolean {
        return repository.postProduk(request)
    }
}
