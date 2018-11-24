package com.ajit.services;

import com.ajit.models.Gif;
import com.ajit.models.GifQuery;
import com.ajit.repository.GiphyRepository;
import com.ajit.repository.GoogleRepository;
import com.ajit.repository.TenorRepository;
import org.lionsoul.jcseg.extractor.impl.TextRankKeywordsExtractor;
import org.lionsoul.jcseg.tokenizer.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.Function;

@Service
public class GifService {
    @Autowired
    private GiphyRepository giphyRepository;
    @Autowired
    private TenorRepository tenorRepository;
    @Autowired
    private GoogleRepository googleRepository;

    private Map<String, Function<Gif, Gif>> gifMethods = new HashMap<>();

    public GifService() {
        gifMethods.put("GIPHY RANDOM", (gif) -> giphyRepository.byRandom(gif));
        gifMethods.put("GIPHY SEARCH", (gif) -> giphyRepository.bySearch(gif));
        gifMethods.put("GIPHY TRANSLATION", (gif) -> giphyRepository.byTranslation(gif));
        gifMethods.put("TENOR SEARCH", (gif) -> tenorRepository.byScrapping(gif));
        gifMethods.put("GOOGLE SCRAPPING", (gif) -> googleRepository.byScrapping(gif));
    }

    public Gif fetchFunnyGif(String query) throws IOException, JcsegException {
        List<String> strings = Arrays.asList(query.split(","));
        final GifQuery gifQuery = new GifQuery();
        gifQuery.setTexts(strings);
        return fetchFunnyGif(gifQuery);
    }

    private Gif fetchFunnyGif(final GifQuery gifQuery) throws JcsegException, IOException {
        final String fullText = gifQuery.getTexts().get(0);
        final String query = extractKeyWord(fullText);
        final Gif gif = new Gif();
        gif.setQuery(query);
        gif.setFullText(fullText);
        final int randomIndex = new Random().nextInt(this.gifMethods.size());
        return new ArrayList<>(this.gifMethods.values()).get(randomIndex).apply(gif);

    }


    private String extractKeyWord(String fullText) throws JcsegException, IOException {
        JcsegTaskConfig config = new JcsegTaskConfig(true);
        config.setClearStopwords(true);
        config.setAppendCJKSyn(false);
        config.setKeepUnregWords(false);
        config.setEnSecondSeg(false);
        final ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
        final ISegment seg = SegmentFactory.createJcseg(
                JcsegTaskConfig.COMPLEX_MODE,
                config, dic);

        final TextRankKeywordsExtractor extractor = new TextRankKeywordsExtractor(seg);
        extractor.setMaxIterateNum(100);
        extractor.setWindowSize(10);
        extractor.setKeywordsNum(2);
        // Set the maximum phrase length, default is 5
        final List<String> keywords = extractor.getKeywords(new StringReader(fullText));

        return String.join(" ", keywords);
    }


}
