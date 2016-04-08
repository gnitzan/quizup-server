package com.rom.quizup.server.models;

import com.rom.quizup.server.models.PlayerStatistics;

/**
 * Player resource. This class is used in the QuizUp API and is
 * projected to the clients in the generated client libraries.
 */
public class Player {
	private String id;
	private String nickname;
	private PlayerStatistics statistics = new PlayerStatistics(0, 0);

	/**
	 * Constructor
	 *
	 * @param id
	 *          player's resource id.
	 * @param nickname
	 *          player's nickname.
	 */
	public Player(String id, String nickname) {
		this.id = id;
		this.nickname = nickname;
		this.statistics = null;
	}

	/**
	 * Constructor
	 *
	 * @param id
	 *          player's resource id.
	 * @param nickName
	 *          player's nickname.
	 * @param statistics
	 *          player's game statistics.
	 */
	public Player(String id, String nickName, PlayerStatistics statistics) {
		this.id = id;
		this.nickname = nickName;
		this.statistics = statistics;
	}

	/**
	 * Gets the player's id.
	 *
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the player's nickname.
	 *
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Get the player's statistics.
	 *
	 */
	public PlayerStatistics getStatistics() {
		return statistics;
	}
	
	public static Player createPlayerFromModel(QuPlayer player) {
		PlayerStatistics stats = new PlayerStatistics(player.getMultiplayerGamesWon(), player.getMultiplayerGamesPlayed());
		
		return new Player(player.getId(), player.getNickname(), stats);
	}
}
