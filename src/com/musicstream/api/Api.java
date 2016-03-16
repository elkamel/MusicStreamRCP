package com.musicstream.api;

import java.util.ArrayList;

public interface Api {
	public Object getUser();

	/**
	 * @param login
	 * @param pass
	 * @return Boolean indicatied if user Authenticasion was Successful 
	 */
	public Boolean userAuthentication(String login, String pass);

	/**
	 * @return Users Tracks
	 */
	public ArrayList<?> getTracksByUser();

	/**
	 * @param trackname
	 * @return Track Object based on which Api called the Method
	 */
	public ArrayList<?> getTrack(String trackname);

	/**
	 * @return User's Playlist
	 */
	public ArrayList<?> getPlaylistByUser();

	/**
	 * @param playlist
	 * @return Get the list of Tracks of a specific PLaylist
	 */
	public ArrayList<?> getTracksofPlaylist(Object playlist);

	/**
	 * @param url Plays audio Feed
	 */
	public void play(String url);

	/**
	 * @return Audio feed Stream Url
	 */
	public String[] getStreamUrl();

	/**
	 * @param title
	 * @return Audio feed Stream Url of a specific track 
	 */
	public String[] getStreamUrlSearch(String title);

	/**
	 * @return number of Tracks
	 */
	public int[] getLength();

	/**
	 * @param title
	 * @return Number of results when searching for a track
	 */
	public int[] getLengthSearch(String title);
}
