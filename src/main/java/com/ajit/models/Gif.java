package com.ajit.models;

public class Gif {
    private String imgUrl;
    private String query = "";
    private String fullText = "";
    private String byUsing;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getQuery() {
        return query.trim();
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFullText() {
        return fullText.trim();
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getByUsing() {
        return byUsing;
    }

    public void setByUsing(String byUsing) {
        this.byUsing = byUsing;
    }
}
