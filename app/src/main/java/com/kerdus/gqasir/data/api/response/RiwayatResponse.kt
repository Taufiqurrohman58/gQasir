package com.kerdus.gqasir.data.api.response

import com.google.gson.annotations.SerializedName

data class RiwayatResponse(

	@field:SerializedName("RiwayatResponse")
	val riwayatResponse: List<RiwayatResponseItem>
)

data class RiwayatResponseItem(

	@field:SerializedName("transaction_id")
	val transactionId: Int,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("item_id")
	val itemId: Int,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("subtotal")
	val subtotal: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("item_name")
	val itemName: String
)
