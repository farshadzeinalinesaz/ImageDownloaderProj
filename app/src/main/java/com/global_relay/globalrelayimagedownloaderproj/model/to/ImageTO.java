package com.global_relay.globalrelayimagedownloaderproj.model.to;

public class ImageTO
{
    private int id;
    private String title;
    private String desc;
    private String imagePath;
    private boolean isDownloaded;

    public ImageTO(int id, String title, String desc,String imagePath) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }
}
