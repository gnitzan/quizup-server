/**
 * The Quizup server domain classes which handles all
 * interactions with the clients based on client server paradigm.
 * 
 * Clients connects to the Quizup server over HTTP supplying
 * their Google provided "authentication token". In its turn
 * the server verifies the token with Google and if valid shall
 * try to register the user in the MongoDB database. If the user
 * already registered, the server will simply return the @see com.rom.quizup.server.models.Player
 * 
 * The server is using the Spring Boot framework for its ease of use,
 * power full API and consistent programming for all components
 * like data handling and REST requests.
 * 
 * The server
 */


package com.rom.quizup.server;
