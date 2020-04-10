package com.global_relay.globalrelayimagedownloaderproj.view.impl;

import com.global_relay.globalrelayimagedownloaderproj.view_model.ImageDownloaderViewModel;

public interface IPreviewImageView
{
    public ImageDownloaderViewModel getImageDownloaderViewModel();
    public void imagePreviewStartObserving(String webData);
}
