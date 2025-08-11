package com.kerdus.gqasir.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kerdus.gqasir.databinding.FragmentRiwayatBinding
import com.kerdus.gqasir.di.Injection
import com.kerdus.gqasir.ui.adapter.RiwayatAdapter
import com.kerdus.gqasir.ui.ViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager


class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RiwayatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        val repository = Injection.provideRepository(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[RiwayatViewModel::class.java]

        setupRecyclerView()
        observeData()

        viewModel.fetchRiwayat()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvRiwayat.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeData() {
        viewModel.riwayatList.observe(viewLifecycleOwner) { list ->
            binding.rvRiwayat.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

            val adapter = RiwayatAdapter(list) { transactionId ->
                viewModel.deleteTransaksi(transactionId) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Transaksi dibatalkan", Toast.LENGTH_SHORT).show()
                        viewModel.fetchRiwayat()
                    } else {
                        Toast.makeText(requireContext(), "Gagal membatalkan transaksi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.rvRiwayat.adapter = adapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.isError.observe(viewLifecycleOwner) { error ->
            if (error) {
                Toast.makeText(requireContext(), "Gagal memuat riwayat transaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
