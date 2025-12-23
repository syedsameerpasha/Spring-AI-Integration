package com.spring.ai.demo.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ai")
public class ChatController {

    // we wont get bean of chatclient directly, we will get chat client.builder bean to avoid tight coupling of chat client configs.
    private ChatClient chatClient;

    public ChatController(ChatClient.Builder builder){
        this.chatClient=builder.build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query){

        String content = chatClient.prompt("q").call().content();
        return ResponseEntity.ok(content);
    }
}
