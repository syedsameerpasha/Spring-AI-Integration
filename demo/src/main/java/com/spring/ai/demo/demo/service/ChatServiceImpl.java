package com.spring.ai.demo.demo.service;

import com.spring.ai.demo.demo.Entity.Tut;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.util.List;
import java.util.Map;


@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

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

    @Override
    public String chatTemplate() {

//        //first step
//        PromptTemplate strTemplate = PromptTemplate.builder().template("What is {techName}? tell ma also about {techExample}").build();
//
//        //render the template
//
//        String renderedMessage = strTemplate.render(Map.of(
//                "techName", "Spring",
//                "techExample", "spring exception"
//        ));
//
//        // build the prompt
//        Prompt prompt = new Prompt(renderedMessage);

        //return this.chatClient.prompt(prompt).call().content();
     //   ____________________________________________________________________________


//        var systemPromptTemplate=SystemPromptTemplate.builder()
//                .template("You are a helpful coding assistant. You are an expert in coding.")
//                .build();
//        var systemMessage=systemPromptTemplate.createMessage();
//
//        var userPromptTemplate=PromptTemplate.builder().template("What is {techName}? tell ma also about {techExample}").build();
//        var userMessage=userPromptTemplate.createMessage(Map.of(
//                "techName", "Spring",
//                "techExample", "spring exception"
//        ));
//
//
//        Prompt prompt = new Prompt(systemMessage,userMessage);

        return this.chatClient
                .prompt()
                .system(system ->
                        system.text(systemMessage.toString())
                )
                .user(user ->
                        user.text(userMessage.toString())
                                .param("concept", "Python iteration")
                )
                .call()
                .content();



    }
}
