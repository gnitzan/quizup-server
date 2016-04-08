package com.rom.quizup.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rom.quizup.server.models.QuDevice;

public interface DevicesRepository extends MongoRepository<QuDevice, String> {
	QuDevice findByDeviceId(String deviceId);
}
