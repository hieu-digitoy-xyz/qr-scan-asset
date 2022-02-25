// Generated by view binder compiler. Do not edit!
package com.tool.scanqr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public final class ActivitySubsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearLayout bgItem;

  @NonNull
  public final TextView btnBuy;

  @NonNull
  public final ImageView imgBg;

  @NonNull
  public final CardView item1;

  @NonNull
  public final CardView item2;

  @NonNull
  public final CardView item3;

  private ActivitySubsBinding(@NonNull ConstraintLayout rootView, @NonNull LinearLayout bgItem,
      @NonNull TextView btnBuy, @NonNull ImageView imgBg, @NonNull CardView item1,
      @NonNull CardView item2, @NonNull CardView item3) {
    this.rootView = rootView;
    this.bgItem = bgItem;
    this.btnBuy = btnBuy;
    this.imgBg = imgBg;
    this.item1 = item1;
    this.item2 = item2;
    this.item3 = item3;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySubsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySubsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_subs, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySubsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bgItem;
      LinearLayout bgItem = ViewBindings.findChildViewById(rootView, id);
      if (bgItem == null) {
        break missingId;
      }

      id = R.id.btnBuy;
      TextView btnBuy = ViewBindings.findChildViewById(rootView, id);
      if (btnBuy == null) {
        break missingId;
      }

      id = R.id.imgBg;
      ImageView imgBg = ViewBindings.findChildViewById(rootView, id);
      if (imgBg == null) {
        break missingId;
      }

      id = R.id.item1;
      CardView item1 = ViewBindings.findChildViewById(rootView, id);
      if (item1 == null) {
        break missingId;
      }

      id = R.id.item2;
      CardView item2 = ViewBindings.findChildViewById(rootView, id);
      if (item2 == null) {
        break missingId;
      }

      id = R.id.item3;
      CardView item3 = ViewBindings.findChildViewById(rootView, id);
      if (item3 == null) {
        break missingId;
      }

      return new ActivitySubsBinding((ConstraintLayout) rootView, bgItem, btnBuy, imgBg, item1,
          item2, item3);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
