package com.kerdus.gqasir.data.api.response

import com.google.gson.annotations.SerializedName

data class LaporanBulananResponse(

	@field:SerializedName("pemasukan")
	val pemasukan: Int,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("pengeluaran")
	val pengeluaran: Int,

	@field:SerializedName("bulan")
	val bulan: String
)
