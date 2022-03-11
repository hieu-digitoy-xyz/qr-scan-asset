package com.tool.scanqrasset.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.marcinorlowski.fonty.Fonty
import com.tool.scanqrasset.databinding.ActivityChooseOptionBinding

class ChooseOptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseOptionBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        Fonty.setFonts(this)

        binding.btnById.setOnClickListener {
            val intent = Intent(this, ScanQrCodeActivity::class.java)
            this@ChooseOptionActivity.startActivity(intent)
        }
        binding.btnByName.setOnClickListener {
            val intent = Intent(this, SearchAssetByNameActivity::class.java)
            this@ChooseOptionActivity.startActivity(intent)
        }
    }
}