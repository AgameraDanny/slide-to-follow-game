package com.agamera.treehugger.repository;

import com.agamera.treehugger.dto.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Fetch last 50 messages
    List<ChatMessage> findTop50ByOrderByTimestampDesc();
}