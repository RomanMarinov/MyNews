package com.dev_marinov.a22bytetest;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

public class FragmentWebview extends Fragment{

    View frag;

    WebView webView;
    Handler handler;
    String myUrl;

    private String mLastUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).flag = true;

        Log.e("444", "-зашел FragmentWebview-");
        Log.e("444","FragmentWebview getSupportFragmentManager().getBackStackEntryCount()="
                + getActivity().getSupportFragmentManager().getBackStackEntryCount());

        frag = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = frag.findViewById(R.id.webview);

        webView.loadUrl(myUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("DebugDebug", "OnPageFinished " + url);
                mLastUrl = url;
                super.onPageFinished(view, url);
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                WebView.HitTestResult hr  = ((WebView)view).getHitTestResult();
                if (hr != null && mLastUrl != null) {
                    if (((MainActivity)getActivity()).previous.isEmpty() || !((MainActivity)getActivity()).previous
                            .get(((MainActivity)getActivity()).previous.size() - 1).equals(mLastUrl))
                    {
                        ((MainActivity)getActivity()).previous.add(mLastUrl);
                    }
                    Log.e("DebugDebug", "getExtra = " + hr.getExtra() + "\t\t Type = " + hr.getType());
                }
                return false;
            }
        });

        return frag;
    }

    public void setParam(String url)
    {
        myUrl = url;
    }
}