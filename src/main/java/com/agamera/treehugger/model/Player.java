package com.agamera.treehugger.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "players")
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    
    // New Logic: Cumulative Score
    private Double totalScore = 0.0;
    
    // Relationship to History
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents infinite loop when sending JSON
    private List<GameRound> history = new ArrayList<>();

    // --- MANUAL GETTERS & SETTERS (To fix your error) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }

    public List<GameRound> getHistory() { return history; }
    public void setHistory(List<GameRound> history) { this.history = history; }
}