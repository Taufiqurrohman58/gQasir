package com.kerdus.gqasir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: MutableList<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.txtItemName)
        val price = itemView.findViewById<TextView>(R.id.txtItemPrice)
        val menuButton = itemView.findViewById<ImageView>(R.id.btnMenu)

        init {
            itemView.setOnClickListener {
                onItemClick(products[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.item.name
        holder.price.text = product.item.price.toString()

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

        holder.menuButton.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_item_product)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onDelete(product)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = products.size

    // Tambahan: untuk mengganti seluruh data
    fun updateData(newList: List<Product>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }

    // Tambahan: untuk mengambil data yang sedang tampil
    fun getProducts(): List<Product> = products
}
