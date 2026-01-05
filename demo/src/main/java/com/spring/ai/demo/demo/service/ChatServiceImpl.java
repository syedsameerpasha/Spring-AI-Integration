package com.spring.ai.demo.demo.service;

import com.spring.ai.demo.demo.Entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;

    private static final PromptTemplate TUT_PROMPT =
            new PromptTemplate("""
            You are a helpful assistant.
            Answer ONLY using the provided context.
            Do not hallucinate.

            Return STRICT JSON.
            The response must match this structure:
            List<Tut>

            User Query:
            {query}
            """);

    @Autowired
    public ChatServiceImpl(ChatClient.Builder chatClient){
        this.chatClient = chatClient.build();
    }
    @Override
    public List<Tut> message(String query) {
        var prompt = TUT_PROMPT.create(
                Map.of("query", query)
        );


        return chatClient
                .prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {
                });

    }
}
