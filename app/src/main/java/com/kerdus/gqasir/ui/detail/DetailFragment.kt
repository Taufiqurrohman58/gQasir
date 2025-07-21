package com.kerdus.gqasir.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kerdus.gqasir.R
import com.kerdus.gqasir.data.api.response.ProdukResponseItem
import com.kerdus.gqasir.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
            findNavController().navigate(R.id.action_detailFragment_to_resultFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
