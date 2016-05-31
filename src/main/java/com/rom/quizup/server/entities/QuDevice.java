package com.rom.quizup.server.entities;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The definition of a device in the database.
 * 
 * @author rom
 *
 */
@Document(collection="devices")
public class QuDevice {
	@Id
	private ObjectId id;
	
	@Indexed(unique = false)
	@Field("deviceId")
	private String deviceId;
	
	@Field("deviceType")
	private Integer deviceType;
	
	@Field("regDate")
	private Date regDate = new Date();
	

	/**
	 * Constructor
	 * @param deviceId
	 * @param deviceType
	 */
	@PersistenceConstructor
	public QuDevice(String deviceId, Integer deviceType, Date regDate) {
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.regDate = regDate == null ? new Date() : regDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
}
