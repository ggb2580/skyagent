package com.hrbu.aidemo.listener;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

//TODO 实现请求最大Token数限制
//TODO
public class BugetListener {
    @Resource
    private StreamingChatModel streamingChatModel;

    public void streamChatModelStop(StreamingChatModel streamingChatModel){
        streamingChatModel.chat("1233",new StreamingChatResponseHandler() {

            /*
            * 触发时机：每当收到一个文本片段（如一个或几个 token）时调用。
            * */
            @Override
            public void onPartialResponse(String partialResponse) {
                StreamingChatResponseHandler.super.onPartialResponse(partialResponse);
            }

            /*
            * 触发时机：同样是收到文本片段，但额外提供了 context 对象，可以控制流式处理。
            * 典型用途：如果需要提前终止流式输出（例如用户点击“停止”按钮），可以调用
            * */
            @Override
            public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
                context.streamingHandle().cancel();
            }

            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                StreamingChatResponseHandler.super.onPartialThinking(partialThinking);
            }

            @Override
            public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
                StreamingChatResponseHandler.super.onPartialThinking(partialThinking, context);
            }

            @Override
            public void onPartialToolCall(PartialToolCall partialToolCall) {
                StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall);
            }

            /*
            * 触发时机：模型正在生成工具调用参数（如函数参数）时，每次收到一个参数片段调用一次。
            * 典型用途：边接收边解析参数，或者实时校验。带 context 的版本可取消调用。
            *
            * */
            @Override
            public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
                context.streamingHandle().cancel();
            }

            /*
            * 触发时机：一个完整的工具调用（所有参数都已接收完毕）时调用一次。
            * 典型用途：执行实际工具（如调用 API、查数据库），然后将结果返回给模型。
            * */
            @Override
            public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
