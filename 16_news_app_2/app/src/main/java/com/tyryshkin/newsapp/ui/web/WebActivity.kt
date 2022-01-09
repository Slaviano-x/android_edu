package com.tyryshkin.newsapp.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

import com.tyryshkin.newsapp.R

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        webView = findViewById(R.id.webView)

        val url = intent.getStringExtra("url")
        webView.webViewClient = WebViewClient()
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}