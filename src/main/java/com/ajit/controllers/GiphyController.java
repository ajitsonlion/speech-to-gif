package com.ajit.controllers;

import at.mukprojects.giphy4j.exception.GiphyException;
import com.ajit.models.GifQuery;
import com.ajit.models.Gif;
import com.ajit.services.GifService;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/giphy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GiphyController {

    @Autowired
    private GifService gifService;

    @PostMapping("query")
    @ResponseBody
    public Gif hello(@RequestBody GifQuery queries) throws GiphyException, InvalidDataException {

        return gifService.fetchFunnyGif(queries);
    }
}
