package com.iak.mwi.finalprojectiakwildan.Model;

/**
 * Created by mwi on 5/23/17.
 */

public class Trailer {
    private String title, url, type;

    public Trailer(String title, String url, String type) {
        this.title = title;
        this.url = "http://www.youtube.com/watch?v=" + url;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
