package com.spring.ai.demo.demo.service;

import com.spring.ai.demo.demo.Entity.Tut;

import java.util.List;

public interface ChatService {

    List<Tut> message(String query);
}
