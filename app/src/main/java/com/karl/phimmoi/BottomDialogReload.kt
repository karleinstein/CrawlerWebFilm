package com.karl.phimmoi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_reload.*
import net.sourceforge.htmlunit.corejs.javascript.tools.shell.Main

class BottomDialogReload(context: Context, private val url: String) : BottomSheetDialog(context), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnCancel -> {
                this.dismiss()
            }
            R.id.btnReload -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet_dialog_reload)
        val typeface = Typeface.createFromAsset(context.assets, "font/diablo_h.ttf")
        txtServer.typeface = typeface
        btnCancel.setOnClickListener(this)
        btnReload.setOnClickListener(this)
    }
}