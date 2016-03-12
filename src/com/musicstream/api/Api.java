package com.musicstream.api;

import java.util.ArrayList;

public interface Api {
	public Object getUser();

	public Boolean userAuthentication(String login, String pass);

	public ArrayList<?> getTracksByUser();

	public ArrayList<?> getTrack(String trackname);

	public ArrayList<?> getPlaylistByUser();

	public ArrayList<?> getTracksofPlaylist(Object playlist);

	public void play(String url);

	public String[] getStreamUrl();

	public String[] getStreamUrlSearch(String title);

	public int[] getLength();

	public int[] getLengthSearch(String title);
}
