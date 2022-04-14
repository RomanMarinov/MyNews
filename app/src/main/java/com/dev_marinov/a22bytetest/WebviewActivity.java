package com.dev_marinov.a22bytetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

//        webView = findViewById(R.id.webview);
//        Intent intent = getIntent();
//        String url = intent.getStringExtra("url");
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(url);


        webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


    }
}