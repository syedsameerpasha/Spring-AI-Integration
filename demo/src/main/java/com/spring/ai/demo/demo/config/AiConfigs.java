package com.spring.ai.demo.demo.config;

import com.spring.ai.demo.demo.advisors.TokenCountAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfigs {

    private Logger logger = LoggerFactory.getLogger(AiConfigs.class);

    //IF U WANT YOUR CUSTOM MAX MESSAGES IN CHAT MEMORY, THEN U CAN CREATE YOUR OWN CHAT MEMORY IMPLEMENTATION AND RETURN ITS BEAN HERE, OTHERWISE DEFAULT CHAT MEMORY IMPLEMENTATION WILL BE USED WHICH HAS MAX 20 MESSAGES.
//
//    @Bean
//    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
//        return MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).maxMessages(10).build();
//
//    }
    //in memory chat memory implementation, all the messages will be stored in the memory and it will be lost once the application is restarted, so it is not recommended for production use, but it is useful for testing and development purposes.
    @Bean
    public ChatMemory chatMemory(InMemoryChatMemoryRepository inMemoryChatMemoryRepository){
        return MessageWindowChatMemory.builder().chatMemoryRepository(inMemoryChatMemoryRepository).maxMessages(10).build();
    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {

        logger.info("chat memory implementation class: {}", chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder
                .defaultAdvisors(messageChatMemoryAdvisor,new TokenCountAdvisor(),new SafeGuardAdvisor(List.of("games")))
//                .defaultSystem("You are a helpful coding assistant. You are an expert in coding.")
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(200)
                        .build())
                .build();
    }

//    @Bean(name = "openAiChatClient")
//    public ChatClient chatClient(OpenAiChatModel chatModel){
//        return ChatClient.builder(chatModel).build();
//    }
//
//    @Bean(name = "ollamaChatClient")
//    public ChatClient ollamaChatModel(OllamaChatModel chatModel){
//        return ChatClient.builder(chatModel).build();
//    }
}
