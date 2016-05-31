package com.rom.quizup.server.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.rom.quizup.server.models.Player;
import com.rom.quizup.server.models.PlayerStatistics;
import com.rom.quizup.server.models.User;

@Document(collection="players")
public class QuPlayer {
	@Id
	private String id;
	
	@Indexed(unique = false)
	@Field("active")
	private Boolean active = false;
	
	@Field("token")
	private String token;
	
	@Indexed(unique = true)
	@Field("email")
	private String email;
	
	@Field("nickname")
	private String nickname;
	
	@Indexed(unique = false)
	@Field("googlePlusId")
	private String googlePlusId;
	
	@Field("multiplayerGamesWon")
	private Integer multiplayerGamesWon = 0;
	
	@Field("multiplayerGamesPlayed")
	private Integer multiplayerGamesPlayed = 0;
	
	@DBRef
	@Field("sentInvitations")
	private List<QuInvitation> sentInvitations = new ArrayList<>();
	
	@DBRef
	@Field("receivedInvitations")
	private List<QuInvitation> receivedInvitations = new ArrayList<>();

	@DBRef
	@Field("devices")
	private List<QuDevice> devices = new ArrayList<>();
	
	@Field("registerDate")
	private Date registerDate;
	
	@Field("lastModified")
	private Date lastModified;
	
	private String imageUrl;
	

	@PersistenceConstructor
	public QuPlayer(String id, String token, Boolean active, String email, String nickname, String googlePlusId, Integer multiplayerGamesWon,
			Integer multiplayerGamesPlayed, List<QuInvitation> sentInvitations, List<QuInvitation> receivedInvitations,
			List<QuDevice> devices, Date registerDate, Date lastModified) {
		this.id = id;
		this.active = active;
		this.token = token;
		this.email = email;
		this.nickname = nickname;
		this.googlePlusId = googlePlusId;
		this.multiplayerGamesWon = multiplayerGamesWon;
		this.multiplayerGamesPlayed = multiplayerGamesPlayed;
		this.sentInvitations = sentInvitations;
		this.receivedInvitations = receivedInvitations;
		this.devices = devices;
		this.lastModified = lastModified == null ? new Date() : lastModified;
		this.registerDate = registerDate == null ?  new Date() : registerDate;
	}

	/**
	 * Constructor used to create a new player model from Google's user object.
	 *
	 * @param user
	 *          the user object for which the player model is created.
	 * @throws IllegalArgumentException
	 *           if user is null.
	 */
	public QuPlayer(User user) {
		if (user == null) {
			throw new IllegalArgumentException("user cannot be null");
		}

		this.email = user.getEmail();
		this.token = user.getIdToken();
		
		String nn = this.email.substring(0, this.email.indexOf("@"));
		
		this.nickname = nn;
		this.active = false;
		this.googlePlusId = null;
		this.multiplayerGamesPlayed = 0;
		this.multiplayerGamesWon = 0;
		this.lastModified = new Date();
		this.registerDate = new Date();
		this.imageUrl = user.getImageUrl();
	}

	public void wonGame() {
		this.multiplayerGamesWon += 1;
		this.multiplayerGamesPlayed += 1;
		this.lastModified = new Date();
	}
	public void lostGame() {
		this.multiplayerGamesPlayed += 1;
		this.lastModified = new Date();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		this.lastModified = new Date();
	}
	public String getGooglePlusId() {
		return googlePlusId;
	}
	public void setGooglePlusId(String googlePlusId) {
		this.googlePlusId = googlePlusId;
		this.lastModified = new Date();
	}
	public Integer getMultiplayerGamesWon() {
		return multiplayerGamesWon;
	}
	public void setMultiplayerGamesWon(Integer multiplayerGamesWon) {
		this.multiplayerGamesWon = multiplayerGamesWon;
		this.lastModified = new Date();
	}
	public Integer getMultiplayerGamesPlayed() {
		return multiplayerGamesPlayed;
	}
	public void setMultiplayerGamesPlayed(Integer multiplayerGamesPlayed) {
		this.multiplayerGamesPlayed = multiplayerGamesPlayed;
		this.lastModified = new Date();
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
		this.lastModified = new Date();
	}
	
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
		this.lastModified = new Date();
	}
	public PlayerStatistics getPlayerStatistics() {
		return new PlayerStatistics(multiplayerGamesWon, multiplayerGamesPlayed);
	}

	public List<QuDevice> getDevices() {
		if (devices == null) {
			devices = new ArrayList<>();
		}

		return devices;
	}

	public void setDevices(List<QuDevice> devices) {
		this.devices = devices;
		this.lastModified = new Date();
	}
	
	public void addDevice(QuDevice device) {
		this.lastModified = new Date();
		
		if (devices == null) {
			devices = new ArrayList<>();
		}
		
		this.devices.add(device);
	}
	
	public boolean removeDevice(QuDevice device) {
		this.lastModified = new Date();

		if (devices == null) {
			devices = new ArrayList<>();
			return false;
		}

		return this.devices.remove(device);
	}
	
	public boolean removeDevice(String deviceId) {
		QuDevice device = getDeviceById(deviceId);
		
		if (device == null) {
			return false;
		}
		
		return this.devices.remove(device);
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		this.lastModified = new Date();
	}

	public List<QuInvitation> getSentInvitations() {
		return sentInvitations;
	}

	public void setSentInvitations(List<QuInvitation> sentInvitations) {
		this.sentInvitations = sentInvitations;
		this.lastModified = new Date();
	}

	public List<QuInvitation> getReceivedInvitations() {
		return receivedInvitations;
	}

	public void setReceivedInvitations(List<QuInvitation> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
		this.lastModified = new Date();
	}

	public QuDevice getDeviceById(String deviceId) {
		QuDevice ret = null;
		
		if (devices == null) {
			devices = new ArrayList<>();
		}
		else {
			for (QuDevice device : devices) {
				if (device.getDeviceId().equalsIgnoreCase(deviceId)) {
					ret = device;
				}
			}
		}
		
		return ret;
	}
	public QuDevice getDeviceByIdAndType(String deviceId, int deviceType) {
		QuDevice ret = null;
		
		if (this.devices == null) {
			devices = new ArrayList<>();
		}
		else {
			for (QuDevice device : this.devices) {
				if (device.getDeviceId().equalsIgnoreCase(deviceId) && device.getDeviceType() == deviceType) {
					ret = device;
				}
			}
		}
		
		return ret;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Player getPlayer() {
		PlayerStatistics ps = new PlayerStatistics(this.multiplayerGamesWon, this.multiplayerGamesPlayed);
		Player p = new Player(id, nickname, ps, imageUrl);
		p.setImageUrl(this.imageUrl);
		
		return p;
	}
}
