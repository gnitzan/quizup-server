package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.entities.QuGame;

/**
 * Define methods to access the Games table in database.
 * 
 * @author rom
 */
public interface GamesRepository extends MongoRepository<QuGame, String> {
}
