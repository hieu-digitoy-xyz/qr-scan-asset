package com.tool.scanqrasset.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {
    private static ClipboardUtils mInstance;

    private Context mContext;
    private ClipboardManager mClipboardManager;

    public ClipboardUtils(Context aContext) {
        mContext = aContext;

        mClipboardManager = (ClipboardManager) aContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static ClipboardUtils get(Context aContext) {
        if (mInstance == null) {
            mInstance = new ClipboardUtils(aContext);
        }

        return mInstance;
    }

    public boolean copy(String srcText) {
        if (mClipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("TextModel", srcText);
            mClipboardManager.setPrimaryClip(clipData);

            return true;
        }

        return false;
    }
}
