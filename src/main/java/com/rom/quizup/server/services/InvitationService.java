package com.rom.quizup.server.services;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.rom.quizup.server.models.QuBoard;
import com.rom.quizup.server.models.QuDevice;
import com.rom.quizup.server.models.QuGame;
import com.rom.quizup.server.models.QuGamePlay;
import com.rom.quizup.server.models.QuInvitation;
import com.rom.quizup.server.models.QuPlayer;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.repositories.GamePlaysRepository;
import com.rom.quizup.server.repositories.InvitationsRepository;
import com.rom.quizup.server.utilities.InvitationStatus;

/**
 * Service for managing invitations.
 */
@Service
public class InvitationService {
	private static final Logger logger = Logger.getLogger(InvitationService.class.getSimpleName());

	@Autowired
	private InvitationsRepository invitationsRepository;
	
	@Autowired
	private AndroidNotificationService androidNotificationService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private GamePlaysRepository gamePlaysRepository;
	
	//@Autowired
	//private Configuration config;


	/**
	 * Gets the invitation status.
	 *
	 * @param gameId
	 *          the game id.
	 * @param invitationId
	 *          the invitation Id.
	 * @throws NotFoundException
	 *           if there is no record for this invitationId and gameId.
	 */
	public QuInvitation getInvitationStatus(String gameId, String invitationId) throws NotFoundException {
		QuInvitation invitation = get(invitationId);

		if (invitation == null) {
			throw new NotFoundException("QuInvitation record not found.");
		}

		// Map from enum defined in the entity class to enum defined in the API
		// layer, to avoid
		// "leaking" entity layer data structures to the API layer.
		/*
		switch (status) {
		case ACCEPTED:
			return QuInvitation.Status.ACCEPTED;
		case DECLINED:
			return QuInvitation.Status.DECLINED;
		case CANCELED:
			return QuInvitation.Status.CANCELED;
		case SENT:
			return QuInvitation.Status.SENT;
		default:
			return QuInvitation.Status.SENT;
		}*/
		return invitation;
	}

	/**
	 * Sends an invitation to start a new game.
	 *
	 * @param sender
	 *          the player that wants to invite another player.
	 * @param inviteeId
	 *          the id of the player that was invited.
	 * @param boardLevel
	 *          the board level for the new game.
	 * @return QuInvitation resource representing the created invitation.
	 * @throws NotFoundException
	 *           if there is no record for this user, inviteeId or boardLevel.
	 */
	public QuInvitation sendInvitation(User sender, String inviteeId, int boardLevel) throws NotFoundException {
		QuPlayer senderEntity = playerService.getByEmail(sender.getEmail());
		
		if (senderEntity == null) {
			throw new NotFoundException("Player record not found.");
		}

		// first try with google plus id
		//QuPlayer invitee = playerService.getByPlusId(inviteeId);
		//if (invitee == null) {
			// try with id
		QuPlayer invitee = playerService.get(inviteeId);
		//}
		
		if (invitee == null) {
			throw new NotFoundException("Player record for the inviteee not found.");
		}

		QuBoard board = gameService.getRandomBoard(boardLevel);
		if (board == null) {
			throw new NotFoundException("No board record found for this level.");
		}

		return sendInvitation(senderEntity, invitee, board);
	}

	/**
	 * Accepts an invitation.
	 *
	 * @param gameId
	 *          the game id this invitation is for.
	 * @param invitationId
	 *          the invitation id.
	 * @throws NotFoundException
	 *           if there is no record for this gameId and invitationId.
	 * @throws ConflictException
	 *           if the invitation state has changed and the invitation cannot be
	 *           accepted.
	 */
	public void acceptInvitation(String gameId, String invitationId) throws NotFoundException, ConflictException {
		QuInvitation invitation = get(invitationId);
		QuGame game = gameService.get(gameId);

		if (invitation == null) {
			throw new NotFoundException("QuInvitation record not found.");
		}

		if (!invitation.accept()) {
			throw new ConflictException(
					"Unable to accept the invitation because it has already been declined " + "or is no longer valid.");
		}
		else {
			invitationsRepository.save(invitation);
		}

		QuGamePlay inviteePlayer = gamePlaysRepository.insert(new QuGamePlay(invitation.getReceipient()));
		QuGamePlay senderPlayer  = gamePlaysRepository.insert(new QuGamePlay(invitation.getSender()));

		gameService.addGamePlay(game, inviteePlayer);
		gameService.addGamePlay(game, senderPlayer);
	}

	/**
	 * Declines an invitation.
	 *
	 * @param gameId
	 *          the game id this invitation is for.
	 * @param invitationId
	 *          the invitation id.
	 * @throws NotFoundException
	 *           if there is no record for this gameId and invitationId.
	 * @throws ConflictException
	 *           if the invitation state has changed and the invitation cannot be
	 *           declined.
	 */
	public void declineInvitation(String gameId, String invitationId) throws NotFoundException, ConflictException {
		QuInvitation invitation = get(invitationId);

		if (invitation == null) {
			throw new NotFoundException("QuInvitation record not found.");
		}

		if (!invitation.decline()) {
			throw new ConflictException(
					"Unable to decline the invitation because it has already been accepted " + "or is no longer valid.");
		}
		
		invitationsRepository.save(invitation);
	}

