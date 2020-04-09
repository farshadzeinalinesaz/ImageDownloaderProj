package com.global_relay.globalrelayimagedownloaderproj.model.repo.impl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IDownloaderApi {
    //@FormUrlEncoded
    //@Headers( "Content-Type: application/json" )
    //@Headers( { "Accept: text/plain"} )
    @GET
    Call<ResponseBody> download(@Url String url);
}
