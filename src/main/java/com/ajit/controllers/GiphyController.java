package com.ajit.controllers;

import com.ajit.models.Gif;
import com.ajit.models.GifQuery;
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
    public Gif queryFunnyGif(@RequestBody GifQuery gifQuery) throws InvalidDataException {

        return gifService.fetchFunnyGif(gifQuery);
    }
}
