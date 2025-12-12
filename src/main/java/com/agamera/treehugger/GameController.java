package com.agamera.treehugger.controller;

import com.agamera.treehugger.model.*;
import com.agamera.treehugger.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired PlayerRepository playerRepo;
    @Autowired GameRoundRepository roundRepo;

    // 1. Auth (Login/Register)
    @PostMapping("/auth")
    public Player auth(@RequestBody Map<String, String> data) {
        String u = data.get("username");
        String p = data.get("password");
        
        Player player = playerRepo.findByUsername(u);
        if (player != null) {
            // Login Check
            if (!player.getPassword().equals(p)) throw new RuntimeException("Invalid Password");
            return player;
        }
        
        // Register New User (We removed Phone to simplify)
        Player newP = new Player();
        newP.setUsername(u);
        newP.setPassword(p);
        newP.setTotalScore(0.0);
        return playerRepo.save(newP);
    }

    // 2. Submit Score
    @PostMapping("/score")
    public Player submitScore(@RequestBody Map<String, Object> data) {
        String u = (String) data.get("username");
        Double roundScore = Double.parseDouble(data.get("time").toString());

        Player player = playerRepo.findByUsername(u);
        if (player != null) {
            // A. Update Cumulative Total (Using the new Getter/Setter)
            Double currentTotal = player.getTotalScore() == null ? 0.0 : player.getTotalScore();
            player.setTotalScore(currentTotal + roundScore);
            playerRepo.save(player);

            // B. Save History Entry
            GameRound round = new GameRound();
            round.setScore(roundScore);
            round.setPlayedAt(LocalDateTime.now());
            round.setPlayer(player);
            roundRepo.save(round);

            return player;
        }
        throw new RuntimeException("Player not found");
    }

    // 3. Global Leaderboard
    @GetMapping("/leaderboard")
    public List<Player> getLeaderboard() {
        return playerRepo.findTop10ByOrderByTotalScoreDesc();
    }

    // 4. Personal History
    @GetMapping("/history/{username}")
    public List<GameRound> getHistory(@PathVariable String username) {
        Player p = playerRepo.findByUsername(username);
        if(p == null) return new ArrayList<>();
        return roundRepo.findTop20ByPlayerOrderByPlayedAtDesc(p);
    }
}