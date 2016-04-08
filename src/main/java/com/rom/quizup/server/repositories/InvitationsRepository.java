package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.models.QuInvitation;

public interface InvitationsRepository extends MongoRepository<QuInvitation, String> {

}
