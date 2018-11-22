package com.ajit.services;

import com.ajit.models.Gif;
import com.ajit.models.GifQuery;
import com.ajit.repository.GiphyRepository;
import com.ajit.repository.TenorRepository;
import com.uttesh.exude.exception.InvalidDataException;
import org.lionsoul.jcseg.extractor.impl.TextRankKeywordsExtractor;
import org.lionsoul.jcseg.tokenizer.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
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
        gifMethods.add((gif) -> tenorRepository.byScrapping(gif));
    }

    public Gif fetchFunnyGif(final GifQuery gifQuery) throws   JcsegException, IOException {
        final String fullText = gifQuery.getTexts().get(0);
        final String query = removeStopWords(fullText);
        final Gif gif = new Gif();
        gif.setQuery(query);
        gif.setFullText(fullText);
        return this.gifMethods.get(new Random().nextInt(this.gifMethods.size())).apply(gif);

    }


    private String removeStopWords(String fullText) throws JcsegException, IOException {
        JcsegTaskConfig config = new JcsegTaskConfig(true);
        config.setClearStopwords(true);
        config.setAppendCJKSyn(false);
        config.setKeepUnregWords(false);
        config.setEnSecondSeg(false);
        ADictionary dic = DictionaryFactory.createSingletonDictionary(config);
        ISegment seg = SegmentFactory.createJcseg(
                JcsegTaskConfig.COMPLEX_MODE,
                config, dic);


        TextRankKeywordsExtractor extractor = new TextRankKeywordsExtractor(seg);
        extractor.setMaxIterateNum(100);
        extractor.setWindowSize(10);
        extractor.setKeywordsNum(2);
        // Set the maximum phrase length, default is 5
        List<String> keywords = extractor.getKeywords(new StringReader(fullText));

        return String.join(" ", keywords);
    }
}
