package com.kimplecker.snake;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardService {

    private static final String DB_URL = "jdbc:sqlite:data/leaderboard.db";

    public LeaderboardService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS leaderboard (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player_name TEXT NOT NULL,
                    score INTEGER NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertScore(String playerName, int score) {
        String sql = """
                INSERT INTO leaderboard(player_name, score)
                VALUES(?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTopScores(int limit) {
        String sql = """
                SELECT player_name, score
                FROM leaderboard
                ORDER BY score DESC
                LIMIT ?
                """;

        List<String> scores = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                scores.add(rs.getString("player_name") + " : " + rs.getInt("score"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }
}