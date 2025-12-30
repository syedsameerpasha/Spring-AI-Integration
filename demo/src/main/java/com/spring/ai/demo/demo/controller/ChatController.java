package com.spring.ai.demo.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ai")
public class ChatController {

    // we wont get bean of chatclient directly, we will get chat client.builder bean to avoid tight coupling of chat client configs.
    private ChatClient openAiChatClient;
    private ChatClient ollamaChatClient;

 // working with single model
//    @Autowired
//    public ChatController(ChatClient.Builder builder){
//        this.openAiChatClient=builder.build();
//    }

    //working with multiple models
    public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaChatClient") ChatClient ollamaChatClient){
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient= ollamaChatClient;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query){

        var content = openAiChatClient.prompt(query).call().content();
        return ResponseEntity.ok(content);
    }
}
