package com.ajit.models;

public class Gif {
    private String imgUrl;
    private String query = "";
    private String fullText = "";
    private String byUsing;

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return "Gif{" +
                "imgUrl='" + imgUrl + '\'' +
                ", query='" + query + '\'' +
                ", fullText='" + fullText + '\'' +
                ", byUsing='" + byUsing + '\'' +
                '}';
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getQuery() {
        return query.isEmpty() ? fullText : query;
    }

    public void setQuery(String query) {
        this.query = query.trim();
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
