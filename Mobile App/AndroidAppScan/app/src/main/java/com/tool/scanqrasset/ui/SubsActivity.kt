package com.tool.scanqrasset.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.marcinorlowski.fonty.Fonty
import com.tool.scanqrasset.MainApplication
import com.tool.scanqrasset.R
import com.tool.scanqrasset.billing.SubscriptionManager
import com.tool.scanqrasset.databinding.ActivitySubsBinding

class SubsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubsBinding
    private var itemChoose: Int = 2
    private val subscriptionManager: SubscriptionManager= SubscriptionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Buy More"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


//        StoreDataHelper.getInstance().saveTimeLefts(13)
        //
        initAction()

        // set default item
        chooseItem(2)

        Fonty.setFonts(this)

        subscriptionManager.setupBillingClient(this)


    }

    private fun initAction() {
        binding.item1.setOnClickListener {
            chooseItem(1)
        }
        binding.item2.setOnClickListener {
            chooseItem(2)
        }
        binding.item3.setOnClickListener {
            chooseItem(3)
        }

        binding.btnBuy.setOnClickListener {
            if (subscriptionManager.isReady()) {
                subscriptionManager.initiatePurchase(itemChoose - 1)
            }
        }
    }

    private fun chooseItem(index: Int) {
        itemChoose = index
        when (index) {
            1 -> {
                binding.item1.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        R.color.orange
                    )
                )
                binding.item2.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
                binding.item3.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
            }
            2 -> {
                binding.item1.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
                binding.item2.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        R.color.orange
                    )
                )
                binding.item3.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
            }
            3 -> {
                binding.item1.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
                binding.item2.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        android.R.color.transparent
                    )
                )
                binding.item3.setCardBackgroundColor(
                    ContextCompat.getColor(
                        MainApplication.getInstance(),
                        R.color.orange
                    )
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}