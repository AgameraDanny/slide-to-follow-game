package com.agamera.treehugger.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_rounds")
public class GameRound {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;
    private LocalDateTime playedAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    // --- MANUAL GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public LocalDateTime getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDateTime playedAt) { this.playedAt = playedAt; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
}