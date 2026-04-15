package com.hrbu.aidemo;

import com.hrbu.aidemo.assistant.Assistant;
import com.hrbu.aidemo.assistant.QwenAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class AiDemoApplicationTests {
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
}
