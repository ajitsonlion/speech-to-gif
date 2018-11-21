package com.ajit.repository;

import com.ajit.models.Gif;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Random;

@Repository
public class TenorRepository {
    private static Logger logger = LoggerFactory.getLogger(TenorRepository.class);
    private static final String BASE_URL = "https://tenor.com";
    private static final String SEARCH_URL = BASE_URL + "/search/";


    public Gif searchTenorForGif(final Gif gif) {
        gif.setByUsing("TENOR SEARCH");
        try {
            final String url = buildTenorUrl(gif.getQuery());
            final Document doc = Jsoup.connect(url).get();
            final Elements imageElements = doc.getElementsByTag("figure");
            final int randomMax = imageElements.size() / 4;
            final Element imageElement = imageElements.get(new Random().nextInt(randomMax)).child(0);

            // fetch the page for the image to get the larger gif
            final String largeImgSiteUrl = BASE_URL + imageElement.attr("href");
            final Document largeImageDoc = Jsoup.connect(largeImgSiteUrl).get();
            final Element imageLargeElement = largeImageDoc.getElementsByClass("Gif").get(0).child(0);
            final String imgUrl = imageLargeElement.attr("src");
            gif.setImgUrl(imgUrl);
        } catch (final IOException e) {
            logger.error("Error getting tenor page", e, gif);
        }

        return gif;
    }

    private String buildTenorUrl(final String query) {

        return SEARCH_URL + query.replace(" ", "+");
    }
}
