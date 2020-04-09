package com.global_relay.globalrelayimagedownloaderproj.model;

import androidx.lifecycle.MutableLiveData;

import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class WebCrawler {
    private String data;
    private MutableLiveData<List<ImageTO>> imageMutableLiveData=new MutableLiveData<>();

    public WebCrawler() {
    }

    public MutableLiveData<List<ImageTO>> getImageMutableLiveData() {
        return imageMutableLiveData;
    }

    public void startFetchingImages() {
        List<ImageTO> imageTOList = new ArrayList<>();
        imageTOList.add(new ImageTO(1, "Title 1", "Desc 1", "https://images.unsplash.com/photo-1513151233558-d860c5398176?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));
        imageTOList.add(new ImageTO(2, "Title 2", "Desc 2", "https://st3.depositphotos.com/1025323/12780/i/950/depositphotos_127802654-stock-photo-paint-explosion-background.jpg"));
        imageTOList.add(new ImageTO(3, "Title 3", "Desc 3", "https://st3.depositphotos.com/1025323/12863/i/950/depositphotos_128631806-stock-photo-paint-explosion-background.jpg"));
        imageTOList.add(new ImageTO(4, "Title 4", "Desc 4", "https://cdn.pixabay.com/photo/2016/06/02/02/33/triangles-1430105__340.png"));
        imageTOList.add(new ImageTO(5, "Title 5", "Desc 5", "https://image.shutterstock.com/image-illustration/color-splash-series-background-design-260nw-587409425.jpg"));
        imageMutableLiveData.setValue(imageTOList);
    }
}
