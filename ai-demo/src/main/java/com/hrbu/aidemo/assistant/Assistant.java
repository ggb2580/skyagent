package com.hrbu.aidemo.assistant;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel ="openAiStreamingChatModel" )
public interface Assistant {
    String chat(String message);
    Flux<String> streamChat(String message);
}
