package com.rom.quizup.server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rom.quizup.server.models.Player;
import com.rom.quizup.server.models.QuDevice;
import com.rom.quizup.server.models.QuGame;
import com.rom.quizup.server.models.QuGamePlay;
import com.rom.quizup.server.models.QuPlayer;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.repositories.PlayersRepository;
import com.rom.quizup.server.utilities.CollectionUtil;
import com.rom.quizup.server.utilities.Strings;

/**
 * Service for managing players.
 */
@Service
public class PlayerService {

	@Autowired
	private PlayersRepository playersRepository;

	/**
	 * Gets the player resource associated with a given user.
	 *
	 * @param user
	 *          user making the request.
	 */
	public Player getPlayer(User user) throws NotFoundException {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null.");
		}

		QuPlayer player = getByEmail(user.getEmail());

		if (player == null) {
			throw new NotFoundException("Player record not found.");
		}

		return new Player(player.getId().toString(), player.getNickname(), player.getPlayerStatistics());
	}

	/**
	 * Gets a list of players that a given user can play with. This implementation
	 * simply returns the list of all players other than the current user.
	 *
	 * @param user
	 *          user making the request.
	 */
	public List<Player> getPlayers(User user) {
		List<Player> results = new ArrayList<Player>();

		List<QuPlayer> players = playersRepository.findAll();//.findActivePlayers(user.getEmail());

		for (QuPlayer player : players) {
			if (player.getEmail().equals(user.getEmail())) continue;
			
			results.add(new Player(player.getId().toString(), player.getNickname()));
		}

		return results;
	}

	/**
	 * Registers a player resource for the user (idempotent operation).
	 *
	 * @param user
	 *          user making the request.
	 * @return a not null Player resource for this user.
	 */
	public Player registerPlayer(User user) {
		QuPlayer player = getByEmail(user.getEmail());

		if (player == null) {
			player = playersRepository.insert(new QuPlayer(user));
		}

		return Player.createPlayerFromModel(player);
	}

	/**
	 * Registers a player resource for the user with Google+ Id (idempotent
	 * operation).
	 *
	 * @param user
	 *          user making the request.
	 * @param plusId
	 *          user's Google+ Id.
	 * @return a not null Player resource for this user.
	 */
	public Player registerPlayerWithPlusId(User user, String plusId) {
		if (Strings.isNullOrEmpty(plusId)) {
			return registerPlayer(user);
		}
		
		QuPlayer player = getByEmail(user.getEmail());

		if (player == null) {
			player = playersRepository.insert(new QuPlayer(user));
			player.setGooglePlusId(plusId);
		}
		else {
			player.setGooglePlusId(plusId);
		}

		playersRepository.save(player);

		return Player.createPlayerFromModel(player);
	}
	
	public QuPlayer registerDevice(QuPlayer player, QuDevice device) {
		player.addDevice(device);
		player.setActive(true);
		return playersRepository.save(player);
	}
	
	public QuPlayer unregisterDevice(QuPlayer player, QuDevice device) {
		player.removeDevice(device);
		if (player.getDevices().size() == 0) {
			player.setActive(false);
		}
		return playersRepository.save(player);
	}

	/**
	 * Assigns a Google+ Id to the player.
	 *
	 * @param user
	 *          user making the request.
	 * @param plusId
	 *          User's Google+ Id.
	 * @throws NotFoundException
	 *           when player record not found.
	 */
	public void assignPlusId(User user, String plusId) throws NotFoundException {
		QuPlayer player = getByEmail(user.getEmail());

		if (player == null) {
			throw new NotFoundException("Player record not found.");
		}

		player.setGooglePlusId(plusId);
		playersRepository.save(player);
	}

	/**
	 * Gets a player by entity key.
	 * 
	 * @param id
	 *          the id of a player
	 * @return player if found or null if there is no player with that id
	 */
	public QuPlayer get(String id) {
		return playersRepository.findOne(id);
	}

	/**
	 * Gets a player by entity google token
	 * 
	 * @param token
	 *          the token of a player
	 * @return player if found or null if there is no player with that id
	 */
	public Player getByToken(String token) {
		QuPlayer player = playersRepository.findByToken(token);
		
		if (player != null) {
			return Player.createPlayerFromModel(player);
		}
		
		return null;
	}

	/**
	 * Gets a player by email address.
	 *
	 * @param email
	 *          the email of the player to retrieve.
	 * @return player entity or null if there is no player entity with this email.
	 */
	protected QuPlayer getByEmail(String email) {
		return playersRepository.findByEmail(email);
	}

	/**
	 * Gets the player entity by player's Google+ Id.
	 *
	 * @param plusId
	 *          User's Google+ Id.
	 * @return player entity or null if there is no player entity with this email.
	 */
	protected QuPlayer getByPlusId(String googlePlusId) {
		return playersRepository.findByGooglePlusId(googlePlusId);
	}

	/**
	 * Gets a list of players by keys.
	 *
	 * @param playerKeys
	 *          list of player entity keys to retrieve.
	 */
	protected List<QuPlayer> getPlayers(List<String> playerIds) {
		Iterable<QuPlayer> playersIter = playersRepository.findAll(playerIds);
		
		List<QuPlayer> players = CollectionUtil.makeList(playersIter);

		if (players == null) {
			players = new ArrayList<>();
		}

		return players;
	}

	/**
	 * Updates win/loss statistics for players that competed in a game.
	 *
	 * @param playerIds
	 *          Ids of the players who competed in a game.
	 * @param winnerId
	 *          Id of the winner.
	 */
	protected void updatePlayerStatistics(QuGame game) {

		for (QuGamePlay play : game.getGamePlays()) {
			QuPlayer player = play.getPlayer();
			if (player.equals(game.getPlayerWon())) {
				player.wonGame();
			}
			else {
				player.lostGame();
			}

			playersRepository.save(player);
		}
	}
}
