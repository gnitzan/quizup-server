package com.rom.quizup.server.controllers;

import java.io.IOException;
import java.util.Collections;
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

import com.rom.quizup.server.entities.QuInvitation;
import com.rom.quizup.server.models.Game;
import com.rom.quizup.server.models.GamePlayStatus;
import com.rom.quizup.server.models.Invitation;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.services.ConflictException;
import com.rom.quizup.server.services.GameService;
import com.rom.quizup.server.services.GoogleTokenService;
import com.rom.quizup.server.services.InvitationService;
import com.rom.quizup.server.services.NotFoundException;

/**
 * Handles client's game related functionality using REST. We annotate the
 * class with Spring's @see RestController so Spring will automatically
 * registers our methods and map it as REST requests under /game path.
 * 
 * @author rom
 */
@RestController
@RequestMapping("/games")
public class GameController {
	private static Logger LOG = LoggerFactory.getLogger(GameController.class);
	
	/**
	 * Inject @see GameService
	 */
	@Autowired
	private GameService gameService;
	
	/**
	 * Inject @see GoogleTokenService
	 */
	@Autowired
	private GoogleTokenService tokenService;
	
	/**
	 * Inject @see InvitationService
	 */
	@Autowired
	private InvitationService invitationService;

	/**
	 * Default constructor
	 */
	public GameController() {
	}
	
	/**
	 * Handles requests for invitation status.
	 * 
	 * When a user sends an invitation to another user, the
	 * client keeps checking for the invitation status with
	 * the Quizup game server.
	 * 
	 * @param gameId
	 * @param invitationId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path="/{gameId}/invitation/{invitationId}/status")
	public Invitation getInvitationStatus(@PathVariable String gameId, @PathVariable String invitationId, @RequestHeader("gToken") String token) throws NotFoundException {
		LOG.info("API getInvitationStatus - gameId: " + gameId + " invitationId: " + invitationId);
		
    try {
			tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
    
    return new Invitation(invitationService.getInvitationStatus(gameId, invitationId));
	}

	/**
	 * Get information for a specific game.
	 * 
	 * @param gameId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path="/{gameId}")
	public Game getGame(@PathVariable String gameId, @RequestHeader("gToken") String token) throws NotFoundException {
		LOG.info("API getGame - gameId: " + gameId);
		
    try {
			tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
    
    return gameService.getGame(gameId);
	}

	/**
	 * Decline an invitation to play a game.
	 * 
	 * @param gameId
	 * @param invitationId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 * @throws ConflictException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/{gameId}/invitation/{invitationId}/decline")
	public Map<String, Boolean> declineInvitation(@PathVariable String gameId, @PathVariable String invitationId, @RequestHeader("gToken") String token) throws NotFoundException, ConflictException {
		LOG.info("API declineInvitation - gameId: " + gameId + " invitationId: " + invitationId);
		
    try {
			tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
    
    invitationService.declineInvitation(gameId, invitationId);
    
    return Collections.singletonMap("success", true);
	}

	/**
	 * Cancels an invitaion sent to another player.
	 * 
	 * @param gameId
	 * @param invitationId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 * @throws ConflictException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/{gameId}/invitation/{invitationId}/cancel")
	public Map<String, Boolean> cancelInvitation(@PathVariable String gameId, @PathVariable String invitationId, @RequestHeader("gToken") String token) throws NotFoundException, ConflictException {
		LOG.info("API cancelInvitation - gameId: " + gameId + " invitationId: " + invitationId);
		
    try {
			tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
    
    invitationService.cancelInvitation(gameId, invitationId);
    
    return Collections.singletonMap("success", true);
	}

	/**
	 * Accept an invitation to a game.
	 * 
	 * @param gameId
	 * @param invitationId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 * @throws ConflictException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/{gameId}/invitation/{invitationId}/accept")
	public Map<String, Boolean> acceptInvitation(@PathVariable String gameId, @PathVariable String invitationId, @RequestHeader("gToken") String token) throws NotFoundException, ConflictException {
		LOG.info("API acceptInvitation - gameId: " + gameId + " invitationId: " + invitationId);
		
    try {
			tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}
    
    invitationService.acceptInvitation(gameId, invitationId);
    
    return Collections.singletonMap("success", true);
	}
	
	/**
	 * Start a multi player game.
	 * 
	 * @param boardLevel
	 * @param inviteeId
	 * @param token
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/startMulti/{boardLevel}/{inviteeId}")
	public Invitation startMultiPlayerGame(@PathVariable Integer boardLevel, @PathVariable String inviteeId, @RequestHeader("gToken") String token) throws NotFoundException {
		LOG.info("API startMulti - level: " + boardLevel + " inviteeId: " + inviteeId);

		User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}

    QuInvitation inv = invitationService.sendInvitation(user, inviteeId, boardLevel);
    
    return new Invitation(inv);
	}

	/**
	 * Start a single player game.
	 * 
	 * @param boardLevel
	 * @param user
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/startSingle/{boardLevel}")
	public Game startNewSinglePlayerGame(@PathVariable Integer boardLevel, @RequestBody User user) throws NotFoundException {
		LOG.info("API startNewSinglePlayerGame - level: " + boardLevel + " for user: " + user.getEmail());
		
		try {
			return gameService.startNewSinglePlayerGame(user, boardLevel);
		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Submit answers when game is done.
	 * 
	 * @param gameId
	 * @param answers
	 * @param token
	 * @return
	 * @throws NotFoundException
	 * @throws BadRequestException
	 */
	@RequestMapping(method = RequestMethod.POST, path="/{gameId}/answers")
	public Map<String, Boolean> submitAnswers(@PathVariable String gameId, @RequestBody GamePlayStatus answers, @RequestHeader("gToken") String token)
      throws NotFoundException, BadRequestException {
		
		LOG.info("API submitAnswers - gameId: " + gameId + " for token: " + token);
		LOG.info(answers.toString());
		
		

    if (answers == null || answers.getCorrectAnswers() == null) {
      throw new BadRequestException("answers and correctAnswers cannot be null");
    }
    
    User user = null;
    
    try {
			user = tokenService.getUserFromToken(token);
		}
		catch (IOException e) {
			e.printStackTrace();
			
			throw new BadRequestException(e);
		}

    gameService.submitAnswers(gameId, user, answers);
    
    return Collections.singletonMap("success", true);
  }	
}