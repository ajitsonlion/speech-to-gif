package com.ajit.services;

import com.ajit.models.Gif;
import com.ajit.models.GifQuery;
import com.ajit.repository.GiphyRepository;
import com.ajit.repository.TenorRepository;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Service
public class GifService {
    @Autowired
    private GiphyRepository giphyRepository;

    @Autowired
    private TenorRepository tenorRepository;

    private List<Function<Gif, Gif>> gifMethods = new ArrayList<>();

    public GifService() {
        gifMethods.add((gif) -> giphyRepository.byRandom(gif));
        gifMethods.add((gif) -> giphyRepository.bySearch(gif));
        gifMethods.add((gif) -> giphyRepository.byTranslation(gif));
        gifMethods.add((gif) -> tenorRepository.searchTenorForGif(gif));
    }

    public Gif fetchFunnyGif(final GifQuery gifQuery) throws InvalidDataException {
        final String fullText = gifQuery.getTexts().get(0);
        final String query = removeStopWords(fullText);
        final Gif gif = new Gif();
        gif.setQuery(query);
        gif.setFullText(fullText);
        return this.gifMethods.get(new Random().nextInt(this.gifMethods.size())).apply(gif);

    }


    private String removeStopWords(String fullText) throws InvalidDataException {
        return ExudeData.getInstance().filterStoppings(fullText);
    }
}
