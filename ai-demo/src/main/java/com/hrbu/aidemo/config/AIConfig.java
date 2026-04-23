package com.hrbu.aidemo.config;

import com.hrbu.aidemo.assistant.SqlAgent;
import com.hrbu.aidemo.listener.TokenListener;
import com.hrbu.aidemo.tool.DatabaseQueryTool;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AIConfig {
    @Autowired
    private ChatModelListener chatModelListener;

    // OpenAI 配置
    @Value("${langchain4j.open-ai.chat-model.api-key:demo}")
    private String openAiApiKey;

    @Value("${langchain4j.open-ai.chat-model.model-name:gpt-4o-mini}")
    private String openAiModelName;

    @Value("${langchain4j.open-ai.chat-model.base-url:http://langchain4j.dev/demo/openai/v1}")
    private String openAiBaseUrl;

    @Autowired
    private TokenListener tokenListener;

    // DashScope 配置
    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String dashScopeApiKey;

    @Value("${langchain4j.community.dashscope.chat-model.model-name:qwen-max}")
    private String dashScopeModelName;

    //embeddingModel
    @Value("${langchain4j.community.dashscope.embedding-model.api-key}")
    private String embeddingModelApiKey;

    @Value("${langchain4j.community.dashscope.embedding-model.model-name:text-embedding-v1}")
    private String embeddingName;




    // 创建 OpenAI 的 Http 客户端（JDK 实现）
    @Bean
    public JdkHttpClient jdkHttpClient() {
        return JdkHttpClient.builder().build();
    }


    // OpenAI 非流式模型
    @Bean("openAiChatModel")
    public ChatModel openAiChatModel(JdkHttpClient jdkHttpClient) {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(openAiModelName)
                .listeners(List.of(chatModelListener))
                .baseUrl(openAiBaseUrl)
                .build();
    }

    // OpenAI 流式模型
    @Bean("openAiStreamingChatModel")
    public StreamingChatModel openAiStreamingChatModel(JdkHttpClient jdkHttpClient) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(openAiModelName)
                .listeners(List.of(chatModelListener))
                .baseUrl(openAiBaseUrl)
                .build();
    }

    // 通义千问非流式模型
    @Bean("myqwenChatModel")
    public ChatModel qwenChatModel() {
        return QwenChatModel.builder()
                .apiKey(dashScopeApiKey)
                .listeners(List.of(chatModelListener,tokenListener))
                .modelName(dashScopeModelName)
                .temperature(0.1F) //温度【思考程度 → 越低越快】
                .maxTokens(4096) //限制回答长度 【限制回答长度 → 越短越快】
                                // 作用：限制模型最多生成多少个词 1024 → 大概
                                //300～500 中文字 2048 → 更长回答，但更慢 4096 → 最长，最慢
                .enableSearch(true) //关闭联网搜索
                .build();
    }

    // 通义千问流式模型
    @Bean("myqwenStreamingChatModel")
    public StreamingChatModel qwenStreamingChatModel() {
         return QwenStreamingChatModel.builder()
                .apiKey(dashScopeApiKey)
                .listeners(List.of(chatModelListener,tokenListener))
                .modelName(dashScopeModelName)
                 .temperature(0.1F) //温度【思考程度 → 越低越快】
                 .maxTokens(4096) //限制回答长度 【限制回答长度 → 越短越快】
                 // 作用：限制模型最多生成多少个词 1024 → 大概
                 //300～500 中文字 2048 → 更长回答，但更慢 4096 → 最长，最慢
                 .enableSearch(true) //关闭联网搜索
                .build();

    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingModel embeddingModel){
        //加载知识库
        Document document1 = FileSystemDocumentLoader.loadDocument("E:\\ocdata\\Desktop\\ai-demo\\src\\main\\resources\\biography-of-john-doe.txt");
        Document document2 = FileSystemDocumentLoader.loadDocument("E:\\ocdata\\Desktop\\ai-demo\\src\\main\\resources\\miles-of-smiles-terms-of-use.txt");
        List<Document> list = Arrays.asList(document1, document2);

        //切片
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        //向量
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel(){
        return QwenEmbeddingModel.builder()
                .modelName(embeddingName)
                .apiKey(embeddingModelApiKey)
                .build();
    }



}