package com.rom.quizup.server.models;

import java.io.Serializable;
import java.util.List;

import com.rom.quizup.server.entities.QuBoard;

/**
 * Game definition projected between server and clients.
 */
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private QuBoard board;
	private List<GamePlay> gamePlays;

	/**
	 * Constructor
	 *
	 * @param id
	 *          the id of the game.
	 * @param board
	 *          the board used in this game.
	 * @param gamePlays
	 *          the list of game plays from each player in this game.
	 */
	public Game(String id, QuBoard board, List<GamePlay> gamePlays) {
		this.id = id;
		this.board = board;
		this.gamePlays = gamePlays;
	}

	/**
	 * Gets the game id.
	 *
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the board used for this game.
	 *
	 */
	public QuBoard getBoard() {
		return board;
	}

	/**
	 * Gets the list of game plays from each player in this game.
	 *
	 */
	public List<GamePlay> getGamePlays() {
		return gamePlays;
	}
}