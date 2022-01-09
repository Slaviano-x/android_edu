package com.tyryshkin.newsapp.ui.web

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import com.tyryshkin.newsapp.R
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.widget.Button

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var textView: TextView
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val webPresenter = WebPresenter(getConnectivityManager())

        val url = intent.getStringExtra("url")

        initViews()

        if (webPresenter.isOnline()) {
            showActivity()
            tryFollowLink(url)
        } else {
            showActivity(View.INVISIBLE, View.VISIBLE, getString(R.string.error_internet_2))
        }
    }

    private fun tryFollowLink(url: String?) {
        webView.webViewClient = object: WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                showActivity(View.INVISIBLE, View.VISIBLE, getString(R.string.error_link_2))
                super.onReceivedError(view, request, error)
            }
        }
        if (url != null) {
            webView.loadUrl(url)
        } else {
            showActivity(View.INVISIBLE, View.VISIBLE, getString(R.string.error_link))
        }
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        constraintLayout = findViewById(R.id.constraintLayout)
        textView = findViewById(R.id.textView3)

        retryButton = findViewById(R.id.button)
        retryButton.setOnClickListener {
            showActivity()
            finish()
            startActivity(intent)
        }
    }

    private fun getConnectivityManager(): ConnectivityManager {
        return this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun showActivity(visibleWebView: Int = View.VISIBLE,
                             visibleLayout: Int = View.INVISIBLE,
                             errorMsg: String = "") {
        webView.visibility = visibleWebView
        constraintLayout.visibility = visibleLayout
        textView.text = errorMsg
    }
}