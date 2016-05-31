package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.entities.QuInvitation;

/**
 * Define methods to access the Invitations table in database.
 * 
 * @author rom
 */
public interface InvitationsRepository extends MongoRepository<QuInvitation, String> {
}
