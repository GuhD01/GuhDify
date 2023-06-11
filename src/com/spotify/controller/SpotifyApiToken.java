package com.spotify.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.spotify.utli.Utilities;

public class SpotifyApiToken {

	private static final String CLIENT_ID = "a70af2fef28a4d15a345f4dbd7e94f69";
	// Client ID and Client Secret are provided by Spotify when you register your app. They are used for app identification and authorization
	private static final String CLIENT_SECRET = "2fac464989c547e8b67f42c6ba766852";


	public static String getAccessToken() throws Exception {
		URL url = new URL("https://accounts.spotify.com/api/token");// Create URL object with the Spotify token endpoint URL
		HttpURLConnection con = (HttpURLConnection) url.openConnection();// Create a HttpURLConnection object and open the connection
		con.setRequestMethod("POST");// Set the request method to POST as we are sending data
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// Set the request's content type header

		String postData = "grant_type=client_credentials&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;// The data to be sent in the POST request

		con.setDoOutput(true); // Enable output for the connection. This needs to be true because we are using POST method which sends data

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(postData);
		out.flush(); // Write the POST data to the output stream
		out.close();


		// Get the response status code. If the status code is not 200, an error has occurred
		int status = con.getResponseCode();
		if (status != 200) {
			JOptionPane.showMessageDialog(null, "Failed to get access token: HTTP error code " + status, "Error", JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException("Failed to get access token: HTTP error code " + status);
		}

		// Read the response from the connection's input stream
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		// Parse the access token from the JSON response
		JSONObject json = new JSONObject(content.toString());
		String accessToken = json.getString("access_token");

		// Store the access token in Utilities class for later use
		Utilities.accesToke=accessToken;
		System.out.println(accessToken);
		return accessToken;
	}
}
