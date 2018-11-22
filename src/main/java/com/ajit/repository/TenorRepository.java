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
    //  private static final String API_KEY = "02FJUD5T2XIG&q=";
    private static final String BASE_URL = "https://tenor.com";
    private static final String SEARCH_URL = BASE_URL + "/search/";
    // private static final String API_BASE = "https://api.tenor.com/v1";
    // private static final String SEARCH_API_WITH_KEY = API_BASE + "/search?key=" + API_KEY + "?media_filter=minimal&limit=5";

    // private final RestTemplate restTemplate = new RestTemplate();

    public Gif byScrapping(final Gif gif) {
/*
        // TODO : FIX THIS SHIT, CREATE OBJECTS FOR THE API
        final LinkedHashMap quote = restTemplate.getForObject(SEARCH_API_WITH_KEY + "&q=" + gif.getFullText(), LinkedHashMap.class);
        final List<LinkedHashMap> results = (ArrayList<LinkedHashMap>) quote.get("results");
        final List<LinkedHashMap> medias = results.stream().map(result -> ((List<LinkedHashMap>) result.get("media")).get(0)).collect(Collectors.toList());
        final List<LinkedHashMap> gifObjects = medias.stream().map(media -> (LinkedHashMap) media.get("gif")).collect(Collectors.toList());
        final List<String> gifUrls = gifObjects.stream().map(gifObj -> (String) gifObj.get("url")).collect(Collectors.toList());


        // Randomly select one of the images
        final String imgUrl = gifUrls.get(new Random().nextInt(gifUrls.size()));
        gif.setImgUrl(imgUrl);
        gif.setByUsing("TENOR SEARCH");
        */


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
            gif.setByUsing("TENOR SEARCH");
        } catch (final IOException e) {
            logger.error("Error getting tenor page", e, gif);
        }


        return gif;
    }

    private String buildTenorUrl(final String query) {

        return SEARCH_URL + query.replace(" ", "+");
    }
}
