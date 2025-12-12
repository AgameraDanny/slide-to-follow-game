package com.agamera.treehugger.repository;

import com.agamera.treehugger.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUsername(String username);
    List<Player> findTop10ByOrderByTotalScoreDesc(); // Changed to TotalScore
}