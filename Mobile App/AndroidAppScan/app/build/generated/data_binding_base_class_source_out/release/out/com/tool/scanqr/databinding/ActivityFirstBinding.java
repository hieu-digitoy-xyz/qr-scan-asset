// Generated by view binder compiler. Do not edit!
package com.tool.scanqr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tool.scanqr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFirstBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout bgQr;

  @NonNull
  public final CardView btnScan;

  @NonNull
  public final ImageView imgPremium;

  private ActivityFirstBinding(@NonNull ConstraintLayout rootView, @NonNull FrameLayout bgQr,
      @NonNull CardView btnScan, @NonNull ImageView imgPremium) {
    this.rootView = rootView;
    this.bgQr = bgQr;
    this.btnScan = btnScan;
    this.imgPremium = imgPremium;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFirstBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFirstBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_first, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFirstBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bgQr;
      FrameLayout bgQr = ViewBindings.findChildViewById(rootView, id);
      if (bgQr == null) {
        break missingId;
      }

      id = R.id.btnScan;
      CardView btnScan = ViewBindings.findChildViewById(rootView, id);
      if (btnScan == null) {
        break missingId;
      }

      id = R.id.imgPremium;
      ImageView imgPremium = ViewBindings.findChildViewById(rootView, id);
      if (imgPremium == null) {
        break missingId;
      }

      return new ActivityFirstBinding((ConstraintLayout) rootView, bgQr, btnScan, imgPremium);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
