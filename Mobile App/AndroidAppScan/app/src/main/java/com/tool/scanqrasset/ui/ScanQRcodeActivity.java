package com.tool.scanqrasset.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.marcinorlowski.fonty.Fonty;
import com.tool.scanqrasset.MainApplication;
import com.tool.scanqrasset.R;
import com.tool.scanqrasset.barcode.BarcodeCaptureActivity;
import com.tool.scanqrasset.utils.ClipboardUtils;
import com.tool.scanqrasset.utils.StoreDataHelper;

import java.net.URL;

public class ScanQRcodeActivity extends AppCompatActivity {
    SearchView searchView;
    TextView tvTimeLefts;
    Button btnBuy;
    ImageView imgCopy, imgOpenLink, imgShare, imgBack, imgPremium, imgScan;
    CardView bgScan;
    int BARCODE_READER_REQUEST_CODE = 1;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Toolbar mActivityActionBarToolbar;

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mActivityActionBarToolbar = toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            Fonty.setFonts(mActivityActionBarToolbar);
        }
    }

    public void setSubtitle(CharSequence subtitle) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setSubtitle(subtitle);
            Fonty.setFonts(mActivityActionBarToolbar);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_scan_qr_code);


        searchView = findViewById(R.id.searchView);
        tvTimeLefts = findViewById(R.id.tvTimeLefts);
        btnBuy = findViewById(R.id.btnBuy);
        imgCopy = findViewById(R.id.imgCopy);
        imgShare = findViewById(R.id.imgShare);
        imgOpenLink = findViewById(R.id.imgOpen);
        bgScan = findViewById(R.id.btnScan);
        imgBack = findViewById(R.id.btnBack);
        imgPremium = findViewById(R.id.imgPremium);
        imgScan = findViewById(R.id.imgScan);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        imgScan.setOnClickListener(v -> {
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            ScanQRcodeActivity.this.startActivity(intent);
        });

        imgCopy.setOnClickListener(v -> {
            copyText();
        });

        imgOpenLink.setOnClickListener(v -> {
            openLink();
        });

        imgShare.setOnClickListener(v -> {
            shareText();
        });

//        bgScan.setOnClickListener(v -> {
//            scanQr();
//        });

        imgBack.setOnClickListener(v -> {
            finish();
        });

        imgPremium.setOnClickListener(v -> {
            int timeLefts = StoreDataHelper.getInstance().getDayLefts();
            String dateLeft;
            if (timeLefts > 1) {
                dateLeft = timeLefts + " days";
            } else {
                dateLeft = "0 day";
            }
            String content = "You are using Premium version\nYour time lefts: " + dateLeft;

            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, SubsActivity.class);
//            startActivity(intent);
        });

        // search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        Fonty.setFonts(this);

//        setTitle("QR Scan");

        try {
            if (getIntent() != null) {
                Barcode barcode = getIntent().getParcelableExtra("Barcode");
                if (barcode != null) {
                    searchView.setQuery(barcode.displayValue, true);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int dateLeft = StoreDataHelper.getInstance().getDayLefts();

        tvTimeLefts.setText("Your date lefts: " + (dateLeft > 0 ? dateLeft : 0));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                try {
                    if (data != null) {
                        Barcode barcode = data.getParcelableExtra("Barcode");
                        if (barcode != null) {
                            searchView.setQuery(barcode.displayValue, true);
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void scanQr() {
        int timeLefts = StoreDataHelper.getInstance().getDayLefts();
        Intent intent;
        if (timeLefts > 0) {
            intent = new Intent(this, BarcodeCaptureActivity.class);
        } else {
            intent = new Intent(this, SubsActivity.class);
        }
        startActivity(intent);

    }

    private void copyText() {
        if (searchView.getQuery() != null) {
            if (ClipboardUtils.get(MainApplication.getInstance()).copy(searchView.getQuery().toString())) {
                Toast.makeText(this, "Copy Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareText() {
        if (searchView.getQuery() != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String content = searchView.getQuery().toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(shareIntent, "Share via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void openLink() {
        if (searchView.getQuery() != null) {
            String text = searchView.getQuery().toString();

            try {
                URL u = new URL(text);
                u.toURI();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
