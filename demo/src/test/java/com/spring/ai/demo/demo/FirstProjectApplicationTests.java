package com.spring.ai.demo.demo;

import com.spring.ai.demo.demo.helpers.Helpers;
import com.spring.ai.demo.demo.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirstProjectApplicationTests {
    @Autowired
    ChatService chatService;

	@Test
	void contextLoads() {
	}

    @Test
    void saveDataTest(){
        System.out.println("adding data to db");
        chatService.saveData(Helpers.getData());
        System.out.println("data added to db");
    }

}
