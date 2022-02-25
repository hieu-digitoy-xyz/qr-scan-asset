package com.tool.scanqr.billing;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.tool.scanqr.MainApplication;
import com.tool.scanqr.ui.SubsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BillingManager implements PurchasesUpdatedListener, PurchaseHistoryResponseListener {

    private BillingClient billingClient;
    private ConsumeResponseListener listener;
    private Map<Integer, SkuDetails> listItem;
    private SubsActivity activity;

    public void setupBillingClient(SubsActivity activity) {
        this.activity = activity;

        listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Toast.makeText(MainApplication.getInstance(), "Consume OK", Toast.LENGTH_SHORT).show();
            }
        };

        billingClient = BillingClientSetUp.getInstance(MainApplication.getInstance(), this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Toast.makeText(MainApplication.getInstance(), "Success to connect OK", Toast.LENGTH_SHORT).show();
                    // Query

//                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
//                            .getPurchasesList();
//                    handleItemAlreadyPurchase(purchases);

                    // load product
                    loadProduct();
                } else {
//                    Toast.makeText(MainApplication.getInstance(), "Error code: " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
//                Toast.makeText(MainApplication.getInstance(), "Disconnect OK", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleItemAlreadyPurchase(List<Purchase> purchases) {
//        StringBuilder purchasedItem = new StringBuilder();
        for (Purchase purchase : purchases) {
//            purchasedItem.append(" " + purchase.getSku());

            ConsumeParams params = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();
            billingClient.consumeAsync(params, listener);

            if (activity != null) {
//                activity.buyItem(purchase);
            }
        }
//        Toast.makeText(MainApplication.getInstance(), "Item (handler): " + purchasedItem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//            StringBuilder purchasedItem = new StringBuilder();
//            assert list != null;
//            for (Purchase purchase : list) {
//                purchasedItem.append(" " + purchase.getSku());
//
//                ConsumeParams params = ConsumeParams.newBuilder()
//                        .setPurchaseToken(purchase.getPurchaseToken())
//                        .build();
//                billingClient.consumeAsync(params, listener);
//            }
//            Toast.makeText(MainApplication.getInstance(), "Item (updated): " + purchasedItem, Toast.LENGTH_SHORT).show();

            assert list != null;
            handleItemAlreadyPurchase(list);
        }
    }


    public void loadProduct() {
        if (billingClient.isReady()) {
            ArrayList<String> productIds = new ArrayList<>();
//            productIds.add(MainApplication.getInstance().getResources().getString(R.string.item_one_year));
//            productIds.add(MainApplication.getInstance().getResources().getString(R.string.item_six_months));
//            productIds.add(MainApplication.getInstance().getResources().getString(R.string.item_three_months));
//            productIds.add(MainApplication.getInstance().getResources().getString(R.string.item_one_month));

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(productIds)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if (listItem == null) {
                        listItem = new HashMap<>();
                    } else {
                        listItem.clear();
                    }
                    //
                    assert list != null;
                    for (SkuDetails detail : list) {
//                        if (detail.getSku().equals(MainApplication.getInstance().getResources().getString(R.string.item_one_month))) {
//                            listItem.put(4, detail);
//                        } else if (detail.getSku().equals(MainApplication.getInstance().getResources().getString(R.string.item_three_months))) {
//                            listItem.put(3, detail);
//                        } else if (detail.getSku().equals(MainApplication.getInstance().getResources().getString(R.string.item_six_months))) {
//                            listItem.put(2, detail);
//                        } else if (detail.getSku().equals(MainApplication.getInstance().getResources().getString(R.string.item_one_year))) {
//                            listItem.put(1, detail);
//                        }
                    }

//                    listItem = list;
                } else {
                    Toast.makeText(MainApplication.getInstance(), "Error code: " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            });

            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this);
        }
    }

    public void purchaseItem(AppCompatActivity appCompatActivity, int position) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(Objects.requireNonNull(listItem.get(position)))
                .build();
        int responseCode = billingClient.launchBillingFlow(appCompatActivity, billingFlowParams)
                .getResponseCode();
        switch (responseCode) {
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                Toast.makeText(MainApplication.getInstance(), "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Toast.makeText(MainApplication.getInstance(), "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                Toast.makeText(MainApplication.getInstance(), "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Toast.makeText(MainApplication.getInstance(), "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                Toast.makeText(MainApplication.getInstance(), "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                Toast.makeText(MainApplication.getInstance(), "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                Toast.makeText(MainApplication.getInstance(), "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                Toast.makeText(MainApplication.getInstance(), "ITEM_NOT_OWNED", Toast.LENGTH_SHORT).show();
            case BillingClient.BillingResponseCode.OK:
                break;
            default:
                Toast.makeText(MainApplication.getInstance(), "Something wrong - " + responseCode, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPurchaseHistoryResponse(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<PurchaseHistoryRecord> list) {
        Log.e("BillingResult", billingResult.toString());
//        Log.e("list", list.toString());

//        PurchaseHistoryRecord record = list.get(0);
//        record.
    }
}
