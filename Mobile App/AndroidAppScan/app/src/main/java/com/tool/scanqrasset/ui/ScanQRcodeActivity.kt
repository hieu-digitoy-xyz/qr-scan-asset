package com.tool.scanqrasset.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.marcinorlowski.fonty.Fonty
import com.tool.scanqrasset.MainApplication
import com.tool.scanqrasset.R
import com.tool.scanqrasset.barcode.BarcodeCaptureActivity
import com.tool.scanqrasset.databinding.ScreenScanQrCodeBinding
import com.tool.scanqrasset.inf.FetchApiAction
import com.tool.scanqrasset.model.Asset
import com.tool.scanqrasset.utils.ClipboardUtils
import com.tool.scanqrasset.utils.FetchApiHelper
import com.tool.scanqrasset.utils.StoreDataHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.URL

class ScanQRcodeActivity : AppCompatActivity(), FetchApiAction {
    var searchView: SearchView? = null
    var tvTimeLefts: TextView? = null
    var btnBuy: Button? = null
    var imgCopy: ImageView? = null
    var imgOpenLink: ImageView? = null
    var imgShare: ImageView? = null
    var imgBack: ImageView? = null
    var imgPremium: ImageView? = null
    var imgScan: ImageView? = null
    var bgScan: CardView? = null
    private val BARCODE_READER_REQUEST_CODE = 1
    private var binding: ScreenScanQrCodeBinding? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private var mActivityActionBarToolbar: Toolbar? = null
    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        mActivityActionBarToolbar = toolbar
    }

    override fun setTitle(title: CharSequence) {
        val ab = supportActionBar
        if (ab != null) {
            ab.title = title
            Fonty.setFonts(mActivityActionBarToolbar)
        }
    }

    fun setSubtitle(subtitle: CharSequence?) {
        val ab = supportActionBar
        if (ab != null) {
            ab.subtitle = subtitle
            Fonty.setFonts(mActivityActionBarToolbar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenScanQrCodeBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        searchView = findViewById(R.id.searchView)
        tvTimeLefts = findViewById(R.id.tvTimeLefts)
        btnBuy = findViewById(R.id.btnBuy)
        imgCopy = findViewById(R.id.imgCopy)
        imgShare = findViewById(R.id.imgShare)
        imgOpenLink = findViewById(R.id.imgOpen)
        bgScan = findViewById(R.id.btnScan)
        imgBack = findViewById(R.id.btnBack)
        imgPremium = findViewById(R.id.imgPremium)
        imgScan = findViewById(R.id.imgScan)

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        imgScan!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, BarcodeCaptureActivity::class.java)
            this@ScanQRcodeActivity.startActivity(intent)
        })
        imgCopy!!.setOnClickListener(View.OnClickListener { v: View? -> copyText() })
        imgOpenLink!!.setOnClickListener(View.OnClickListener { v: View? -> openLink() })
        imgShare!!.setOnClickListener(View.OnClickListener { v: View? -> shareText() })

//        bgScan.setOnClickListener(v -> {
//            scanQr();
//        });
        imgBack!!.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        imgPremium!!.setOnClickListener(View.OnClickListener { v: View? ->
            val timeLefts = StoreDataHelper.getInstance().getDayLefts()
            val dateLeft: String
            dateLeft = if (timeLefts > 1) {
                "$timeLefts days"
            } else {
                "0 day"
            }
            val content = "You are using Premium version\nYour time lefts: $dateLeft"
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        })

        // search view
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                searchAssetById(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        Fonty.setFonts(this)

        binding!!.bgInfo.visibility = View.GONE

//        setTitle("QR Scan");
        try {
            if (intent != null) {
                val barcode = intent.getParcelableExtra<Barcode>("Barcode")
                if (barcode != null) {
                    binding!!.searchView.setQuery(barcode.displayValue, true)
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun searchAssetById(id: String) {
        FetchApiHelper.getInstance()
            .fetchAssetsApi(id, this, CoroutineExceptionHandler { _, throwable ->
                Toast.makeText(
                    MainApplication.getInstance().applicationContext,
                    throwable.message,
                    Toast.LENGTH_SHORT
                )
                    .show()

                binding!!.bgInfo.visibility = View.GONE

                binding!!.bgMsg.visibility = View.VISIBLE
                binding!!.lbMsg.text = "Not found ID: $id";
            })
    }

    override fun onResume() {
        super.onResume()
        val dateLeft = StoreDataHelper.getInstance().getDayLefts()
        tvTimeLefts!!.text = "Your date lefts: " + if (dateLeft > 0) dateLeft else 0
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                try {
                    if (data != null) {
                        val barcode = data.getParcelableExtra<Barcode>("Barcode")
                        if (barcode != null) {
                            searchView!!.setQuery(barcode.displayValue, true)
                        }
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun scanQr() {
        val timeLefts = StoreDataHelper.getInstance().getDayLefts()
        val intent: Intent = if (timeLefts > 0) {
            Intent(this, BarcodeCaptureActivity::class.java)
        } else {
            Intent(this, SubsActivity::class.java)
        }
        startActivity(intent)
    }

    private fun copyText() {
        if (searchView!!.query != null) {
            if (ClipboardUtils.get(MainApplication.getInstance())
                    .copy(searchView!!.query.toString())
            ) {
                Toast.makeText(this, "Copy Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Copy Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareText() {
        if (searchView!!.query != null) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val content = searchView!!.query.toString()
            shareIntent.putExtra(Intent.EXTRA_TEXT, content)
            startActivity(
                Intent.createChooser(shareIntent, "Share via")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun openLink() {
        if (searchView!!.query != null) {
            val text = searchView!!.query.toString()
            try {
                val u = URL(text)
                u.toURI()
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
                startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun doAction(value: Any) {
        // Only runs if there is a view that is currently focused
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val asset  = value as Asset

        binding!!.bgInfo.visibility = View.VISIBLE
        binding!!.bgMsg.visibility = View.GONE

        binding!!.tvPartId.text = asset.partId
        binding!!.tvDescription.text = asset.description
        binding!!.tvUseFor.text = asset.useFor
        binding!!.tvStorageBin.text = asset.storageBin
        binding!!.tvStockAmount.text = asset.stockAmount.toString()
        binding!!.tvSearch.text = asset.search
        binding!!.tvBrand.text = asset.brand
        binding!!.cbLocal.isChecked = asset.local
        binding!!.cbImport.isChecked = asset.import
        binding!!.tvNote.text = asset.note
        binding!!.tvAlp.text = asset.alp
        binding!!.tvSupplier.text = asset.supplier
    }
}