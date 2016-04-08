package com.rom.quizup.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rom.quizup.server.models.Game;
import com.rom.quizup.server.models.GamePlay;
import com.rom.quizup.server.models.GamePlayStatus;
import com.rom.quizup.server.models.Player;
import com.rom.quizup.server.models.QuBoard;
import com.rom.quizup.server.models.QuGame;
import com.rom.quizup.server.models.QuGamePlay;
import com.rom.quizup.server.models.QuPlayer;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.repositories.BoardsRepository;
import com.rom.quizup.server.repositories.GamePlaysRepository;
import com.rom.quizup.server.repositories.GamesRepository;

/**
 * Service for managing games.
 */
@Service
public class GameService {
	
	@Autowired
	private GamesRepository gamesRepository;
	
	@Autowired
	private BoardsRepository boardsRepository;
	
	@Autowired
	private GamePlaysRepository gamePlaysRepository;
	
	@Autowired
	private PlayerService playerService;

	/**
	 * Gets the game resource by its id.
	 *
	 * @param gameId
	 *          the id of the game resource.
	 * @throws NotFoundException
	 *           when game with gameId is not found.
	 */
	public Game getGame(String gameId) throws NotFoundException {
		QuGame game = get(gameId);
		
		if (game == null) {
			throw new NotFoundException("Game " + gameId + " not found.");
		}

		return new Game(
				game.getId(), getBoard(game.getBoard().getBoardId()), getGamePlays(game));
	}
	
	/**
	 * Submits the player's answers for a game.
	 *
	 * @param gameId
	 *          the Id of the game.
	 * @param player
	 *          the current player.
	 * @param answers
	 *          the player's answers in this game.
	 * @throws NotFoundException
	 *           when game with gameId is not found or if the player is not
	 *           registered or did not play that game.
	 */
	public void submitAnswers(String gameId, User user, GamePlayStatus answers) throws NotFoundException {
		QuGame game = get(gameId);
		
		if (game == null) {
			throw new NotFoundException("Game " + gameId + " not found.");
		}

		QuPlayer player = playerService.getByEmail(user.getEmail());
		
		if (player == null) {
			throw new NotFoundException("Player not found.");
		}

		// Find the GamePlay entity for this player.
		QuGamePlay gamePlayForThisPlayer = null;

		for (QuGamePlay gamePlay : game.getGamePlays()) {
			if (gamePlay.getPlayer().getId().equalsIgnoreCase(player.getId())) {
				gamePlayForThisPlayer = gamePlay;
				break;
			}
		}

		if (gamePlayForThisPlayer == null) {
			throw new NotFoundException("Player did not play game " + gameId + ".");
		}

		// Update the GamePlay entity for this player

		gamePlayForThisPlayer.setCorrectAnswers(answers.getCorrectAnswers());
		gamePlayForThisPlayer.setTimeLeft(answers.getTimeLeft());
		gamePlayForThisPlayer.setFinished(true);
		gamePlaysRepository.save(gamePlayForThisPlayer);

		// if this was a multiplayer game and the game is complete
		// update player statistics for every player that
		// participated in this game.
		if (game.getGamePlays().size() > 1 && isGameFinished(game.getGamePlays())) {
			
			determineWinner(game, game.getGamePlays());
			
			gamesRepository.save(game);
			
			playerService.updatePlayerStatistics(game);
		}
	}
	
  /**
   * Determines which player has won the game and update the game entity with that winner player's
   * key.
   *
   * @param players list of players from this game.
   * @return true if the winner was determined; false if the game is not finished yet.
   * @throws IllegalArgumentException if player list is null or empty.
   */
  public boolean determineWinner(QuGame game, List<QuGamePlay> players) {
    if (players == null || players.size() <= 1) {
      throw new IllegalArgumentException("players list must contain at least two players");
    }

    if (!isGameFinished(players)) {
      return false;
    }

    QuGamePlay currentWinner = players.get(0);
    
    int currentWinnerAnswerCount = currentWinner.getCorrectAnswers().size();

    for (int index = 1; index < players.size(); index++) {

    	QuGamePlay player = players.get(index);

      int playerAnswerCount = player.getCorrectAnswers().size();

      if (playerAnswerCount > currentWinnerAnswerCount) {
        currentWinner = player;
        currentWinnerAnswerCount = playerAnswerCount;
      } else if (playerAnswerCount == currentWinnerAnswerCount) {
        currentWinner = whoWinsTie(currentWinner, player);
      }
    }

    game.setPlayerWon(currentWinner.getPlayer());

    return true;
  }

  private boolean isGameFinished(List<QuGamePlay> players) {
  	for (QuGamePlay player : players) {
      if (!player.getFinished()) {
        return false;
      }
    }
    return true;
  }

  private QuGamePlay whoWinsTie(QuGamePlay player1, QuGamePlay player2) {
    long player1TimeLeft = player1.getTimeLeft();
    long player2TimeLeft = player2.getTimeLeft();

    if (player1TimeLeft > player2TimeLeft) {
      return player1;
    } else if (player2TimeLeft > player1TimeLeft) {
      return player2;
    } else {
      return null;
    }
  }
	
