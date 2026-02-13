package com.rinbowxp.app.game_logic;

/**
 * Represents the current status of a game session.
 */
public enum GameStatus {
    /**
     * The game is currently in progress.
     */
    RUNNING,
    
    /**
     * The player has won the game.
     */
    WON,
    
    /**
     * The player has lost the game.
     */
    LOST
}
