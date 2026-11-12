package com.aids.connect4.service;

import com.aids.connect4.model.GameResponse;
import org.springframework.stereotype.Service;

@Service
public class Connect4AIService {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int EMPTY = 0;
    private static final int HUMAN = 1;
    private static final int AI = 2;

    private long nodesEvaluated;

    public GameResponse processMove(int[][] board, int humanCol, int depth) {
        long startTime = System.currentTimeMillis();
        nodesEvaluated = 0;

        // 1. Apply Human Move
        if (humanCol >= 0 && humanCol < COLS && isValidMove(board, humanCol)) {
            dropPiece(board, humanCol, HUMAN);
        }

        if (checkWin(board, HUMAN)) {
            return new GameResponse(board, -1, "HUMAN", 0, System.currentTimeMillis() - startTime);
        }
        if (isBoardFull(board)) {
            return new GameResponse(board, -1, "DRAW", 0, System.currentTimeMillis() - startTime);
        }

        // 2. Calculate Best AI Move using Minimax + Alpha-Beta Pruning
        int bestCol = getBestMove(board, depth);
        if (bestCol != -1) {
            dropPiece(board, bestCol, AI);
        }

        long elapsed = System.currentTimeMillis() - startTime;

        if (checkWin(board, AI)) {
            return new GameResponse(board, bestCol, "AI", nodesEvaluated, elapsed);
        }
        if (isBoardFull(board)) {
            return new GameResponse(board, bestCol, "DRAW", nodesEvaluated, elapsed);
        }

        return new GameResponse(board, bestCol, "NONE", nodesEvaluated, elapsed);
    }

    private int getBestMove(int[][] board, int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        // Prioritize center columns first for better Alpha-Beta pruning efficiency
        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};

        for (int col : colOrder) {
            if (isValidMove(board, col)) {
                int row = dropPiece(board, col, AI);
                int score = minimax(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board[row][col] = EMPTY; // Undo move

                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }
        return bestCol;
    }

    private int minimax(int[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        nodesEvaluated++;

        if (checkWin(board, AI)) return 100000 + depth;
        if (checkWin(board, HUMAN)) return -100000 - depth;
        if (isBoardFull(board) || depth == 0) {
            return evaluateBoard(board);
        }

        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : colOrder) {
                if (isValidMove(board, col)) {
                    int row = dropPiece(board, col, AI);
                    int eval = minimax(board, depth - 1, alpha, beta, false);
                    board[row][col] = EMPTY;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : colOrder) {
                if (isValidMove(board, col)) {
                    int row = dropPiece(board, col, HUMAN);
                    int eval = minimax(board, depth - 1, alpha, beta, true);
                    board[row][col] = EMPTY;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }

    // Heuristic Evaluation Function for non-terminal states
    private int evaluateBoard(int[][] board) {
        int score = 0;

        // Bonus for controlling the center column
        for (int r = 0; r < ROWS; r++) {
            if (board[r][COLS / 2] == AI) score += 3;
        }

        // Evaluate all 4-cell windows (Horizontal, Vertical, Diagonal)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                score += scoreWindow(board[r][c], board[r][c+1], board[r][c+2], board[r][c+3]);
            }
        }
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS - 3; r++) {
                score += scoreWindow(board[r][c], board[r+1][c], board[r+2][c], board[r+3][c]);
            }
        }
        for (int r = 0; r < ROWS - 3; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                score += scoreWindow(board[r][c], board[r+1][c+1], board[r+2][c+2], board[r+3][c+3]);
                score += scoreWindow(board[r+3][c], board[r+2][c+1], board[r+1][c+2], board[r][c+3]);
            }
        }
        return score;
    }

    private int scoreWindow(int c1, int c2, int c3, int c4) {
        int aiCount = 0, humanCount = 0, emptyCount = 0;
        int[] window = {c1, c2, c3, c4};

        for (int cell : window) {
            if (cell == AI) aiCount++;
            else if (cell == HUMAN) humanCount++;
            else emptyCount++;
        }

        if (aiCount == 4) return 100;
        if (aiCount == 3 && emptyCount == 1) return 5;
        if (aiCount == 2 && emptyCount == 2) return 2;
        if (humanCount == 3 && emptyCount == 1) return -4; // Block opponent threats
        return 0;
    }

    private boolean isValidMove(int[][] board, int col) {
        return board[0][col] == EMPTY;
    }

    private int dropPiece(int[][] board, int col, int player) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == EMPTY) {
                board[r][col] = player;
                return r;
            }
        }
        return -1;
    }

    private boolean isBoardFull(int[][] board) {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == EMPTY) return false;
        }
        return true;
    }

    private boolean checkWin(int[][] board, int player) {
        // Horizontal, Vertical, and Diagonal checks
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == player && board[r][c+1] == player && board[r][c+2] == player && board[r][c+3] == player) return true;
            }
        }
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS - 3; r++) {
                if (board[r][c] == player && board[r+1][c] == player && board[r+2][c] == player && board[r+3][c] == player) return true;
            }
        }
        for (int r = 0; r < ROWS - 3; r++) {
            for (int c = 0; c < COLS - 3; c++) {
                if (board[r][c] == player && board[r+1][c+1] == player && board[r+2][c+2] == player && board[r+3][c+3] == player) return true;
                if (board[r+3][c] == player && board[r+2][c+1] == player && board[r+1][c+2] == player && board[r][c+3] == player) return true;
            }
        }
        return false;
    }
}
