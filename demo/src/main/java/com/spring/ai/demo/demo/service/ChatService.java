package com.spring.ai.demo.demo.service;

import com.spring.ai.demo.demo.Entity.Tut;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {

//    List<Tut> message(String query);

    String chatTemplate(String query, String userId);

    Flux<String> streamChat(String query);

    void saveData(List<String> list);
}
