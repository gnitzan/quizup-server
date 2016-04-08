package com.rom.quizup.server.models;

import com.rom.quizup.server.utilities.InvitationStatus;

public class Invitation {
  private String invitationId;
  private String gameId;
  private InvitationStatus status = InvitationStatus.INITIALIZED;

	public Invitation() {
	}

	public Invitation(QuInvitation i) {
		this.invitationId = i.getId();
		this.gameId = i.getGame().getId();
		this.status = i.getStatus();
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public InvitationStatus getStatus() {
		return status;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
	}	
}
