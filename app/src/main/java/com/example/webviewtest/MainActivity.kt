package com.example.webviewtest

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var qrCodeBottomSheetView: View
    private lateinit var qrCodeBottomSheetDialog: BottomSheetDialog

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        // onCreate
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize QRCode bottomSheetView
        qrCodeBottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        qrCodeBottomSheetView.resources.displayMetrics

        // initialize QRCode bottomSheetDialog
        qrCodeBottomSheetDialog = BottomSheetDialog(this)
        qrCodeBottomSheetDialog.setContentView(qrCodeBottomSheetView)

        // initialize web-view
        val webView: WebView = findViewById(R.id.WebView)
        webView.loadUrl("http://192.168.10.44:3000")
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(this), "android") // append android interface in javascript window object
        webView.webViewClient = TCJWebViewClient()

    }

    public fun openBottomSheetDialog() {
        val width = (windowManager.defaultDisplay.width * 0.7).toInt()
        val bitmapQrCode = BarcodeEncoder().encodeBitmap("test", BarcodeFormat.QR_CODE, width, width)
        val imageView: ImageView = qrCodeBottomSheetView.findViewById(R.id.QrImageView)
        imageView.setImageBitmap(bitmapQrCode)
        runOnUiThread {
            qrCodeBottomSheetDialog.show()
        }
    }
}

class WebAppInterface(private val mainActivity: MainActivity) {
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mainActivity.applicationContext, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun getAccessToken(): String {
        return "Access Token"
    }

    @JavascriptInterface
    fun openQrCode() {
        mainActivity.openBottomSheetDialog()
    }
}

private class TCJWebViewClient : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Log.d("WebViewClient", "page started")
        super.onPageStarted(view, url, favicon)
    }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d("WebViewClient", request?.url.toString())
        return false
    }

}