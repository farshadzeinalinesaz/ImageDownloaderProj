package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.view.activities.MainActivity;
import com.global_relay.globalrelayimagedownloaderproj.view_model.ShareImageDownloadViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagePreviewFragment extends Fragment implements Observer<String> {
    private ShareImageDownloadViewModel shareImageDownloadViewModel;
    private MutableLiveData<String> webData;

    @BindView(R.id.btnUrlLoader)
    public MaterialButton btnUrlLoader;
    @BindView(R.id.editImageUrl)
    public TextInputEditText editImageUrl;
    @BindView(R.id.webViewImagePreview)
    public WebView webViewImagePreview;

    public ImagePreviewFragment() {
    }

    public static ImagePreviewFragment newInstance() {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_preview, container, false);
        ButterKnife.bind(this, rootView);
        setupWebViewImagePreview();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareImageDownloadViewModel = ViewModelProviders.of(getActivity()).get(ShareImageDownloadViewModel.class);
        webData = shareImageDownloadViewModel.getWebData();
        webData.observe(getViewLifecycleOwner(), this);
    }

    private void setupWebViewImagePreview() {
        WebSettings webSettings = webViewImagePreview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webViewImagePreview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewImagePreview.setWebViewClient(new AppWebViewClient());
    }

    private void loadWebViewImagePreview(String data) {
        webViewImagePreview.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }


    @OnClick({R.id.btnUrlLoader})
    public void doClick(View view) {
        if (view.getId() == R.id.btnUrlLoader) {
            String url = editImageUrl.getText().toString();
            if (!Patterns.WEB_URL.matcher(url).matches()) {
                editImageUrl.setError(getResources().getString(R.string.invalid_url));
                return;
            }
            shareImageDownloadViewModel.startLoadingData(url);
        }
    }

    @Override
    public void onChanged(String webData) {
        loadWebViewImagePreview(webData);
    }


    private static class AppWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }
}
