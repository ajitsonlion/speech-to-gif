package com.ajit.repository;

import com.ajit.models.Gif;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class GoogleRepository {
    private static final String URL_BASE = "https://www.google.com/search?tbm=isch&tbs=itp:animated&q=";
    private static Logger logger = LoggerFactory.getLogger(GoogleRepository.class);

    public Gif byScrapping(final Gif gif) {
        try {
            // https://stackoverflow.com/questions/40162503/java-jsoup-google-image-search-result-parsing
            final Document doc = Jsoup.connect(URL_BASE + gif.getQuery() + "+funny").get();
            final Elements elements = doc.select("div.rg_meta");
            List<String> resultUrls = new ArrayList<>();
            for (Element element : elements) {
                if (element.childNodeSize() > 0) {
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(element.childNode(0).toString());
                        resultUrls.add((String) jsonObject.get("ou"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            final String imgUrl = resultUrls.get(new Random().nextInt(resultUrls.size() / 10));
            gif.setImgUrl(imgUrl);
            gif.setByUsing("GOOGLE SCRAPPING");

        } catch (IOException e) {
            logger.error("Error loading google images url", e);
        }
        return gif;
    }

}
