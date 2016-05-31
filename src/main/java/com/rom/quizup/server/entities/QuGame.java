package com.rom.quizup.server.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rom.quizup.server.models.Game;
import com.rom.quizup.server.models.GamePlay;

@Document(collection="games")
public class QuGame {
	@Id
	private String id;
	
	// the board played
	@Indexed
	@DBRef
	private QuBoard board;
	
	// the player who won
	@DBRef
	private QuPlayer playerWon;
	
	@DBRef
	private List<QuGamePlay> gamePlays;
	
	private Date createDate;
	
	private Date lastModified;

	public QuGame(QuBoard board) {
		this.board = board;
		this.gamePlays = new ArrayList<QuGamePlay>();
		this.createDate = new Date();
		this.lastModified = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public QuBoard getBoard() {
		return board;
	}

	public void setBoard(QuBoard board) {
		this.board = board;
		this.lastModified = new Date();
	}

	public QuPlayer getPlayerWon() {
		return playerWon;
	}

	public void setPlayerWon(QuPlayer playerWon) {
		this.playerWon = playerWon;
		this.lastModified = new Date();
	}

	public List<QuGamePlay> getGamePlays() {
		return gamePlays;
	}

	public void setGamePlays(List<QuGamePlay> gamePlays) {
		this.gamePlays = gamePlays;
		this.lastModified = new Date();
	}
	
	public void addGamePlay(QuGamePlay gamePlay) {
		this.gamePlays.add(gamePlay);
		this.lastModified = new Date();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Game getGame() {
		List<GamePlay> gp = new ArrayList<>();
		for(QuGamePlay qgp : this.gamePlays) {
			gp.add(qgp.getGamePlay());
		}
		
		return new Game(id, board, gp);
	}
}
