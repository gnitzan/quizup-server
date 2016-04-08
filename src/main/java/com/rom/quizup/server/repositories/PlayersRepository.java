package com.rom.quizup.server.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.rom.quizup.server.models.QuPlayer;

public interface PlayersRepository extends MongoRepository<QuPlayer, String> {
	
	QuPlayer findByEmail(String email);
	
	QuPlayer findByGooglePlusId(String googlePlusId);
	
	QuPlayer findByToken(String token);
	
	@Query("{ $and : [{ 'active' : true }, { 'email' : { '$ne' : ?0 } }]}")
	List<QuPlayer> findActivePlayers(String playerToIgnore);
}
