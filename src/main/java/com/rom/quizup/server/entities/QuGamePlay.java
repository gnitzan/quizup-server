package com.rom.quizup.server.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rom.quizup.server.models.GamePlay;

/**
 * GamePlay entity represents one player's play as part of a game in the database.
 * A multiplayer game has multiple GamePlay entities. All GamePlay entities for
 * a given game belong to the entity group rooted at the game entity.
 */
@Document(collection="game_plays")
public class QuGamePlay {
	@Id
	String id;
	
	@DBRef
	private QuPlayer player;
	
	private List<Integer> correctAnswers;
	
	private Boolean finished = false;
	private Boolean winner = false;

	private Long timeLeft;
	private Date createDate;
	private Date lasModified;
	
	public QuGamePlay(QuPlayer player) {
		this.player = player;
		this.timeLeft = 0L;
		this.finished = false;
		this.correctAnswers = new ArrayList<>();
		this.createDate = this.lasModified = new Date();
	}
	
	public QuPlayer getPlayer() {
		return player;
	}
	public void setPlayer(QuPlayer player) {
		this.player = player;
		this.lasModified = new Date();
	}
	public List<Integer> getCorrectAnswers() {
		return correctAnswers;
	}
	public void setCorrectAnswers(List<Integer> answers) {
		this.correctAnswers = answers;
		this.lasModified = new Date();
	}
	
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
		this.lasModified = new Date();
	}
	
	public Long getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
		this.lasModified = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLasModified() {
		return lasModified;
	}

	public void setLasModified(Date lasModified) {
		this.lasModified = lasModified;
	}
	
	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

	public GamePlay getGamePlay() {
		return new GamePlay(this.player.getPlayer(), correctAnswers, finished, timeLeft, winner);
	}
}
