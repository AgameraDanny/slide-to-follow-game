package com.agamera.treehugger.repository;
import com.agamera.treehugger.model.GameRound;
import com.agamera.treehugger.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    List<GameRound> findTop20ByPlayerOrderByPlayedAtDesc(Player player);
}