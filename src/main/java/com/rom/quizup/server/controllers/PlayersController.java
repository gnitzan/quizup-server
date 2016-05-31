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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rom.quizup.server.models.Player;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.services.DeviceService;
import com.rom.quizup.server.services.GoogleTokenService;
import com.rom.quizup.server.services.PlayerService;
import com.rom.quizup.server.utilities.NotFoundException;

/**
 * Handles client's player related functionality using REST. We annotate the
 * class with Spring's @see RestController so Spring will automatically
 * registers our methods and map it as REST requests.
 * 
 * @author rom
 */
@RestController
@RequestMapping("/players")
public class PlayersController {
	private static Logger LOG = LoggerFactory.getLogger(PlayersController.class);
	
	/**
	 * inject @see PlayerService
	 */
	@Autowired
	private PlayerService playerService;
	
	/**
	 * inject the @see GoogleTokenService
	 */
	@Autowired
	private GoogleTokenService tokenService;
	
	/**
	 * inject the @see DeviceService
	 */
	@Autowired
	private DeviceService deviceService;

	/**
	 * default constructor
	 */
	public PlayersController() {
	}
	
	/**
	 * Register a user with Quizup server. The client attaches
	 * Google's authentication token.
	 * 
	 * @param token
	 * @return
	 * @throws IOException
	 * @throws BadRequestException
	 */
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
	
	/**
	 * Register a user's device with Quizup server.
	 * The device id and type are taken from the device.
	 * 
	 * @param deviceId
	 * @param deviceType
	 * @param token
	 * @return
	 * @throws NotFoundException
	 */
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
	
	/**
	 * Un registers a device from Quizup server.
	 * 
	 * @param deviceId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 */
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
	
	/**
	 * Get a list of players registered with the server.
	 * 
	 * @param token
	 * @return
	 */
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