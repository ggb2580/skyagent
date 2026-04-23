package com.hrbu.aidemo;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EmbeddingTest {
    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    ContentRetriever contentRetriever;

    @Test
    public void test(){
        Response<Embedding> embed = embeddingModel.embed("你好");
        System.out.println("向量纬度："+embed.content().vector().length);
        System.out.println("向量输出:"+embed.toString());
    }
    @Test
    public void test01(){
        Query query = new Query("你是谁？");
        List<Content> retrieve = contentRetriever.retrieve(query);
        System.out.println(retrieve);
    }

}
