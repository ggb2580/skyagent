package com.hrbu.aidemo.config;

import com.hrbu.aidemo.store.MongoDBChatMemory;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {
    @Autowired
    MongoDBChatMemory memory;

    @Bean
    public ChatMemoryProvider chatMemoryProvider(){
        return memoryId->MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(memory)
                .build();
    }
}
