package com.hrbu.aidemo.service.impl;

import com.hrbu.aidemo.assistant.QwenAssistant;
import com.hrbu.aidemo.dto.ChatFormDTO;
import com.hrbu.aidemo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private QwenAssistant assistant;
    @Override
    public Flux<String> chat(ChatFormDTO chatFormDTO) {
        return assistant.streamChat(chatFormDTO.getId(),chatFormDTO.getMessage());
    }
}
