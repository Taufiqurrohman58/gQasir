package com.kerdus.gqasir.ui.assigment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.response.LaporanBulananResponse
import com.kerdus.gqasir.data.api.response.LaporanHarianResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class AssigmentViewModel(private val repository: Repository) : ViewModel() {

    private val _laporanHarian = MutableLiveData<LaporanHarianResponse>()
    val laporanHarian: LiveData<LaporanHarianResponse> = _laporanHarian

    private val _laporanBulanan = MutableLiveData<LaporanBulananResponse>()
    val laporanBulanan: LiveData<LaporanBulananResponse> = _laporanBulanan

    private val _statusPengeluaran = MutableLiveData<Boolean>()
    val statusPengeluaran: LiveData<Boolean> = _statusPengeluaran

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getLaporanHarian(date: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val session = repository.getSession().first()
                val response = repository.getLaporanHarian(session.token, date)
                _laporanHarian.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun getLaporanBulanan(month: Int, year: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val session = repository.getSession().first()
                val response = repository.getLaporanBulanan(session.token, month, year)
                _laporanBulanan.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun tambahPengeluaran(description: String, amount: Int) {
        viewModelScope.launch {
            try {
                val session = repository.getSession().first()
                repository.tambahPengeluaran(session.token, description, amount)
                _statusPengeluaran.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _statusPengeluaran.value = false
            }
        }
    }
    private val _downloadedFile = MutableLiveData<ResponseBody>()
    val downloadedFile: LiveData<ResponseBody> = _downloadedFile

    fun downloadExcel(date: String) {
        viewModelScope.launch {
            try {
                val session = repository.getSession().first()
                val response = repository.downloadLaporanStok(session.token, date)
                _downloadedFile.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
