package com.spotify.model;

public class Track {

	public String trackName;
	public String artistName;
	public String url;
	
	public Track(String trackName, String artistName, String url) { //these are the constructors
		super();
		this.trackName = trackName;
		this.artistName = artistName;
		this.url = url;
	}

	// This is a getter method for the trackName instance variable. It returns the track name.
	public String getTrackName() {
		return trackName;
	}

	// This is a setter method for the trackName instance variable. It sets the track name to a new value.
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	// This is a getter method for the artistName instance variable. It returns the artist name.
	public String getArtistName() {
		return artistName;
	}

	// This is a setter method for the artistName instance variable. It sets the artist name to a new value.
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	// This is a getter method for the url instance variable. It returns the URL.
	public String getUrl() {
		return url;
	}

	// This is a setter method for the url instance variable. It sets the URL to a new value.
	public void setUrl(String url) {
		this.url = url;
	}

	// This is the toString method for the Track class. It returns the string representation of a Track object.
	@Override
	public String toString() {
		return "Track [trackName=" + trackName + ", artistName=" + artistName + ", url=" + url + "]";
	}
}
