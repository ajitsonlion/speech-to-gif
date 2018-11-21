package com.ajit.repository;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.entity.search.SearchRandom;
import at.mukprojects.giphy4j.exception.GiphyException;
import com.ajit.models.Gif;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Random;

@Repository
public class GiphyRepository {
    private final Giphy giphy = new Giphy("fANff7PNrIxKjqqT8EFpT0xG1E0X1DeJ");
    private static Logger logger = LoggerFactory.getLogger(GiphyRepository.class);

    public Gif byTranslation(final Gif gif) {
        logger.info("Using Translate method");
        gif.setByUsing("GIPHY TRANSLATION");
        try {
            final SearchGiphy translate = giphy.translate(gif.getFullText().replace(' ', '-'));
            final String imgUrl = translate.getData().getImages().getOriginal().getUrl();
            gif.setImgUrl(imgUrl);
        } catch (final GiphyException e) {
            logger.error("Error while fetching by translation ", e, gif);
        }
        return gif;
    }

    public Gif bySearch(final Gif gif) {
        logger.info("Using Search method");
        gif.setByUsing("GIPHY SEARCH");
        try {
            final SearchFeed searchFeed = giphy.search(gif.getQuery().replace(' ', '-'), new Random().nextInt(5));
            final String imgUrl = searchFeed.getDataList().get(0).getImages().getOriginal().getUrl();
            gif.setImgUrl(imgUrl);
        } catch (final GiphyException e) {
            logger.error("Error while fetching by translation ", e, gif);
            return byTranslation(gif);
        }
        return gif;

    }

    public Gif byRandom(final Gif gif) {
        logger.info("Using Random method");
        gif.setByUsing("GIPHY RANDOM");
        try {
            final SearchRandom random = giphy.searchRandom(gif.getQuery().replace(' ', '-'));
            final String imgUrl = random.getData().getImageUrl();
            gif.setImgUrl(imgUrl);
        } catch (final GiphyException e) {
            logger.error("Error while fetching by translation ", e, gif);
            return byTranslation(gif);
        }
        return gif;

    }
}
