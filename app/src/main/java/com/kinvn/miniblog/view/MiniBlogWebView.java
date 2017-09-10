package com.kinvn.miniblog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Kinvn on 2017/9/7.
 */

public class MiniBlogWebView extends WebView {
    public MiniBlogWebView(Context context) {
        this(context, null);
    }

    public MiniBlogWebView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MiniBlogWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
