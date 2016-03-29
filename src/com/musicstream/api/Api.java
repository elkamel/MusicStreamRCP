package com.musicstream.api;

import java.util.ArrayList;

import com.zeloon.deezer.domain.Track;
import com.zeloon.deezer.domain.internal.TrackId;

public abstract class Api {
	public abstract Object getUser();

	/**
	 * @param login
	 * @param pass
	 * @return Boolean indicatied if user Authenticasion was Successful
	 */
	public abstract Boolean userAuthentication(String login, String pass);

	/**
	 * @return Users Tracks
	 */
	public abstract ArrayList<?> getTracksByUser();

	/**
	 * @param trackname
	 * @return Track Object based on which Api called the Method
	 */
	public abstract ArrayList<?> getTrack(String trackname);

	/**
	 * @return User's Playlist
	 */
	public abstract ArrayList<?> getPlaylistByUser();

	/**
	 * @param playlist
	 * @return Get the list of Tracks of a specific PLaylist
	 */
	public abstract ArrayList<?> getTracksofPlaylist(Object playlist);

	/**
	 * @param url
	 *            Plays audio Feed
	 */
	public abstract void play(String url);

	/**
	 * @return Audio feed Stream Url
	 */
	public abstract String[] getStreamUrl();

	/**
	 * @param title
	 * @return Audio feed Stream Url of a specific track
	 */
	public abstract String[] getStreamUrlSearch(String title);

	/**
	 * @return number of Tracks
	 */
	public abstract int[] getLength();

	/**
	 * @param title
	 * @return Number of results when searching for a track
	 */
	public abstract int[] getLengthSearch(String title);

	public abstract String getPreviewTrack(TrackId tID);
}
