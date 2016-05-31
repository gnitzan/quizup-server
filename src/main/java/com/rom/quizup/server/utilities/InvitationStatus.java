package com.rom.quizup.server.utilities;

import com.rom.quizup.server.models.Invitation;

/**
 * Enum that represents the status of @see {@link Invitation}
 * 
 * @author rom
 *
 */
public enum InvitationStatus {
	INITIALIZED, SENT, ACCEPTED, DECLINED, CANCELED;
}
