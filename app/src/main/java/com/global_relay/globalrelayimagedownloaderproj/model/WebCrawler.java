package com.global_relay.globalrelayimagedownloaderproj.model;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;

import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class WebCrawler {
    private MutableLiveData<List<ImageTO>> imageMutableLiveData = new MutableLiveData<>();

    public WebCrawler() {
    }

    public MutableLiveData<List<ImageTO>> getImageMutableLiveData() {
        return imageMutableLiveData;
    }

    public void startParseDocument(String data) {
        if (data == null || data.isEmpty()) {
            imageMutableLiveData.setValue(null);
            return;
        }
        Document document = Jsoup.parse(data);
        startLoadingImgSrc(document);
    }

    private void startLoadingImgSrc(Document document) {
        if (document == null) {
            imageMutableLiveData.setValue(null);
            return;
        }
        List<ImageTO> imageTOList = new ArrayList<>();
        int counter = 0;
        Elements images = document.getElementsByTag("img");
        for (Element img : images) {
            ++counter;
            String url = img.attr("src");
            String title = img.attr("alt");
            if (Patterns.WEB_URL.matcher(url).matches()) {
                imageTOList.add(new ImageTO(counter, title, "", url));
            }
        }
        Elements links = document.getElementsByTag("a");
        for (Element link : links) {
            ++counter;
            String url = link.attr("href");
            String title = link.text();
            if (Patterns.WEB_URL.matcher(url).matches()) {
                imageTOList.add(new ImageTO(counter, title, "", url));
            }
        }
        imageMutableLiveData.setValue(imageTOList);
    }
}