  /**
	 * Starts a new single player game.
	 *
	 * @param player
	 *          the current player
	 * @param boardLevel
	 *          the board level to use.
	 * @return {@link Game}
	 * @throws NotFoundException
	 *           if there is no board level specified by boardLevel argument or
	 *           the player is not registered.
	 */
	public Game startNewSinglePlayerGame(User user, int boardLevel) throws NotFoundException {
		QuPlayer player = playerService.getByEmail(user.getEmail());

		if (player == null) {
			throw new NotFoundException("Player record not found.");
		}

		QuBoard board = getRandomBoard(boardLevel);
		if (board == null) {
			throw new NotFoundException("No board found for level " + boardLevel);
		}

		// Create a Game entity and GamePlay entity for this game as an atomic
		// operation.
		QuGame game = new QuGame(board);
		gamesRepository.save(game);

		QuGamePlay playerGame = new QuGamePlay(player);
		gamePlaysRepository.save(playerGame);
		
		game.addGamePlay(playerGame);
		gamesRepository.save(game);

		return getGame(game.getId());
	}
	
	public QuGame createNewGame(QuBoard board) {
		return gamesRepository.insert(new QuGame(board));
	}
	
	public QuGame addGamePlay(QuGame game, QuGamePlay gamePlay) {
		game.addGamePlay(gamePlay);
		return gamesRepository.save(game);
	}

	/**
	 * Gets a game by id.
	 *
	 * @param key
	 *          the entity key of the game to retrieve.
	 * @return game entity or null if there is no game entity with this key.
	 */
	public QuGame get(String gameId) {
		return gamesRepository.findOne(gameId);
	}

	/**
	 * Gets a game board by entity key.
	 *
	 * @param key
	 *          the entity key of the board to retrieve.
	 * @return board entity or null if there is no game entity with this key.
	 */
	private QuBoard getBoard(String boardId) {
		return boardsRepository.findOne(boardId);
	}

	/**
	 * Gets a list of all game board entities.
	 *
	 * @return non null map of board entities indexed by entity keys.
	 *
	private Map<String, QuBoard> getBoards() {
		Map<String, QuBoard> boards = new HashMap<>();
		
		List<QuBoard> results = boardsRepository.findAll();
		
		for (QuBoard board : results) {
			boards.put(board.getBoardId(), board);
		}

		return boards;
	}*/

	/**
	 * Gets a not null list of game boards for a given level.
	 *
	 * @param level
	 *          the level of the board.
	 */
	private List<QuBoard> getBoardsForLevel(int level) {
		return boardsRepository.findByLevel(level);
	}

	/**
	 * Gets random board of a given level or null if there is no board for this
	 * level.
	 *
	 * @param level
	 *          the level of the board.
	 */
	protected QuBoard getRandomBoard(int level) {
		List<QuBoard> boards = getBoardsForLevel(level);

		if (boards.size() == 0) {
			return null;
		}

		return boards.get(new Random().nextInt(boards.size()));
	}

	/**
	 * Gets all GamePlay entities for a given game.
	 *
	 */
	private List<QuGamePlay> getGamePlayEntities(String gameId) {
		QuGame game = get(gameId);

		return game.getGamePlays();
	}

	/**
	 * Gets all GamePlay resources for a given game.
	 *
	 */
	private List<GamePlay> getGamePlays(QuGame game) {
		List<GamePlay> gamePlays = new ArrayList<GamePlay>();


		// Get the list of all GamePlay entities for this game and transform it into
		// a list of GamePlay resources.
		for (QuGamePlay gamePlay : getGamePlayEntities(game.getId())) {
			Player player = new Player(gamePlay.getPlayer().getId(), gamePlay.getPlayer().getNickname());

			gamePlays.add(
					new GamePlay(
							player,
							gamePlay.getCorrectAnswers(),
							gamePlay.getFinished(),
							gamePlay.getTimeLeft(),
							isWinner(game, gamePlay.getPlayer().getId())));
		}

		return gamePlays;
	}
	
  /**
   * Checks if the player is the winner of this game.
   *
   * @param game the game to check
   * @param playerId the id of the player.
   * @return true if the player is the winner; false otherwise.
   */
  public boolean isWinner(QuGame game, String playerId) {
  	if (game.getPlayerWon() == null || game.getPlayerWon().getId() == null) {
  		return false;
  	}
  	
		String winnerId = game.getPlayerWon().getId();
		
		return playerId.equals(winnerId);
  }

	/**
	 * Initiates updating player statistics asynchronously for all players that
	 * played a given game.
	 *
	 *
	private void updateMultiplayerStatistics(String gameId, String winnerId) {
		Queue queue = QueueFactory.getQueue("player-statistics");

		TaskOptions taskOptions = TaskOptions.Builder.withUrl("/admin/statistics/processgameresults")
				.param("gameId", String.valueOf(gameId)).param("winnerId", String.valueOf(winnerId)).method(TaskOptions.Method.POST);

		queue.add(taskOptions);
	}*/
}
