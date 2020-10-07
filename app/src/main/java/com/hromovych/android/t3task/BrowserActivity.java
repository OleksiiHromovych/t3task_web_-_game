package com.hromovych.android.t3task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BrowserActivity extends AppCompatActivity {

    private static final String PAGE_URL = "page url";

    ProgressBar mProgressBar;
    WebView mWebView;

    public static Intent newIntent(Context context, String pageUrl) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(PAGE_URL, pageUrl);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.webView);

        String url = getIntent().getStringExtra(PAGE_URL);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100)
                    mProgressBar.setVisibility(View.GONE);
                else
                    mProgressBar.setVisibility(View.VISIBLE);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
    }

}
