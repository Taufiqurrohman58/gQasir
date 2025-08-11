package com.kerdus.gqasir.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kerdus.gqasir.R
import com.kerdus.gqasir.data.Repository
import com.kerdus.gqasir.data.api.request.StokRequest
import com.kerdus.gqasir.data.api.request.TransaksiRequest
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import com.kerdus.gqasir.data.api.retrofit.ApiConfig
import com.kerdus.gqasir.data.pref.UserPreference
import com.kerdus.gqasir.data.pref.dataStore
import com.kerdus.gqasir.databinding.FragmentDetailgBinding
import kotlinx.coroutines.launch


class DetailgFragment : Fragment() {

    private var _binding: FragmentDetailgBinding? = null
    private val binding get() = _binding!!
    private var quantity = 0
    private var gudangQty = 0
    private var kantinQty = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchButton)?.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main2, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari argument
        val produk = arguments?.getParcelable<ProdukResponseItem>("produk")

        binding.valueTerjual.text = quantity.toString()

        // Tambah stok Gudang
        binding.btnPlusTerjual.setOnClickListener {
            gudangQty++
            binding.valueTerjual.text = gudangQty.toString()
        }
        binding.btnMinusTerjual.setOnClickListener {
            if (gudangQty > 0) {
                gudangQty--
                binding.valueTerjual.text = gudangQty.toString()
            }
        }

// Tambah stok Kantin
        binding.btnPlusTerjualg.setOnClickListener {
            kantinQty++
            binding.valueTerjualg.text = kantinQty.toString()
        }
        binding.btnMinusTerjualg.setOnClickListener {
            if (kantinQty > 0) {
                kantinQty--
                binding.valueTerjualg.text = kantinQty.toString()
            }
        }



        produk?.let {
            binding.tvNamaBarang.text = it.name
            binding.valueStokGudang.text = it.stockGudang.toString()
        }

        // Navigasi ke EditFragment
        binding.btnEditt.setOnClickListener {
            produk?.let {
                val bundle = Bundle().apply {
                    putParcelable("produk", it)
                }
                findNavController().navigate(R.id.action_detailgFragment_to_editgFragment, bundle)
            }
        }

        binding.btnSimpan.setOnClickListener {
            produk?.let { item ->
                val repository = Repository.getInstance(
                    UserPreference.getInstance(requireContext().dataStore),
                    ApiConfig.getApiService()
                )

                lifecycleScope.launch {
                    val stokGudangSuccess = if (gudangQty > 0) {
                        repository.tambahStokGudang(StokRequest(item.id, gudangQty))
                    } else true

                    val stokKantinSuccess = if (kantinQty > 0) {
                        repository.transferStokKantin(StokRequest(item.id, kantinQty))
                    } else true

                    if (stokGudangSuccess && stokKantinSuccess) {
                        Toast.makeText(requireContext(), "Berhasil memperbarui stok", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_detailgFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), "Gagal memperbarui stok", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
