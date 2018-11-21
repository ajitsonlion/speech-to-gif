package com.ajit.models;

import java.util.ArrayList;
import java.util.List;

public class GifQuery {
    private List<String> texts = new ArrayList<>();

    public GifQuery() {
        this.texts.add("not found");
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
}
