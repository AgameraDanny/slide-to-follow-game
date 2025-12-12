package com.agamera.treehugger.controller;

import com.agamera.treehugger.dto.ChatMessage;
import com.agamera.treehugger.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatRepo;

    // 1. WebSocket: Receive Message -> Save to DB -> Broadcast
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatRepo.save(chatMessage); // <--- SAVING HERE
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage) {
        chatMessage.setContent("joined the server!");
        chatMessage.setTimestamp(LocalDateTime.now());
        // We don't necessarily need to save "Join" messages to DB, but you can if you want:
        // return chatRepo.save(chatMessage); 
        return chatMessage;
    }

    // 2. REST API: Load History
    @GetMapping("/api/chat/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory() {
        // Get last 50, but they come out Newest->Oldest. 
        // We reverse them so they show up Oldest->Newest in the chat box.
        List<ChatMessage> history = chatRepo.findTop50ByOrderByTimestampDesc();
        Collections.reverse(history);
        return history;
    }
}