package com.rom.quizup.server.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A service which provides the @see {@link ObjectMapper} which is
 * JSON to/from Java object mapper.
 * 
 * Instead of constructing a mapper each time we need it,
 * we instantiate it one and provide the instance as a service.
 * 
 * @author rom
 *
 */
@Service
public class MapperService {
	private ObjectMapper mapper = new ObjectMapper();

	public ObjectMapper getMapper() {
		return mapper;
	}
}
