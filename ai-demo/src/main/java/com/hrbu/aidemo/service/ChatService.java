package com.hrbu.aidemo.service;

import com.hrbu.aidemo.dto.ChatFormDTO;
import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<String> chat(ChatFormDTO chatFormDTO);
}
