package com.tool.scanqrasset.billing

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.tool.scanqrasset.MainApplication
import com.tool.scanqrasset.ui.SubsActivity
import com.tool.scanqrasset.utils.Security
import com.tool.scanqrasset.utils.StoreDataHelper
import java.io.IOException


class SubscriptionManager : PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private var billingClient: BillingClient? = null
    private var listener: ConsumeResponseListener? = null
    private val listItem: Map<Int, SkuDetails>? = null
    private var activity: SubsActivity? = null

    fun isReady(): Boolean {
        return billingClient!!.isReady
    }

    fun setupBillingClient(activity: SubsActivity?) {
        this.activity = activity
        listener = ConsumeResponseListener { billingResult: BillingResult, s: String? ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    Toast.makeText(MainApplication.getInstance(), "Consume OK", Toast.LENGTH_SHORT).show();
            }
        }
        billingClient = BillingClientSetUp.getInstance(MainApplication.getInstance(), this)
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient!!.queryPurchasesAsync(
                        BillingClient.SkuType.SUBS
                    ) { billingResult, list -> getPurchaseList(list) }
//                    val queryPurchase = billingClient!!.queryPurchasesAsync(BillingClient.SkuType.SUBS, Quer)

                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    private fun getPurchaseList(list: List<Purchase>) {
        if (list.isNotEmpty()) {
            handlePurchases(list)
        }

        //check which items are in purchase list and which are not in purchase list
        //if items that are found add them to purchaseFound
        //check status of found items and save values to preference
        //item which are not found simply save false values to their preference
        //indexOf return index of item in purchase list from 0-2 (because we have 3 items) else returns -1 if not found
        val purchaseFound = ArrayList<Int>()
        if (list.isNotEmpty()) {
            //check item in purchase list
            for (p in list) {
                val index = subscribeItemIDs.indexOf(p.skus[0])
                //if purchase found
                if (index > -1) {
                    purchaseFound.add(index)
                    if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        saveSubscribeItemValueToPref(subscribeItemIDs[index], true)
                    } else {
                        saveSubscribeItemValueToPref(subscribeItemIDs[index], false)
                    }
                }
            }
            //items that are not found in purchase list mark false
            //indexOf returns -1 when item is not in foundlist
            for (i in subscribeItemIDs.indices) {
                if (purchaseFound.indexOf(i) == -1) {
                    saveSubscribeItemValueToPref(subscribeItemIDs[i], false)
                }
            }
        }
        //if purchase list is empty that means no item is not purchased/Subscribed
        //Or purchase is refunded or canceled
        //so mark them all false
        else {
            for (purchaseItem in subscribeItemIDs) {
                saveSubscribeItemValueToPref(purchaseItem, false)
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult =
                billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(MainApplication.getInstance(), "Purchase Canceled", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                MainApplication.getInstance(),
                "Error " + billingResult.debugMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

     fun initiatePurchase(index: Int) {

        val PRODUCT_ID = subscribeItemIDs[index]

        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        val billingResult = billingClient!!.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            billingClient!!.querySkuDetailsAsync(params.build()
            ) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (skuDetailsList != null && skuDetailsList.size > 0) {
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                        billingClient!!.launchBillingFlow(activity!!, flowParams)
                    }
                    else {
                        //try to add item/product id "s1" "s2" "s3" inside subscription in google play console
                        Toast.makeText(MainApplication.getInstance(), "Subscribe Item $PRODUCT_ID not Found", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(MainApplication.getInstance(),
                        " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(MainApplication.getInstance(),
                "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            val index = subscribeItemIDs.indexOf(purchase.skus[0])
            //purchase found
            if (index > -1) {
                //if item is purchased
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                        // Invalid purchase
                        // show error to user
                        Toast.makeText(
                            MainApplication.getInstance(),
                            "Error : Invalid Purchase",
                            Toast.LENGTH_SHORT
                        ).show()
                        continue  //skip current iteration only because other items in purchase list must be checked if present
                    }
                    // else purchase is valid
                    //if item is purchased/subscribed and not Acknowledged
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()
                        billingClient!!.acknowledgePurchase(
                            acknowledgePurchaseParams
                        ) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                //if purchase is acknowledged
                                //then saved value in preference
                                saveSubscribeItemValueToPref(subscribeItemIDs[index], true)
                                Toast.makeText(
                                    MainApplication.getInstance(),
                                    subscribeItemIDs[index] + " Item Subscribed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                notifyList()
                            }
                        }
                    } else {
                        // Grant entitlement to the user on item purchase
                        if (!getSubscribeItemValueFromPref(subscribeItemIDs[index])) {
                            saveSubscribeItemValueToPref(subscribeItemIDs[index], true)
                            Toast.makeText(
                                MainApplication.getInstance(),
                                subscribeItemIDs[index] + " Item Subscribed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            notifyList()
                        }
                    }


                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Toast.makeText(
                        MainApplication.getInstance(),
                        subscribeItemIDs[index] + " Purchase is Pending. Please complete Transaction",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    //mark purchase false in case of UNSPECIFIED_STATE
                    saveSubscribeItemValueToPref(subscribeItemIDs[index], false)
                    Toast.makeText(
                        MainApplication.getInstance(),
                        subscribeItemIDs[index] + " Purchase Status Unknown",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyList()
                }
            }
        }
    }

    private fun notifyList() {
        subscribeItemDisplay.clear()
        for (p in subscribeItemIDs) {
            subscribeItemDisplay.add(
                "Subscribe Status of " + p + " = " + getSubscribeItemValueFromPref(
                    p
                )
            )
        }
        // arrayAdapter!!.notifyDataSetChanged()
    }

    private val preferenceObject: SharedPreferences
        private get() = MainApplication.getInstance().getSharedPreferences(PREF_FILE, 0)
    private val preferenceEditObject: SharedPreferences.Editor
        private get() {
            val pref = MainApplication.getInstance().getSharedPreferences(PREF_FILE, 0)
            return pref.edit()
        }

    private fun getSubscribeItemValueFromPref(PURCHASE_KEY: String): Boolean {
        return preferenceObject.getBoolean(PURCHASE_KEY, false)
    }

    private fun saveSubscribeItemValueToPref(PURCHASE_KEY: String, value: Boolean) {
        preferenceEditObject.putBoolean(PURCHASE_KEY, value).commit()

        if (value) {
            when(subscribeItemIDs.indexOf(PURCHASE_KEY)) {
                0 -> StoreDataHelper.getInstance().addTimeLefts(30)
                1 -> StoreDataHelper.getInstance().addTimeLefts(365)
                2 -> StoreDataHelper.getInstance().addTimeLefts(90)
            }
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            //for old play console
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            //for new play console
            //To get key go to Developer Console > Select your app > Monetize > Monetization setup
            val base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjMl72tScH5xpPcyuvOVoJjDNUQbDdSfTeuMLmTlgnNPB0QazKPIlLk+BWaRvSTXwQWMHDDW7pdnadVXd/scnaLhNuJiF/4HV+6uXg3UByCpqkUvcWK2wiJL28pCxYpxCNMj7zz2P2nyAXlRFQxkITtBEucsCYkIzw89bqLkzuMw9dagiOSc9qI0C5a+oGu7R7TnGwtiH5POu4fjx1iWULcc2TUkEUbPFPoG8cVLOV4oDGMAVl1mU01sNViu3huSI9xnMQmI2qz7mA1sQZvMPywLC1sSMh6rRX+fRzQHp3IvxePIAxUIXJwAw1I6/m0sxn3ufWs6wXnvw8ixYVJTXqwIDAQAB"
            Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }

    companion object {
        const val PREF_FILE = "MyPref"

        //note add unique product ids
        //use same id for preference key
        private val subscribeItemIDs: ArrayList<String> = object : ArrayList<String>() {
            init {
                add("sub_item_small")
                add("sub_item_large")
                add("sub_item_medium")
            }
        }
        private val subscribeItemDisplay = ArrayList<String>()
    }

    override fun onPurchaseHistoryResponse(
        p0: BillingResult,
        p1: MutableList<PurchaseHistoryRecord>?
    ) {
        Log.d("history", p1?.size.toString())
    }
}