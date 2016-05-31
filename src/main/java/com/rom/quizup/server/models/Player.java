package com.rom.quizup.server.models;

import com.rom.quizup.server.models.PlayerStatistics;

/**
 * Player resource. This class is used in the QuizUp API and is
 * projected to the clients in the generated client libraries.
 */
public class Player {
	private String id;
	private String nickname;
	private PlayerStatistics statistics;
	private String imageUrl;

	/**
	 * Constructor
	 *
	 * @param id
	 *          player's resource id.
	 * @param nickname
	 *          player's nickname.
	 */
	public Player(String id, String nickname, String imgUrl) {
		this(id, nickname, null, imgUrl);
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
	public Player(String id, String nickName, PlayerStatistics statistics, String imgUrl) {
		this.id = id;
		this.nickname = nickName;
		
		if (statistics == null) {
			// initialize statistics in case we got null on new player
			statistics = new PlayerStatistics(0, 0);
		}
		else {
			this.statistics = statistics;
		}
		this.imageUrl = imgUrl;
	}

	/**
	 * Gets the player's id.
	 *
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the player's id.
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the player's image URL (Google provided).
	 * @return
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Set the player's image URL (Google provided)
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Get the player's nickname.
	 *
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Set player's nick name
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Get the player's statistics of games played and won.
	 *
	 */
	public PlayerStatistics getStatistics() {
		return statistics;
	}

	/**
	 * Set player's statistics of games played and won
	 * @param statistics
	 */
	public void setStatistics(PlayerStatistics statistics) {
		this.statistics = statistics;
	}

}
