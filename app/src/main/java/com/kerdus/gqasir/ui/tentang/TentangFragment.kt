package com.kerdus.gqasir.ui.tentang

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kerdus.gqasir.R
import com.kerdus.gqasir.databinding.FragmentTentangBinding

class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null
    private val binding get() = _binding!!

    private val emailAddress = "taufiqurrohman250503@gmail.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        setupEmailText()
        setupEmailClick()

        binding.profileTaufiq.setOnClickListener {
            openLinkedIn("https://www.linkedin.com/in/taufiqurrohman03")
        }

        binding.profileRobby.setOnClickListener {
            openLinkedIn("https://www.linkedin.com/in/robby-wahyudi-a5b8b1203")
        }
        Glide.with(this)
            .load(R.drawable.taufiq) // gambar di drawable
            .circleCrop()
            .into(binding.imgTaufiq)

        Glide.with(this)
            .load(R.drawable.roby)
            .circleCrop()
            .into(binding.imgRobby)


        return binding.root
    }

    private fun openLinkedIn(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Tidak dapat membuka tautan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEmailText() {
        val label = "Email: "
        val fullText = label + emailAddress
        val spannable = SpannableString(fullText)

        // Warna biru dan underline untuk bagian alamat email saja
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            label.length,
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            UnderlineSpan(),
            label.length,
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Tampilkan teks ke TextView
        binding.emailTextView.text = spannable
    }

    private fun setupEmailClick() {
        binding.emailTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822" // khusus aplikasi email
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                putExtra(Intent.EXTRA_SUBJECT, "Dukungan Aplikasi GQasir")
            }

            try {
                startActivity(Intent.createChooser(intent, "Pilih Aplikasi Email"))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Tidak ada aplikasi email yang tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
