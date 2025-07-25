package com.kerdus.gqasir.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerdus.gqasir.R
import com.kerdus.gqasir.Product
import com.kerdus.gqasir.ProductAdapter
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.retrofit.ApiConfig
import com.kerdus.gqasir.data.pref.UserPreference
import com.kerdus.gqasir.data.pref.dataStore
import com.kerdus.gqasir.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProdukViewModel
    private var selectedCategory: String = "all"
    private var fullProductList: List<Product> = emptyList() // Data mentah untuk pencarian

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreference.getInstance(requireContext().dataStore)
        val apiService = ApiConfig.getApiService()
        val repository = Repository.getInstance(pref, apiService)
        viewModel = ViewModelProvider(
            this,
            ProdukViewModelFactory(repository)
        )[ProdukViewModel::class.java]

        adapter = ProductAdapter(
            mutableListOf(),
            onItemClick = { product ->
                val bundle = Bundle().apply {
                    putParcelable("produk", product.item)
                }
                findNavController().navigate(R.id.action_HomeFragment_to_DetailFragment, bundle)
            },

            onDelete = { product ->
                val id = product.item.id ?: return@ProductAdapter
                viewModel.deleteProduk(id) { success ->
                    if (success) {
                        val currentList = adapter.getProducts().toMutableList()
                        currentList.remove(product)
                        adapter.updateData(currentList)
                    } else {
                        Toast.makeText(requireContext(), "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        )

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter

        viewModel.produk.observe(viewLifecycleOwner) { produkList ->
            val converted = produkList.map { Product(it) }
            fullProductList = converted
            adapter.updateData(converted)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Tombol kategori
        binding.btnAll.setOnClickListener {
            selectedCategory = "all"
            updateCategoryButtons(binding.btnAll)
            viewModel.loadProduk()
        }

        binding.btnMakanan.setOnClickListener {
            selectedCategory = "Makanan"
            updateCategoryButtons(binding.btnMakanan)
            viewModel.loadProduk("Makanan")
        }

        binding.btnMinuman.setOnClickListener {
            selectedCategory = "Minuman"
            updateCategoryButtons(binding.btnMinuman)
            viewModel.loadProduk("Minuman")
        }

        binding.btnSnack.setOnClickListener {
            selectedCategory = "Snack"
            updateCategoryButtons(binding.btnSnack)
            viewModel.loadProduk("Snack")
        }

        binding.btnPopmie.setOnClickListener {
            selectedCategory = "PopMie"
            updateCategoryButtons(binding.btnPopmie)
            viewModel.loadProduk("PopMie")
        }

        binding.btnTiket.setOnClickListener {
            selectedCategory = "Tiket"
            updateCategoryButtons(binding.btnTiket)
            viewModel.loadProduk("Tiket")
        }

        // Set default kategori
        updateCategoryButtons(binding.btnAll)
        viewModel.loadProduk()

        // Tambah produk
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    private fun updateCategoryButtons(selected: Button) {
        val buttons = listOf(
            binding.btnAll,
            binding.btnMakanan,
            binding.btnMinuman,
            binding.btnSnack,
            binding.btnPopmie,
            binding.btnTiket
        )
        buttons.forEach { btn ->
            btn.setBackgroundColor(resources.getColor(R.color.gray_button, null))
        }
        selected.setBackgroundColor(resources.getColor(R.color.blue_button, null))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main2, menu)

        val searchItem = menu.findItem(R.id.searchButton)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Cari produk..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // tidak perlu aksi submit
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = if (!newText.isNullOrEmpty()) {
                    fullProductList.filter {
                        it.item.name.contains(newText, ignoreCase = true)
                    }
                } else {
                    fullProductList
                }
                adapter.updateData(filtered)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchButton)?.isVisible = true // tampilkan tombol search
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}