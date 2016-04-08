package com.rom.quizup.server.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rom.quizup.server.models.Player;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.services.DeviceService;
import com.rom.quizup.server.services.GoogleTokenService;
import com.rom.quizup.server.services.NotFoundException;
import com.rom.quizup.server.services.PlayerService;


@RestController
@RequestMapping("/players")
public class PlayersController {
	private static Logger LOG = LoggerFactory.getLogger(PlayersController.class);
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private GoogleTokenService tokenService;
	
	@Autowired
	private DeviceService deviceService;

	public PlayersController() {
	}
	
	@RequestMapping(method = RequestMethod.POST, path="/assign/{plusId}")
	public Map<String, Boolean> assignGooglePlusId(@PathVariable String plusId, @RequestBody User user) throws NotFoundException {
		
		playerService.assignPlusId(user, plusId);
		
		return Collections.singletonMap("success", true);
	}

	@RequestMapping(method = RequestMethod.POST, path="/register")
	public Player registerUser(@RequestHeader("gToken") String token) throws IOException, BadRequestException {
		LOG.debug("API registerUser - token: " + token);

		User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}

		return playerService.registerPlayer(user);
	}
	
	@RequestMapping(method = RequestMethod.POST, path="/device/{deviceId}/{deviceType}")
	public Map<String, Boolean> registerDevice(@PathVariable String deviceId, @PathVariable Integer deviceType, @RequestHeader("gToken") String token) throws NotFoundException {
		
    User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}

    deviceService.registerDevice(user, deviceType, deviceId);
		
		return Collections.singletonMap("success", true);
	}
	
	@RequestMapping(method = RequestMethod.POST, path="/device/{deviceId}")
	public Map<String, Boolean> unregisterDevice(@PathVariable String deviceId, @RequestHeader("gToken") String token) throws NotFoundException {
		
    User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}

    deviceService.unregisterDevice(user, deviceId);
		
		return Collections.singletonMap("success", true);
	}
	
	@RequestMapping(method = RequestMethod.GET, path="/list")
	public List<Player> getPlayers(@RequestHeader("gToken") String token) {
    User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
		
    return playerService.getPlayers(user);
	}
}