package com.agamera.treehugger.controller;

import com.agamera.treehugger.model.*;
import com.agamera.treehugger.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
// NO @CrossOrigin HERE (Handled by WebConfig)
public class GameController {

    @Autowired PlayerRepository playerRepo;
    @Autowired GameRoundRepository roundRepo;

    // 1. Auth (Login/Register) - BULLETPROOF VERSION
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody Map<String, String> data) {
        try {
            // Validate Inputs
            String u = data.get("username");
            String p = data.get("password");

            if (u == null || u.trim().isEmpty()) return ResponseEntity.badRequest().body("Username empty");
            if (p == null || p.trim().isEmpty()) return ResponseEntity.badRequest().body("Password empty");

            u = u.trim();
            p = p.trim();

            Player player = playerRepo.findByUsername(u);

            // CASE 1: LOGIN
            if (player != null) {
                if (player.getPassword() == null || !player.getPassword().equals(p)) {
                    return ResponseEntity.status(401).body("Wrong Password");
                }
                return ResponseEntity.ok(player);
            }

            // CASE 2: REGISTER
            Player newP = new Player();
            newP.setUsername(u);
            newP.setPassword(p);
            newP.setTotalScore(0.0);
            
            Player saved = playerRepo.save(newP);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            // Print error to Render Logs so we can see it
            e.printStackTrace();
            // Send safe error to user
            return ResponseEntity.status(500).body("Login Error: " + e.getMessage());
        }
    }

    // 2. Submit Score
    @PostMapping("/score")
    public ResponseEntity<?> submitScore(@RequestBody Map<String, Object> data) {
        try {
            String u = (String) data.get("username");
            Double roundScore = Double.parseDouble(data.get("time").toString());

            Player player = playerRepo.findByUsername(u);
            if (player != null) {
                Double currentTotal = player.getTotalScore() == null ? 0.0 : player.getTotalScore();
                player.setTotalScore(currentTotal + roundScore);
                playerRepo.save(player);

                GameRound round = new GameRound();
                round.setScore(roundScore);
                round.setPlayedAt(LocalDateTime.now());
                round.setPlayer(player);
                roundRepo.save(round);

                return ResponseEntity.ok(player);
            }
            return ResponseEntity.badRequest().body("Player not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Score Error: " + e.getMessage());
        }
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