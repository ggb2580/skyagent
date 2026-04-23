package com.hrbu.aidemo;


import com.hrbu.aidemo.assistant.Assistant;
import com.hrbu.aidemo.assistant.QwenAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class AiDemoApplicationTests {
    @Resource
    private QwenChatModel qwenChatModel;
    @Autowired
    Assistant assistant;
    @Autowired
    QwenAssistant qwenAssistant;

    @Test
    public void test(){
//        String chat = assistant.chat("你是谁啊?");
//        System.out.println(chat);
        Flux<String> stringFlux = assistant.streamChat("你是什么大模型，说出你的版本");
        // 必须订阅，才能一字一字输出
        stringFlux.doOnNext(System.out::print)
                .doOnComplete(() -> System.out.println("\n=== 流式回答结束 ==="))
                .blockLast(); // 测试里必须加这个，否则程序直接结束看不到结果
    }
    @Test
    public void test01(){
        String chat = qwenAssistant.chat("计算12加8");
        System.out.println(chat);

    }

    @Test
    public void test02(){
        Flux<String> stringFlux = qwenAssistant.streamChat(2L, "我的token用了多少");
        System.out.println(stringFlux);
    }

    @Test
    public void test03(){
        Flux<String> stringFlux = qwenAssistant.streamChat(3L, "我的token用了多少");
        stringFlux.doOnNext(System.out::print)
                .doOnComplete(() -> System.out.println("\n=== 流式回答结束 ==="))
                .blockLast();
    }

}
