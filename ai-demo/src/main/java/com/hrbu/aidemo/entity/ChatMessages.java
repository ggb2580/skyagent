package com.hrbu.aidemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("chat_messages")
public class ChatMessages {
    //唯一标识ID 映射到MongoDB 文档的 _id字段
    @Id
    private Object id;
    private Integer messageId;
    //存储当前聊天记录列表的json字符串
    private String content;
}
