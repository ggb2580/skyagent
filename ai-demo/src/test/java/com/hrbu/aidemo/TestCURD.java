package com.hrbu.aidemo;


import com.hrbu.aidemo.entity.ChatMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootTest
public class TestCURD {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Test
    public void testQuery(){
        Criteria criteria = new Criteria("memoryId").is(18);
        Query query = new Query(criteria);
        List<ChatMessages> chatMessages = mongoTemplate.find(query, ChatMessages.class);
        for (ChatMessages chatMessages1 : chatMessages){
            System.out.println(chatMessages1.getContent());
        }
    }
}
