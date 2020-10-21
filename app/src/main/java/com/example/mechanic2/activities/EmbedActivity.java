package com.example.mechanic2.activities;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mechanic2.R;

public class EmbedActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embed);

        mWebView = findViewById(R.id.web_view);

        //mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(0x00000000);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //String html = "<iframe src='http://www.aparat.com/video/video/embed/videohash/VB8KM/vt/frame' allowFullScreen='true' webkitallowfullscreen='true' mozallowfullscreen='true' />";
        //String html = "<div id='7633990465'><script type='text/JavaScript' src='https://www.aparat.com/embed/4RiFs?data[rnddiv]=7633990465&data[responsive]=yes&&recom=none'></script></div>";
        //String html = "<style>.h_iframe-aparat_embed_frame{position:relative;}.h_iframe-aparat_embed_frame .ratio{display:block;width:100%;height:auto;}.h_iframe-aparat_embed_frame iframe{position:absolute;top:0;left:0;width:100%;height:100%;}</style><div class='h_iframe-aparat_embed_frame'><span style='display: block;padding-top: 57%'></span><iframe src='http://www.aparat.com/video/video/embed/videohash/4RiFs/vt/frame?&recom=none' allowFullScreen='true' webkitallowfullscreen='true' mozallowfullscreen='true'></iframe></div>";
        String html = "<div id='31818645336'><script type='text/JavaScript' src='https://www.aparat.com/embed/ulkiv?data[rnddiv]=31818645336&data[responsive]=yes'></script></div>";
        mWebView.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
    }


}