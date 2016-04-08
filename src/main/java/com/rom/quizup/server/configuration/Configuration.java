package com.rom.quizup.server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author rom
 * 
 * The Quizup server configuration class
 *
 */
@Component
@ConfigurationProperties(prefix="quizup")
public class Configuration {
	private String name;
	private String version;
	private String description;
	private String webClientId;
	private String androidClientId;
	private String cloudMessagingApiKey;
	private String audience;
	private String emailScope;

	public Configuration() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebClientId() {
		return webClientId;
	}

	public void setWebClientId(String webClientId) {
		this.webClientId = webClientId;
	}

	public String getAndroidClientId() {
		return androidClientId;
	}

	public void setAndroidClientId(String androidClientId) {
		this.androidClientId = androidClientId;
	}

	public String getCloudMessagingApiKey() {
		return cloudMessagingApiKey;
	}

	public void setCloudMessagingApiKey(String cloudMessagingApiKey) {
		this.cloudMessagingApiKey = cloudMessagingApiKey;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getEmailScope() {
		return emailScope;
	}

	public void setEmailScope(String emailScope) {
		this.emailScope = emailScope;
	}

}
