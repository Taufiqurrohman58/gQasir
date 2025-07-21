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
import com.kerdus.gqasir.data.api.request.TransaksiRequest
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import com.kerdus.gqasir.data.api.retrofit.ApiConfig
import com.kerdus.gqasir.data.pref.UserPreference
import com.kerdus.gqasir.data.pref.dataStore
import com.kerdus.gqasir.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var quantity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Pindahkan ke sini
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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

        binding.btnPlusTerjual.setOnClickListener {
            quantity++
            binding.valueTerjual.text = quantity.toString()
        }

        binding.btnMinusTerjual.setOnClickListener {
            if (quantity > 0) {
                quantity--
                binding.valueTerjual.text = quantity.toString()
            }
        }


        produk?.let {
            binding.tvNamaBarang.text = it.name
            binding.valueStokKantin.text = it.stockKantin.toString()
            binding.valueHarga.text = "Rp ${it.price}"
        }

        // Navigasi ke EditFragment
        binding.btnEditt.setOnClickListener {
            produk?.let {
                val bundle = Bundle().apply {
                    putParcelable("produk", it)
                }
                findNavController().navigate(R.id.action_detailFragment_to_editFragment, bundle)
            }
        }



        // Tombol Simpan diklik, langsung navigasi ke ResultFragment
        binding.btnSimpan.setOnClickListener {
            produk?.let { item ->
                val request = TransaksiRequest(
                    item_id = item.id,
                    name = item.name,
                    quantity = quantity,
                    price = item.price
                )

                val repository = Repository.getInstance(
                    UserPreference.getInstance(requireContext().dataStore),
                    ApiConfig.getApiService()
                )
                Log.d("TransaksiRequest", "ID: ${item.id}, Name: ${item.name}, Price: ${item.price}, Quantity: $quantity")


                lifecycleScope.launch {
                    val success = repository.postTransaksi(request)
                    if (success) {
                        findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show()
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
