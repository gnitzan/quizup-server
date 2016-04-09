package com.rom.quizup.server.services;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rom.quizup.server.models.GoogleToken;
import com.rom.quizup.server.models.User;

@Service
public class GoogleTokenService {
	private static Logger LOG = LoggerFactory.getLogger(GoogleTokenService.class);

	@Autowired
	private MapperService mapperService;

	public GoogleToken validateGoogleToken(String token) throws IOException {
		
		LOG.debug("Calling Google with token: " + token);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token);

		GoogleToken tokenObj = null;

		CloseableHttpResponse response1 = httpclient.execute(httpGet);

		HttpEntity entity1 = response1.getEntity();

		String googleResponse = EntityUtils.toString(entity1, "UTF-8");

		EntityUtils.consume(entity1);

		LOG.debug("Google response: " + googleResponse);

		tokenObj = mapperService.getMapper().readValue(googleResponse, GoogleToken.class);

		return tokenObj;
	}
	
	public User getUserFromToken(String sToken) throws IOException {
		GoogleToken token = validateGoogleToken(sToken);
		
		User user = new User();
		
		user.setEmail(token.getEmail());
		user.setFirstName(token.getGiven_name());
		user.setLastName(token.getFamily_name());
		user.setUsername(token.getName());
		user.setNickname(token.getName());
		user.setImageUrl(token.getPicture());

		
		return user;
	}
}
