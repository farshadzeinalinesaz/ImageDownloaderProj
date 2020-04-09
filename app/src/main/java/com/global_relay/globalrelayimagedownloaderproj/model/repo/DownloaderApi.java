package com.global_relay.globalrelayimagedownloaderproj.model.repo;

import com.global_relay.globalrelayimagedownloaderproj.model.repo.impl.IDownloaderApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DownloaderApi {
    private IDownloaderApi iDownloaderApi;

    public DownloaderApi(String baseUrl) {
        Retrofit retrofit = new RepoCoreApi(baseUrl).getRetrofit();
        iDownloaderApi = retrofit.create(IDownloaderApi.class);
    }

    public void download(String url, Callback<ResponseBody> callback) {
        if (callback == null) {
            return;
        }
        Call<ResponseBody> call = iDownloaderApi.download(url);
        call.enqueue(callback);
    }
}
