package com.example.mechanic2.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic2.R;
import com.example.mechanic2.app.ChromeClient;
import com.example.mechanic2.models.Movies;
import com.example.mechanic2.models.WebMovies;

import java.util.List;

public class MechanicWebViewAdapter extends RecyclerView.Adapter<MechanicWebViewAdapter.WebViewViewHolder> {
    List<Movies> htmlList;
    Activity activity;


    public MechanicWebViewAdapter(Activity activity,List<Movies> htmlList) {
        this.activity=activity;
        this.htmlList = htmlList;
    }

    @NonNull
    @Override
    public WebViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WebViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WebViewViewHolder holder, int position) {
        holder.bindView(htmlList.get(position).getMovie_url());
    }

    @Override
    public int getItemCount() {
        return htmlList.size();
    }

    class WebViewViewHolder extends RecyclerView.ViewHolder {
        private WebView mWebView;


        public WebViewViewHolder(@NonNull View itemView) {
            super(itemView);
            mWebView = itemView.findViewById(R.id.web_view);
        }

        @SuppressLint("SetJavaScriptEnabled")
        void bindView(String html) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebChromeClient(new ChromeClient(activity));
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            mWebView.setBackgroundColor(0);
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
            mWebView.setBackground(itemView.getContext().getDrawable(R.drawable.btn_white_solid));
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    view.loadUrl(request.getUrl().toString());
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageStarted(WebView webview, String url, Bitmap favicon) {
                    super.onPageStarted(webview, url, favicon);
                    webview.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPageFinished(WebView webview, String url) {

                    webview.setVisibility(View.VISIBLE);
                    super.onPageFinished(webview, url);

                }
            });

            mWebView.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);

        }
    }
}
