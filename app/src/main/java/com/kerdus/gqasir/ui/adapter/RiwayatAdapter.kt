package com.kerdus.gqasir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerdus.gqasir.data.api.response.RiwayatResponseItem
import com.kerdus.gqasir.databinding.ItemRiwayatBinding

class RiwayatAdapter(
    private val list: List<RiwayatResponseItem>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(val binding: ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiwayatViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvNamaProduk.text = item.itemName
            tvJumlah.text = "${item.quantity}x"
            tvHarga.text = "Rp ${item.price}"
            tvTotalHarga.text = "Total: Rp ${item.subtotal}"

            btnBatalkan.setOnClickListener {
                onDeleteClick(item.transactionId)
            }
        }
    }
}
