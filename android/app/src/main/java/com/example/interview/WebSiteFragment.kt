package com.example.interview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.interview.base.BaseFragment

class WebSiteFragment(override val mLayoutResId: Int = R.layout.fragment_page_b) : BaseFragment() {

    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("url").toString()
        Log.d(sTAG,"$javaClass > received key(Title) > value:$title")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val webView = view!!.findViewById<WebView>(R.id.webview)
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                // Here can handle error about internet
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                // Here handle website loading
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://www.travel.taipei/zh-tw/news/details/45417")

        webView.webChromeClient = WebChromeClient() // Handle jsp dialog

        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE



        return view
    }

    override fun onResume() {
        super.onResume()
        updateTitle("最新消息")
        updateSettingsButtonVisibility(false)
    }

}