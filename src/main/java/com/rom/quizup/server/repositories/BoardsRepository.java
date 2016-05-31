package com.rom.quizup.server.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.entities.QuBoard;

/**
 * Define methods to access the Boards table in database.
 * 
 * @author rom
 */
public interface BoardsRepository extends MongoRepository<QuBoard, String> {
	
	List<QuBoard> findByLevel(Integer level);

}
