package com.kerdus.gqasir.ui.assigment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kerdus.gqasir.R
import com.kerdus.gqasir.di.Injection
import com.kerdus.gqasir.ui.ViewModelFactory
import okhttp3.ResponseBody
import java.util.Date
import okio.buffer
import okio.sink

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class AssigmentFragment : Fragment() {

    private lateinit var viewModel: AssigmentViewModel

    // Input fields
    private lateinit var dateInput: EditText
    private lateinit var monthInput: EditText
    private lateinit var yearInput: EditText
    private lateinit var pengeluaranDesc: EditText
    private lateinit var pengeluaranAmount: EditText
    private lateinit var dateInputExcel: EditText

    // Buttons
    private lateinit var downloadButton: Button
    private lateinit var showHarianButton: Button
    private lateinit var showBulananButton: Button
    private lateinit var savePengeluaranButton: Button

    // Output TextViews
    private lateinit var valuePemasukan: TextView
    private lateinit var valuePengeluaran: TextView
    private lateinit var valueTotalPendapatan: TextView
    private lateinit var valuePemasukanBulanan: TextView
    private lateinit var valuePengeluaranBulanan: TextView
    private lateinit var valueTotalPendapatanBulanan: TextView

    private val POST_NOTIF_REQUEST_CODE = 100
    private val WRITE_EXTERNAL_REQUEST_CODE = 101
    private var currentDownloadDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_assigment, container, false)

        val repository = Injection.provideRepository(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[AssigmentViewModel::class.java]

        // Bind inputs
        dateInput = view.findViewById(R.id.inputDate)
        monthInput = view.findViewById(R.id.inputMonth)
        yearInput = view.findViewById(R.id.inputYear)
        pengeluaranDesc = view.findViewById(R.id.inputPengeluaranDesc)
        pengeluaranAmount = view.findViewById(R.id.inputPengeluaranAmount)
        dateInputExcel = view.findViewById(R.id.inputDateExel)

        // Bind buttons
        downloadButton = view.findViewById(R.id.btnDownloadExcel)
        showHarianButton = view.findViewById(R.id.btnShowHarian)
        showBulananButton = view.findViewById(R.id.btnShowBulanan)
        savePengeluaranButton = view.findViewById(R.id.btnSimpanPengeluaran)

        // Bind outputs
        valuePemasukan = view.findViewById(R.id.valuePemasukan)
        valuePengeluaran = view.findViewById(R.id.valuePengeluaran)
        valueTotalPendapatan = view.findViewById(R.id.valueTotalPendapatan)
        valuePemasukanBulanan = view.findViewById(R.id.valuePemasukanBulanan)
        valuePengeluaranBulanan = view.findViewById(R.id.valuePengeluaranBulanan)
        valueTotalPendapatanBulanan = view.findViewById(R.id.valueTotalPendapatanBulanan)

        // Tombol lihat harian
        showHarianButton.setOnClickListener {
            val date = dateInput.text.toString()
            if (date.isNotEmpty()) {
                viewModel.getLaporanHarian(date)
            } else {
                Toast.makeText(requireContext(), "Tanggal harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol lihat bulanan
        showBulananButton.setOnClickListener {
            val month = monthInput.text.toString().toIntOrNull()
            val year = yearInput.text.toString().toIntOrNull()
            if (month != null && year != null) {
                viewModel.getLaporanBulanan(month, year)
            } else {
                Toast.makeText(requireContext(), "Bulan dan tahun harus valid", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol simpan pengeluaran
        savePengeluaranButton.setOnClickListener {
            val desc = pengeluaranDesc.text.toString()
            val amount = pengeluaranAmount.text.toString().toIntOrNull()
            if (desc.isNotEmpty() && amount != null) {
                viewModel.tambahPengeluaran(desc, amount)
            } else {
                Toast.makeText(requireContext(), "Isi deskripsi dan jumlah pengeluaran", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol download Excel
        downloadButton.setOnClickListener {
            val date = dateInputExcel.text.toString()
            if (date.isEmpty()) {
                Toast.makeText(requireContext(), "Tanggal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentDownloadDate = date

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_REQUEST_CODE)
                    return@setOnClickListener
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIF_REQUEST_CODE)
                    return@setOnClickListener
                }
            }

            viewModel.downloadExcel(date)
        }

        observeViewModel()
        return view
    }

    private fun observeViewModel() {
        viewModel.laporanHarian.observe(viewLifecycleOwner) {
            valuePemasukan.text = it.pemasukan.toString()
            valuePengeluaran.text = it.pengeluaran.toString()
            valueTotalPendapatan.text = it.total.toString()
        }

        viewModel.laporanBulanan.observe(viewLifecycleOwner) {
            valuePemasukanBulanan.text = it.pemasukan.toString()
            valuePengeluaranBulanan.text = it.pengeluaran.toString()
            valueTotalPendapatanBulanan.text = it.total.toString()
        }

        viewModel.statusPengeluaran.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Pengeluaran berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.downloadedFile.observe(viewLifecycleOwner) { responseBody ->
            responseBody?.let {
                saveToFile(it, currentDownloadDate)
            }
        }

    }

    private fun saveToFile(responseBody: ResponseBody, fileName: String) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Tambahkan timestamp agar nama file unik
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val uniqueFileName = "rekap_stok_${fileName}_$timestamp.xlsx"

            val file = File(downloadsDir, uniqueFileName)

            // Gunakan extension function `.sink()` dan `.buffer()`
            file.sink().buffer().use { sink ->
                sink.writeAll(responseBody.source())
            }

            // Scan file agar muncul di File Manager
            MediaScannerConnection.scanFile(requireContext(), arrayOf(file.absolutePath), null, null)

            Toast.makeText(requireContext(), "File berhasil disimpan: ${file.name}", Toast.LENGTH_LONG).show()
            showDownloadNotification(file.name, file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Gagal menyimpan file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }




    private fun showDownloadNotification(title: String, filePath: String) {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "download_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Unduhan", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.lgqasir)
            .setContentTitle("Download selesai")
            .setContentText("File: $title")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == POST_NOTIF_REQUEST_CODE || requestCode == WRITE_EXTERNAL_REQUEST_CODE)
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.downloadExcel(currentDownloadDate)
        } else {
            Toast.makeText(requireContext(), "Izin diperlukan untuk melanjutkan", Toast.LENGTH_SHORT).show()
        }
    }
}
