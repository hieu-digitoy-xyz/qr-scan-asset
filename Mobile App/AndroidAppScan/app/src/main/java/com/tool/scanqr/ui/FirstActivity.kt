package com.tool.scanqr.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marcinorlowski.fonty.Fonty
import com.tool.scanqr.barcode.BarcodeCaptureActivity
import com.tool.scanqr.databinding.ActivityFirstBinding
import com.tool.scanqr.utils.StoreDataHelper.Companion.getInstance

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Fonty.setFonts(this)



        binding.btnScan.setOnClickListener {
            scanQr()
        }

        binding.imgPremium.setOnClickListener {
            val timeLefts = getInstance().getDayLefts()
            val dateLeft: String = if (timeLefts > 1) {
                "$timeLefts days"
            } else {
                "0 day"
            }
            val content =
                "You are using Premium version\nYour time lefts: $dateLeft"
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val timeLefts = getInstance().getDayLefts()
        if (timeLefts > 0) {
            binding.imgPremium.visibility = View.VISIBLE
        } else {
            binding.imgPremium.visibility = View.GONE
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun scanQr() {
        val timeLefts = getInstance().getDayLefts()
        val intent: Intent
        if (timeLefts > 0) {
            intent = Intent(this, BarcodeCaptureActivity::class.java)
            startActivity(intent)
        } else {
            intent = Intent(this, SubsActivity::class.java)
            startActivity(intent)
        }
    }
}