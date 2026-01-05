package com.spring.ai.demo.demo.controller;

import com.spring.ai.demo.demo.Entity.Tut;
import com.spring.ai.demo.demo.service.ChatServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/ai")
public class ChatController {

    private ChatServiceImpl chatService;

    @Autowired
    public ChatController(ChatServiceImpl chatService){
        this.chatService = chatService;
    }

    // we wont get bean of chatclient directly, we will get chat client.builder bean to avoid tight coupling of chat client configs.
//    private ChatClient openAiChatClient;
//    private ChatClient ollamaChatClient;

 // working with single model
//    @Autowired
//    public ChatController(ChatClient.Builder builder){
//        this.openAiChatClient=builder.build();
//    }

//    //working with multiple models
//    public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaChatClient") ChatClient ollamaChatClient){
//        this.openAiChatClient = openAiChatClient;
//        this.ollamaChatClient= ollamaChatClient;
//    }

    @GetMapping("/chat")
    public ResponseEntity<List<Tut>> chat(@RequestParam(value = "q") String query){
        return ResponseEntity.ok(chatService.message(query));
    }
}
