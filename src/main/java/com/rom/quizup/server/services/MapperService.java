package com.rom.quizup.server.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MapperService {
	private ObjectMapper mapper = new ObjectMapper();

	public ObjectMapper getMapper() {
		return mapper;
	}
}
