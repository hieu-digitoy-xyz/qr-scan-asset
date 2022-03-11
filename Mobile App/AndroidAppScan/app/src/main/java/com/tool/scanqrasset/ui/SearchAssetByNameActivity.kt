package com.tool.scanqrasset.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcinorlowski.fonty.Fonty
import com.tool.scanqrasset.MainApplication
import com.tool.scanqrasset.adapter.AssetAdapter
import com.tool.scanqrasset.databinding.ActivitySearchAssetByNameBinding
import com.tool.scanqrasset.inf.FetchApiAction
import com.tool.scanqrasset.model.Asset
import com.tool.scanqrasset.utils.FetchApiHelper
import kotlinx.coroutines.CoroutineExceptionHandler

class SearchAssetByNameActivity : AppCompatActivity(), FetchApiAction {

    private lateinit var binding: ActivitySearchAssetByNameBinding
    private var adapter: AssetAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchAssetByNameBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                searchAssetByName(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        Fonty.setFonts(this)

        setupRv()

        binding.btnBack.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun searchAssetByName(name: String) {
        FetchApiHelper.getInstance()
            .fetchAssetsByNameApi(name, this, CoroutineExceptionHandler { _, throwable ->
                Toast.makeText(
                    MainApplication.getInstance().applicationContext,
                    throwable.message,
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding.lbMsg.visibility = View.VISIBLE

                binding.rv.visibility = View.GONE
            })
    }

    private fun setupRv() {
        adapter = AssetAdapter(listOf())
        binding.rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.rv.addItemDecoration(
//            DividerItemDecoration(
//                this,
//                LinearLayoutManager.VERTICAL
//            )
//        )
        binding.rv.setHasFixedSize(true)
        binding.rv.setItemViewCacheSize(20)

        binding.rv.adapter = adapter
//        binding.rv.isNestedScrollingEnabled = false
    }

    override fun doAction(value: Any) {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val listAsset = value as List<Asset>

        if (listAsset.isEmpty()) {
            binding.rv.visibility = View.GONE
            binding.lbMsg.visibility = View.VISIBLE
        } else {
            binding.rv.visibility = View.VISIBLE
            binding.lbMsg.visibility = View.GONE

            adapter!!.update(listAsset)
        }

    }
}