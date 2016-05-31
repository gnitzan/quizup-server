package com.rom.quizup.server.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rom.quizup.server.utilities.InvitationStatus;

/**
 * The definition of Invitation table in the database.
 * 
 * @author rom
 *
 */
@Document(collection="invitations")
public class QuInvitation {
	@Id
	private String id;
	
	@Indexed
	@DBRef
	private QuPlayer sender;
	
	@Indexed
	@DBRef
	private QuPlayer receipient;
	
	@DBRef
	private QuGame game;
	
	private String gcmMessageId;

	private InvitationStatus status;
	
	private Date createDate;
	
	private Date lastModified;
	
	public QuInvitation(QuPlayer sender, QuPlayer receipient, QuGame game) {
		this.sender = sender;
		this.receipient = receipient;
		this.game = game;
		this.status = InvitationStatus.INITIALIZED;
		this.createDate = this.lastModified = new Date();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public QuPlayer getSender() {
		return sender;
	}
	public void setSender(QuPlayer sender) {
		this.sender = sender;
		this.lastModified = new Date();
	}
	public QuPlayer getReceipient() {
		return receipient;
	}
	public void setReceipient(QuPlayer receipient) {
		this.receipient = receipient;
		this.lastModified = new Date();
	}
	public QuGame getGame() {
		return game;
	}
	public void setGame(QuGame game) {
		this.game = game;
		this.lastModified = new Date();
	}

	public InvitationStatus getStatus() {
		return status;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
		this.lastModified = new Date();
	}
	
	public boolean cancel() {
		if (this.status.equals(InvitationStatus.ACCEPTED)) {
			return false;
		}
		
		this.status = InvitationStatus.CANCELED;
		this.lastModified = new Date();
		return true;
	}
	
	public boolean decline() {
		if (this.status.equals(InvitationStatus.ACCEPTED)) {
			return false;
		}
		
		this.status = InvitationStatus.DECLINED;
		this.lastModified = new Date();
		return true;
	}
	
	public boolean accept() {
		if (this.status.equals(InvitationStatus.DECLINED) || this.status.equals(InvitationStatus.CANCELED)) {
			return false;
		}
		
		this.status = InvitationStatus.ACCEPTED;
		this.lastModified = new Date();
		return true;
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

	public String getGcmMessageId() {
		return gcmMessageId;
	}

	public void setGcmMessageId(String gcmMessageId) {
		this.gcmMessageId = gcmMessageId;
		this.lastModified = new Date();
	}	

}
