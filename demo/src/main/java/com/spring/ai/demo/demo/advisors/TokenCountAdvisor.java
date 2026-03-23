package com.spring.ai.demo.demo.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class TokenCountAdvisor implements CallAdvisor, StreamAdvisor {

    private Logger logger = LoggerFactory.getLogger(TokenCountAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logger.info("my token count advisor is called");

        logger.info("request:" + chatClientRequest.prompt().getContents());
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        logger.info("response received from the model");


        logger.info("response:" + chatClientResponse.chatResponse().getResult().getOutput().getText());

        logger.info("token consumed:" + chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens());
      return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        logger.info("my token count stream advisor is called");

        logger.info("request:" + chatClientRequest.prompt().getContents());
        Flux<ChatClientResponse> responseFlux = streamAdvisorChain.nextStream(chatClientRequest);

         logger.info("response received from the model");

         return responseFlux;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
