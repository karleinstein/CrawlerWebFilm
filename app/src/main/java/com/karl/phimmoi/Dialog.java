package com.karl.phimmoi;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

public class Dialog extends android.app.Dialog {
    private TextView loading;
    private ProgressBar progressBar;

    Dialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        loading = findViewById(R.id.loading);
        progressBar = findViewById(R.id.progress_bar);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/diablo_h.ttf");
        loading.setTypeface(typeface);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Objects.requireNonNull(getWindow())
                .setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
