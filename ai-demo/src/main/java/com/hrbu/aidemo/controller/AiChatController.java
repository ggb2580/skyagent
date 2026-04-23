package com.hrbu.aidemo.controller;

import com.hrbu.aidemo.dto.ChatFormDTO;
import com.hrbu.aidemo.service.ChatService;
import com.hrbu.aidemo.service.impl.ChatServiceImpl;
//import com.hrbu.aidemo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class AiChatController {
    @Autowired
    private ChatService chatService;
    @PostMapping(value = "/aichat",produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatFormDTO chatFormDTO){
        return chatService.chat(chatFormDTO);
    }
//    @Autowired
//    private JWTUtil jwtUtil;

//    // 获取真实有效的 Token
//    @GetMapping("/token")
//    public String generateToken() {
//        return jwtUtil.generateToken("test-user");
//    }
}
