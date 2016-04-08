package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.models.QuGamePlay;

public interface GamePlaysRepository extends MongoRepository<QuGamePlay, String> {

}
