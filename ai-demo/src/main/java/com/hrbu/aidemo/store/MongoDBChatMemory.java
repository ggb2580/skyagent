package com.hrbu.aidemo.store;

import com.hrbu.aidemo.entity.ChatMessages;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MongoDBChatMemory implements ChatMemoryStore {
    @Autowired
    private MongoTemplate mongoTemplate;


    /*
    * 查询单条数据
    * */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        Criteria criteria = new Criteria("memoryId").is(memoryId);
        Query query = new Query(criteria);
        ChatMessages one = mongoTemplate.findOne(query, ChatMessages.class);
        if (one == null){
            return new ArrayList<>();
        }

        return ChatMessageDeserializer.messagesFromJson(one.getContent());
    }

    /*
    * 更新数据
    * */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {
        Criteria criteria = new Criteria("memoryId").is(memoryId);
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", ChatMessageSerializer.messagesToJson(list));
        mongoTemplate.upsert(query,update, ChatMessages.class);
    }

    /*
    *删除数据
    * */
    @Override
    public void deleteMessages(Object memoryId) {
        Criteria criteria = new Criteria("memoryId").is(memoryId);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }
}
