package com.kerdus.gqasir.ui.edit

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kerdus.gqasir.R
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.request.ProdukUpdateRequest
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import com.kerdus.gqasir.data.api.retrofit.ApiConfig
import com.kerdus.gqasir.data.pref.UserPreference
import com.kerdus.gqasir.data.pref.dataStore
import com.kerdus.gqasir.databinding.FragmentEditBinding
import com.kerdus.gqasir.databinding.FragmentEditgBinding
import com.kerdus.gqasir.ui.ViewModelFactory

class EditgFragment : Fragment() {

    private var _binding: FragmentEditgBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditgProdukViewModel
    private var produk: ProdukResponseItem? = null
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main2, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchButton)?.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        produk = arguments?.getParcelable("produk")

        val pref = UserPreference.getInstance(requireContext().dataStore)
        val apiService = ApiConfig.getApiService()
        val repository = Repository.getInstance(pref, apiService)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        )[EditgProdukViewModel::class.java]

        produk?.let { item ->
            binding.etNamaBarang.setText(item.name)

            binding.etStokGudang.setText(item.stockGudang.toString())
        }

        binding.btnSimpanEdit.setOnClickListener {
            println("🟢 Tombol Simpan diklik")

            val updated = ProdukUpdateRequest(
                name = binding.etNamaBarang.text.toString(),
                category = binding.etKategoriBarang.text.toString(),
                price = binding.etHarga.text.toString().toIntOrNull() ?: 0,
                stock_gudang = binding.etStokGudang.text.toString().toIntOrNull() ?: 0,
                stock_kantin = binding.etStokKantin.text.toString().toIntOrNull() ?: 0
            )

            println("📦 Data Update: $updated")

            produk?.let { produkItem ->
                println("🔧 ID Produk: ${produkItem.id}")
                viewModel.updateProduk(produkItem.id, updated)
            } ?: println("❌ Produk null, tidak bisa update")
        }



        viewModel.result.observe(viewLifecycleOwner) { updatedProduk ->
            if (updatedProduk != null && !hasNavigated) {
                hasNavigated = true
                Toast.makeText(requireContext(), "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editgFragment_to_homeFragment)
            } else if (!hasNavigated) {
                Toast.makeText(requireContext(), "Gagal memperbarui produk", Toast.LENGTH_SHORT).show()
            }
        }



        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
