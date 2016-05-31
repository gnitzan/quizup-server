package com.rom.quizup.server.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Board definition. This class is the only class which is
 * used as a model and also as an entity since its data is
 * used both on the client and server.
 * 
 * @author rom
 */

@Document(collection = "boards")
public class QuBoard {
	@Id
	private String id;
	
	private List<String> gridDefinitions;
	
	private List<String> questions;
	
	private List<String> answers;
	
	private Date dateCreated;
	
	@Indexed(unique = false)
	private Integer level;
	
	private Integer allottedTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBoardId() {
		return id.toString();
	}
	public List<String> getGridDefinitions() {
		return gridDefinitions;
	}
	public void setGridDefinitions(List<String> gridDefinitions) {
		this.gridDefinitions = gridDefinitions;
	}
	public List<String> getQuestions() {
		return questions;
	}
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getAllottedTime() {
		return allottedTime;
	}
	public void setAllottedTime(Integer allottedTime) {
		this.allottedTime = allottedTime;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
