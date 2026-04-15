package com.hrbu.aidemo.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(wiringMode = EXPLICIT,
        streamingChatModel = "qwenStreamingChatModel",
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        tools = "fuctionTolls")
public interface QwenAssistant {
    @SystemMessage(fromResource = "qwenprompt.txt")
    Flux<String> streamChat(@MemoryId Long memoryId, @UserMessage String message);
    String chat(String message);
}
