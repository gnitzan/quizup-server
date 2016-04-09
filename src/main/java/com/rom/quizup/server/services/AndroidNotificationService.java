package com.rom.quizup.server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.rom.quizup.server.configuration.Configuration;

import com.rom.quizup.server.models.QuDevice;
import com.rom.quizup.server.models.QuInvitation;
import com.rom.quizup.server.models.QuPlayer;

/**
 * Service for sending push notifications to Android devices using Google Cloud
 * Messaging for Android.
 */
@Service
public class AndroidNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(AndroidNotificationService.class);
	private static final int NUMBER_OF_RETRIES = 3;
	
	@Autowired
	private Configuration config;
	
	@Autowired
	private DeviceService deviceService;
	
	/**
	 * Sends a message to Android devices.
	 *
	 * @param devices
	 *          the list of devices that the notification is sent to.
	 * @param message
	 *          the message to be sent.
	 * @param playerKey
	 *          the key of the player that the notification is sent to.
	 * @throws IOException
	 *           if there was IOException thrown by Google Cloud Messaging for
	 *           Android.
	 */
	protected void sendMessage(List<QuDevice> devices, Message message, QuPlayer player, QuInvitation invitation) throws IOException {
		List<String> androidDeviceIds = new ArrayList<String>();
		
		for (QuDevice device : devices) {
			androidDeviceIds.add(device.getDeviceId());
		}

		Sender messageSender = new Sender(config.getCloudMessagingApiKey());
		
		MulticastResult messageResults = messageSender.send(message, androidDeviceIds, NUMBER_OF_RETRIES);

		if (messageResults != null) {
	
			for (int i = 0; i < messageResults.getTotal(); i++) {
				Result result = messageResults.getResults().get(i);

				if (result.getMessageId() != null) {
					String canonicalRegId = result.getCanonicalRegistrationId();

					logger.info("Notification id: " + result.getMessageId() + " sent to: " + canonicalRegId);
					
					if (canonicalRegId != null) {
						try {
							logger.info("Updating device: " + devices.get(i) + " to: " + canonicalRegId);
							
							deviceService.updateDeviceRegistration(devices.get(i), canonicalRegId, player.getId());
						}
						catch (NotFoundException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
				else {
					String error = result.getErrorCodeName();
					
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						// The user has uninstalled the application or turned off
						// notifications.
						// Remove the device from Quizup.
						try {
							logger.error("Error in push: " + error);
							logger.info("Device not registered. Unregisterin device: " + devices.get(i).getDeviceId() + " from user: " + player.getEmail());
							deviceService.unregisterDevice(player.getId(), devices.get(i).getDeviceId());
						}
						catch (NotFoundException e) {
							logger.error(e.getMessage(), e);
						}
					}
					else {
						logger.error("Error when sending Android push notification: " + error);
					}
				}
			}
		}
		else {
			throw new IOException("Failed to send invitation");
		}
	}
}
