package com.global_relay.globalrelayimagedownloaderproj.view_model;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.WebCrawler;
import com.global_relay.globalrelayimagedownloaderproj.model.repo.DownloaderApi;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDownloaderViewModel extends AndroidViewModel
{
    private MutableLiveData<List<ImageTO>> imagesList=new MutableLiveData<>();
    private MutableLiveData<String> webData=new MutableLiveData<>();

    private WebCrawler webCrawler;
    private DownloaderApi downloaderApi;
    public ImageDownloaderViewModel(@NonNull Application application) {
        super(application);
        //todo update this part later
        downloaderApi=new DownloaderApi("http://www.google.com");
        webCrawler=new WebCrawler();
    }

    public LiveData<List<ImageTO>> getImagesList() {
        return imagesList;
    }

    public MutableLiveData<String> getWebData() {
        return webData;
    }

    public MutableLiveData<List<ImageTO>> getImageMutableLiveData() {
        return webCrawler.getImageMutableLiveData();
    }

    public void startLoadingData(String url)
    {
        downloaderApi.download(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()!=200)
                {
                    webData.setValue(null);
                    return;
                }
                try
                {
                    webData.setValue(response.body().string());
                }
                catch (IOException ex)
                {
                    webData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                webData.setValue(null);
            }
        });
    }

    public void startFetchingImages(String data)
    {
        webCrawler.startFetchingImages();
    }

    public void loadImage(Context ctx, String url, ImageView view) {
        Glide
                .with(ctx)
                .load(url)
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_panorama)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .into(view);
    }

    public void stopDownloading()
    {

    }
}
