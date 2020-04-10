package com.global_relay.globalrelayimagedownloaderproj.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.global_relay.globalrelayimagedownloaderproj.model.repo.DownloaderApi;
import com.global_relay.globalrelayimagedownloaderproj.view.activities.MainActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePreviewViewModel extends AndroidViewModel
{
    private MutableLiveData<String> webData=new MutableLiveData<>();
    private DownloaderApi downloaderApi;

    public ImagePreviewViewModel(@NonNull Application application) {
        super(application);
        downloaderApi=new DownloaderApi("https://www.globalrelay.com/");
    }

    public MutableLiveData<String> getWebData() {
        return webData;
    }

    public void imagePreviewStartObserving(MainActivity mainActivity,String webData)
    {
        mainActivity.imagePreviewStartObserving(webData);
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
}
