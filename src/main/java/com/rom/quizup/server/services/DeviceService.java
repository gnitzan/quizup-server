package com.rom.quizup.server.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rom.quizup.server.entities.QuDevice;
import com.rom.quizup.server.entities.QuPlayer;
import com.rom.quizup.server.models.User;
import com.rom.quizup.server.repositories.DevicesRepository;
import com.rom.quizup.server.utilities.NotFoundException;

/**
 * Service for managing device registration.
 * 
 * @author rom
 */
@Service
public class DeviceService {
	
	@Autowired
	private DevicesRepository devicesRepository;
	
	@Autowired
	private PlayerService playerService;
	
	/**
	 * Registers a device to a player (idempotent operation).
	 *
	 * @param user
	 *          the player using the device.
	 * @param deviceType
	 *          the device type.
	 * @param deviceId
	 *          the id of the device assigned by the platform specific push
	 *          notification system.
	 * @throws NotFoundException
	 *           when player is not registered.
	 */
	public void registerDevice(User user, int deviceType, String deviceId) throws NotFoundException {
		QuPlayer player = playerService.getByEmail(user.getEmail());

		if (player == null) {
			throw new NotFoundException("Player not registered.");
		}

		if (isDeviceRegistered(player, deviceId, deviceType)) {
			return;
		}

		QuDevice newDevice = devicesRepository.insert(new QuDevice(deviceId, deviceType, new Date()));
		
		playerService.registerDevice(player, newDevice);
	}

	/**
	 * Unregisters a device for a player.
	 *
	 * @param user
	 *          the player that was using the device.
	 * @param deviceId
	 *          the id of the device to be unregistered.
	 * @throws NotFoundException
	 *           when player is not registered.
	 */
	public void unregisterDevice(User user, String deviceId) throws NotFoundException {
		QuPlayer player = playerService.getByEmail(user.getEmail());

		if (player == null) {
			throw new NotFoundException("Player not registered.");
		}
		
		QuDevice device = devicesRepository.findByDeviceId(deviceId);
		
		if (device == null) {
			throw new NotFoundException("Device not registered.");
		}

		playerService.unregisterDevice(player, device);
	}

	/**
	 * Unregisters a device for a player. If this was the only device used by the
	 * player then the player is marked as inactive.
	 *
	 * @param playerKey
	 *          the entity key of the player who is registered with the device.
	 * @param deviceKey
	 *          the entity key of the device to be unregistered.
	 * @throws NotFoundException 
	 */
	protected void unregisterDevice(String playerId, String deviceId) throws NotFoundException {
		QuPlayer player = playerService.get(playerId);

		if (player == null) {
			throw new NotFoundException("Player not registered.");
		}
		
		unregisterDevice(player, deviceId);
	}

	/**
	 * Unregisters a device for a player. If this was the only device used by the
	 * player then the player is marked as inactive.
	 * 
	 * @param player the player who is registered with the device
	 * @param deviceId the id of the device, usually provided by Google
	 * @throws NotFoundException 
	 */
	protected void unregisterDevice(QuPlayer player, String deviceId) throws NotFoundException {
		player.removeDevice(deviceId);
		
		if (player.getDevices().size() == 0) {
			player.setActive(false);
		}
		
		QuDevice device = devicesRepository.findByDeviceId(deviceId);

		if (device == null) {
			throw new NotFoundException("Device not registered.");
		}

		playerService.unregisterDevice(player, device);
	}

	/**
	 * Updates device's id.
	 *
	 * @param device
	 *          the device entity to update.
	 * @param newDeviceId
	 *          the new device id.
	 * @throws NotFoundException 
	 */
	protected void updateDeviceRegistration(QuDevice device, String newDeviceId, String playerId) throws NotFoundException {
		QuPlayer player = playerService.get(playerId);
		if (player == null) {
			throw new NotFoundException("Player not registered.");
		}

		device.setDeviceId(newDeviceId);
		
		devicesRepository.save(device);
	}
	
	/**
	 * Gets a not null list of device entities registered for a player.
	 *
	 * @param playerKey
	 *          the entity key of the player
	 * @throws NotFoundException 
	 */
	protected List<QuDevice> getDevicesForPlayer(String playerId) throws NotFoundException {
		QuPlayer player = playerService.get(playerId);

		if (player == null) {
			throw new NotFoundException("Player not registered.");
		}

		return player.getDevices();
	}

	/**
	 * Gets device entity for a given player and device id.
	 *
	 * @param playerKey
	 *          the entity key of the player.
	 * @param deviceId
	 *          the id of the device to retrieve.
	 * @return the retrieved device entity or null if there is no device entity
	 *         for this playerKey and deviceId.
	 */
	public QuDevice getDevice(QuPlayer player, String deviceId) {
		return player.getDeviceById(deviceId);
	}

	/**
	 * Checks if a device is registered for a given player.
	 *
	 * @param playerEntity
	 *          the player entity.
	 * @param deviceId
	 *          the id of the device.
	 * @param deviceType
	 *          the type of the device.
	 * @return true if device is registered for this player; false otherwise.
	 */
	private boolean isDeviceRegistered(QuPlayer player, String deviceId, int deviceType) {
		QuDevice device = player.getDeviceByIdAndType(deviceId, deviceType);
		
		return device == null ? false : true;
	}
}
