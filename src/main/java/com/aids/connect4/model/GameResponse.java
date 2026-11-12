package com.aids.connect4.model;

public class GameResponse {
    private int[][] board;
    private int aiCol;
    private String winner; // "NONE", "HUMAN", "AI", "DRAW"
    private long nodesEvaluated;
    private long executionTimeMs;

    public GameResponse(int[][] board, int aiCol, String winner, long nodesEvaluated, long executionTimeMs) {
        this.board = board;
        this.aiCol = aiCol;
        this.winner = winner;
        this.nodesEvaluated = nodesEvaluated;
        this.executionTimeMs = executionTimeMs;
    }

    public int[][] getBoard() { return board; }
    public int getAiCol() { return aiCol; }
    public String getWinner() { return winner; }
    public long getNodesEvaluated() { return nodesEvaluated; }
    public long getExecutionTimeMs() { return executionTimeMs; }
}
