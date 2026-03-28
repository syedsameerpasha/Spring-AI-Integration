package com.spring.ai.demo.demo.service;

import com.spring.ai.demo.demo.Entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@Service
public class ChatServiceImpl implements ChatService {
    private final ChatClient chatClient;

    private VectorStore vectorStore;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;



//    private static final PromptTemplate TUT_PROMPT =
//            new PromptTemplate("""
//            You are a helpful assistant.
//            Answer ONLY using the provided context.
//            Do not hallucinate.
//
//            Return STRICT JSON.
//            The response must match this structure:
//            List<Tut>
//
//            User Query:
//            {query}
//            """);
//
    @Autowired
    public ChatServiceImpl(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore= vectorStore;
    }
//    @Override
//    public List<Tut> message(String query) {
//        var prompt = TUT_PROMPT.create(
//                Map.of("query", query)
//        );
//
//
//        return chatClient
//                .prompt(prompt)
//                .call()
//                .entity(new ParameterizedTypeReference<List<Tut>>() {
//                });
//
//    }
//
    @Override
    public String chatTemplate(String query, String userId) {
//
////        //first step
////        PromptTemplate strTemplate = PromptTemplate.builder().template("What is {techName}? tell ma also about {techExample}").build();
////
////        //render the template
////
////        String renderedMessage = strTemplate.render(Map.of(
////                "techName", "Spring",
////                "techExample", "spring exception"
////        ));
////
////        // build the prompt
////        Prompt prompt = new Prompt(renderedMessage);
//
//        //return this.chatClient.prompt(prompt).call().content();
//     //   ____________________________________________________________________________
//
//
////        var systemPromptTemplate=SystemPromptTemplate.builder()
////                .template("You are a helpful coding assistant. You are an expert in coding.")
////                .build();
////        var systemMessage=systemPromptTemplate.createMessage();
////
////        var userPromptTemplate=PromptTemplate.builder().template("What is {techName}? tell ma also about {techExample}").build();
////        var userMessage=userPromptTemplate.createMessage(Map.of(
////                "techName", "Spring",
////                "techExample", "spring exception"
////        ));
////
////
////        Prompt prompt = new Prompt(systemMessage,userMessage);
        // retreving data from vector store based on user query and then passing that data as context in system prompt to get more relevant answer from model.
       //no need to ryt below code  u can use QA advisor for this, it will do all the below steps for u and also it will take care of prompt formatting as well as few shot examples if u want to provide, it also has option to provide chain of thought prompt to get better answer from model, it is recommended to use QA advisor instead of writing all the code below for retreving data from vector store and passing it as context in system prompt, but if u want to write ur own code then below code is an example of how to do it.
//
//        SearchRequest searchRequest = SearchRequest.builder().topK(5).similarityThreshold(0.6).query(query).build();
//        List<Document> documentList = vectorStore.similaritySearch(searchRequest);
//        List<String> contextList = documentList.stream().map(Document::getText).toList();
//        String context = String.join(",", contextList);

        return this.chatClient
                .prompt()
//                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"userId"))
//                .system(system ->
//                        system.text(this.systemMessage).param("documentSections", context)
//                )
                //use QA or if u want your custom code to retreive documents u can use searchRequest like below as well
               // .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder()
                                .topK(5)
                                .similarityThreshold(0.6)
                                .query(query)
                                .build())
                        .build())
                .user(user ->
                        user.text(userMessage.toString())
                                .param("concept", query)
                )
                .call()
                .content();
//
//




    }

    @Override
    public Flux<String> streamChat(String query) {
        return chatClient.
                prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"userId"))

                .system(system->
                                system.text(this.systemMessage))
                .user(user->user.text(this.userMessage).param("concept",query))
                .stream()
                .content();

    }

    @Override
    public void saveData(List<String> list) {
        //save data to db or do any post processing
        List<Document> documentList = list.stream().map(Document::new).toList();
        vectorStore.add(documentList);

    }
}
