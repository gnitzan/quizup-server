package com.rom.quizup.server.models;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing the status of a game play as reported by the client. This
 * class is used in the Quizup API and is serialized between server and client.
 */
public class GamePlayStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Integer> correctAnswers;
	private long timeLeft;

	/**
	 * Default constructor.
	 */
	public GamePlayStatus() {
	}

	/**
	 * Constructor.
	 *
	 * @param correctAnswers
	 *          the list of player's correct answers.
	 * @param timeLeft
	 *          time in milliseconds still left when the player submitted the
	 *          answers.
	 */
	public GamePlayStatus(List<Integer> correctAnswers, long timeLeft) {
		this.correctAnswers = correctAnswers;
		this.timeLeft = timeLeft;
	}

	/**
	 * Gets the list of correct answers.
	 *
	 */
	public List<Integer> getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * Gets the time in milliseconds still left when the player submitted the
	 * answers.
	 *
	 */
	public long getTimeLeft() {
		return timeLeft;
	}
	
	/**
	 * Overriding toString for logging purposes
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n-----------------------------------------\n");
		sb.append("GamePlayStatus:\n");
		sb.append("timeLeft       : ").append(timeLeft).append("\n");
		int j = 0;
		for (Integer i : correctAnswers) {
			sb.append("correctAnswer[").append(j++).append("]    ").append(i).append("\n");
		}
		sb.append("-----------------------------------------\n");
		return sb.toString();
	}
}
