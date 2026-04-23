package com.hrbu.aidemo.listener;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hrbu.aidemo.util.UserInfoConstant.*;

@Component
public class TokenListener implements ChatModelListener {
    private static final Logger log = LoggerFactory.getLogger(TokenListener.class);

    /*
    * 请求发送前触发
    * */
    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        //1.获取原始请求
        ChatRequest chatRequest = requestContext.chatRequest();
        List<ChatMessage> messages = chatRequest.messages();
        log.info("=====================================================");
        log.info("                 AI请求开始");
        log.info("=====================================================");

        for (ChatMessage chatMessage : messages){
            String name = chatMessage.type().name();
            String text = extractText(chatMessage);

            log.info("{type}:" +name);
            log.info("{message}:"+text);
        }

    }

    /*
    *响应返回后触发
    * */
    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        ChatResponse chatResponse = responseContext.chatResponse();

        TokenUsage tokenUsage = chatResponse.tokenUsage();
        log.info("====================================================");
        log.info("                AI响应开始");
        log.info("====================================================");
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info(aiMessage.text());
        Integer input = tokenUsage.inputTokenCount();
        Integer output = tokenUsage.outputTokenCount();
        Integer total = input + output;
        // 通义千问计费
        // 输入 1 token 价格
        double inputCost = input * 0.00001;
        // 输出 1 token 价格
        double outputCost = output * 0.000015;
        double totalCost = inputCost + outputCost;

        log.info("==================================================");
        log.info("                  Token 统计                      ");
        log.info("==================================================");
        log.info("\n输入Token：{}", input);
        log.info("\n输出Token：{}", output);
        log.info("\n总计Token：{}", total);
        log.info("\n预估费用：≈ ¥{}", String.format("%.4f", totalCost));
    }

    /*
    * 出现异常时触发
    * */
    @Override
    public void onError(ChatModelErrorContext errorContext) {
        ChatModelListener.super.onError(errorContext);
    }

    /**
     * 抽取消息文本
     */
    private String extractText(ChatMessage msg) {
        if (msg instanceof UserMessage userMsg) {
            return userMsg.singleText();
        }
        return msg.toString();
    }

    /*
    * 过滤敏感信息
    * */
    private String desensitize(String text){
        if (text == null || text.isEmpty()){
            return null;
        }

        text = ID_CARD.matcher(text).replaceAll("*************************");
        text = PHONE.matcher(text).replaceAll("1***************");
        text = SECRET_KEY.matcher(text).replaceAll("*****************");
        text = PASSWORD.matcher(text).replaceAll("******************");
        return text;
    }
}
