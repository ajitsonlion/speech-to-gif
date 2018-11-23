package com.ajit.controllers;

import com.ajit.models.Gif;
import com.ajit.services.GifService;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GifService gifService;

    @MessageMapping("/send/query")
    public void onReceivedMesage(final String query) throws IOException, JcsegException {

        final Gif gif=gifService.fetchFunnyGif(query);
        this.template.convertAndSend("/gifs", gif);

    }
}