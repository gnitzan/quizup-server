package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.entities.QuGamePlay;

/**
 * Define methods to access the GamePlays table in database.
 * 
 * @author rom
 */
public interface GamePlaysRepository extends MongoRepository<QuGamePlay, String> {
}
