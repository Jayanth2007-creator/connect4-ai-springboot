package com.aids.connect4.controller;

import com.aids.connect4.model.GameResponse;
import com.aids.connect4.service.Connect4AIService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameController {

    private final Connect4AIService aiService;

    public GameController(Connect4AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/move")
    public GameResponse makeMove(@RequestBody Map<String, Object> payload) {
        // Parse 2D board from JSON
        var rawBoard = (java.util.List<java.util.List<Integer>>) payload.get("board");
        int[][] board = new int[6][7];
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                board[r][c] = rawBoard.get(r).get(c);
            }
        }

        int col = (int) payload.get("col");
        int depth = (int) payload.get("depth");

        return aiService.processMove(board, col, depth);
    }
}
