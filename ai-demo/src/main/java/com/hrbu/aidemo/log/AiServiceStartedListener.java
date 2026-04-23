package com.hrbu.aidemo.log;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiServiceStartedListener {

    @Bean
    ChatModelListener chatModelListener() {
        return new ChatModelListener() {

            private static final Logger log = LoggerFactory.getLogger(ChatModelListener.class);

            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                // 可选：记录请求的模型和消息数量（不包含 token 数，因为请求时还不知道）
                log.info("Chat request received. Messages count: {}", requestContext.chatRequest().messages().size());
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                // 从响应中获取 token 使用情况
                TokenUsage tokenUsage = responseContext.chatResponse().tokenUsage();
                if (tokenUsage != null) {
                    log.info("Token usage - Input: {}, Output: {}, Total: {}",
                            tokenUsage.inputTokenCount(),
                            tokenUsage.outputTokenCount(),
                            tokenUsage.totalTokenCount());
                } else {
                    log.warn("No token usage information available in the response.");
                }
                // 可选：记录完整响应（谨慎使用，避免日志过大）
                // log.info("Response: {}", responseContext.chatResponse());
            }

            @Override
            public void onError(ChatModelErrorContext errorContext) {
                log.error("Error during chat model invocation: {}", errorContext.error().getMessage(), errorContext.error());
            }
        };
    }
}