	/**
	 * Cancels an invitation.
	 *
	 * @param gameId
	 *          the game id this invitation is for.
	 * @param invitationId
	 *          the invitation id.
	 * @throws NotFoundException
	 *           if there is no record for this gameId and invitationId.
	 * @throws ConflictException
	 *           if the invitation state has changed and the invitation cannot be
	 *           cancelled.
	 */
	public void cancelInvitation(String gameId, String invitationId) throws NotFoundException, ConflictException {
		QuInvitation invitation = get(invitationId);

		if (invitation == null) {
			throw new NotFoundException("QuInvitation record not found.");
		}

		if (!invitation.cancel()) {
			throw new ConflictException("Unable to decline the invitation because it has already been accepted.");
		}

		invitationsRepository.save(invitation);
	}

	/**
	 * Atomically create game entity, invitation entity and a task to deliver the
	 * invitation notification.
	 *
	 * @param senderEntity
	 *          the Player entity that wants to invite another player.
	 * @param invitee
	 *          the Player entity of the invited player.
	 * @param board
	 *          the board to use for the new game
	 * @return QuInvitation resource representing the created invitation.
	 */
	private QuInvitation sendInvitation(QuPlayer sender, QuPlayer invitee, QuBoard board) {

		QuGame game = gameService.createNewGame(board);

		QuInvitation invitation = new QuInvitation(sender, invitee, game);
		invitationsRepository.insert(invitation);

		String invitationText = String.format("%s has invited you to play a game of Quizup", sender.getNickname());

		/*
    try {
      // Prepare JSON containing the GCM message content. What to send and where to send.
      JSONObject jGcmData = new JSONObject();
      JSONObject jData = new JSONObject();
      jData.put("message", "my test2");//invitationText);
      // Where to send GCM message.
      //if (args.length > 1 && args[1] != null) {
      //    jGcmData.put("to", args[1].trim());
      //} else {
          jGcmData.put("to", "/topics/global");
      //}
      // What to send in GCM message.
      jGcmData.put("data", jData);

      // Create connection to send GCM Message request.
      URL url = new URL("https://android.googleapis.com/gcm/send");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", "key=" + config.getCloudMessagingApiKey());
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);

      // Send GCM message content.
      OutputStream outputStream = conn.getOutputStream();
      outputStream.write(jGcmData.toString().getBytes());

      // Read GCM response.
      InputStream inputStream = conn.getInputStream();
      int responseCode = conn.getResponseCode();

  		System.out.println("Response Code : " + responseCode);

  		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
  		String inputLine;
  		StringBuffer response = new StringBuffer();

  		while ((inputLine = in.readLine()) != null) {
  			response.append(inputLine);
  		}
  		in.close();

  		//print result
  		System.out.println(response.toString());
  } catch (IOException e) {
      System.out.println("Unable to send GCM message.");
      System.out.println("Please ensure that API_KEY has been replaced by the server " +
              "API key, and that the device's registration token is correct (if specified).");
      e.printStackTrace();
  }*/
		
		
		try {
			sendAndroidInvitationNotification(deviceService.getDevicesForPlayer(invitee.getId()),
					invitee, invitation, game.getId(), invitationText);
		}
		catch (NotFoundException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}

		invitation.setStatus(InvitationStatus.SENT);
		
		return invitationsRepository.save(invitation);
	}

	/**
	 * Gets QuInvitation entity by entity key.
	 *
	 * @param invitationKey
	 *          the invitation entity key to retrieve.
	 */
	private QuInvitation get(String invitationId) {
		return invitationsRepository.findOne(invitationId);
	}

	/**
	 * Sends an invitation notification to Android devices.
	 *
	 * @param androidDevices
	 *          the list of Android devices to send the notification to.
	 * @param invitee
	 *          the Player who has been invited.
	 * @param invitationId
	 *          the invitation id.
	 * @param gameId
	 *          the game id.
	 * @param messageText
	 *          invitationText to be sent.
	 * @throws IOException
	 *           if an error occurred while sending Android push notifications.
	 */
	private void sendAndroidInvitationNotification(List<QuDevice> androidDevices, QuPlayer invitee, QuInvitation invitation,
			String gameId, String messageText) throws IOException {
		
		Builder builder = new Message.Builder();
		builder.addData("messageText", messageText);
		builder.addData("invitationId", invitation.getId());
		builder.addData("gameId", gameId);
		builder.addData("playerId", invitee.getId());
		builder.addData("nickName", invitee.getNickname());

		Message message = builder.build();

		androidNotificationService.sendMessage(androidDevices, message, invitee, invitation);
	}
}
