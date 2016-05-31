package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.entities.QuDevice;

/**
 * Define methods to access the Devices table in database.
 * 
 * @author rom
 */
public interface DevicesRepository extends MongoRepository<QuDevice, String> {
	QuDevice findByDeviceId(String deviceId);
}
