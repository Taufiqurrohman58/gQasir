package com.kerdus.gqasir.ui.home

import android.os.Bundle
import android.view.*

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProdukViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Penting agar onCreateOptionsMenu dipanggil
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main2, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchButton)?.isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Repository dan ViewModel
        val pref = UserPreference.getInstance(requireContext().dataStore)
        val apiService = ApiConfig.getApiService()

        val repository = Repository.getInstance(pref, apiService)
        viewModel = ViewModelProvider(
            this,
            ProdukViewModelFactory(repository)
        )[ProdukViewModel::class.java]

        // Setup Adapter dan RecyclerView
        adapter = ProductAdapter(
            mutableListOf(),
            onItemClick = {
                findNavController().navigate(R.id.action_HomeFragment_to_DetailFragment)
            },
            onDelete = { product ->
                // Hapus item dari adapter
                val currentList = adapter.getProducts().toMutableList()
                currentList.remove(product)
                adapter.updateData(currentList)
            }
        )
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter

        // Ambil data produk dari API
        viewModel.loadProduk()

        viewModel.produk.observe(viewLifecycleOwner) { produkList ->
            val converted = produkList.map {
                Product(it.name, "Rp. ${it.price}")
            }
            adapter.updateData(converted)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


        // Tombol tambah produk
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
