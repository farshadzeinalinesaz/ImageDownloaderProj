package com.global_relay.globalrelayimagedownloaderproj.view_model;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.WebCrawler;
import com.global_relay.globalrelayimagedownloaderproj.model.local.SettingSP;
import com.global_relay.globalrelayimagedownloaderproj.model.repo.DownloaderApi;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;
import com.global_relay.globalrelayimagedownloaderproj.utils.Utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareImageDownloadViewModel extends AndroidViewModel {
    private static MutableLiveData<ImageTO> imageTOMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> webData = new MutableLiveData<>();

    private DownloaderApi downloaderApi;
    private Utils utils;
    private WebCrawler webCrawler;

    public ShareImageDownloadViewModel(@NonNull Application application) {
        super(application);
        utils = Utils.getInstance(application.getApplicationContext());
        downloaderApi = new DownloaderApi("https://www.globalrelay.com/");
        webCrawler = new WebCrawler();
    }

    public MutableLiveData<ImageTO> getImageTOMutableLiveData() {
        return imageTOMutableLiveData;
    }

    public MutableLiveData<List<ImageTO>> getImageMutableLiveData() {
        return webCrawler.getImageMutableLiveData();
    }

    public void startFetchingImages(String data) {
        webCrawler.startParseDocument(data);
    }

    public boolean updateDataLocalPath(String localPath) {
        SettingSP settingSP = new SettingSP(getApplication().getApplicationContext());
        return settingSP.updateDataLocalPath(localPath);
    }

    public String getDataLocalPath() {
        SettingSP settingSP = new SettingSP(getApplication().getApplicationContext());
        String localPath = settingSP.getDataLocalPath();
        if (localPath == null || localPath.isEmpty()) {
            localPath = utils.hasDeviceExternalStorage() ? utils.getExternalStoragePath() : utils.getCameraGalleryPath();
        }
        return localPath;
    }

    public MutableLiveData<String> getWebData() {
        return webData;
    }

    public void startDownloadSaveImages(ImageTO imageTO) {
        if (imageTO.isDownloaded()) {
            imageTOMutableLiveData.setValue(imageTO);
            return;
        }
        imageTO.setDownloaded(false);
        downloaderApi.download(imageTO.getImagePath(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    imageTOMutableLiveData.setValue(imageTO);
                    return;
                }
                try {
                    if (utils.writeToFile(getDataLocalPath(), String.valueOf(new Date().getTime()), response.body().bytes())) {
                        imageTO.setDownloaded(true);
                        imageTOMutableLiveData.setValue(imageTO);
                    }
                } catch (IOException ex) {
                    imageTOMutableLiveData.setValue(imageTO);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                imageTOMutableLiveData.setValue(imageTO);
            }
        });
    }

    public void startLoadingData(String url) {
        downloaderApi.download(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    webData.setValue(null);
                    return;
                }
                try {
                    webData.setValue(response.body().string());
                } catch (IOException ex) {
                    webData.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                webData.setValue(null);
            }
        });
    }

    public void loadImage(Context ctx, String url, ImageView view) {
        Glide.with(ctx)
                .load(url)
                .placeholder(R.drawable.ic_panorama)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .into(view);
    }
}
