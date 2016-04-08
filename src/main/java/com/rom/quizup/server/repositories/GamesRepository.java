package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.models.QuGame;

public interface GamesRepository extends MongoRepository<QuGame, String> {

}
