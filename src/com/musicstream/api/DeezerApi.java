package com.musicstream.api;

 
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.client.DeezerClient;
import com.zeloon.deezer.domain.Playlist;
import com.zeloon.deezer.domain.Track;
import com.zeloon.deezer.domain.User;
import com.zeloon.deezer.domain.internal.PlaylistId;
import com.zeloon.deezer.domain.internal.TrackId;
import com.zeloon.deezer.domain.internal.UserId;
import com.zeloon.deezer.domain.internal.search.Search;
import com.zeloon.deezer.domain.internal.search.SearchOrder;
import com.zeloon.deezer.io.HttpResourceConnection;

/**
 * @author Malek
 * Javadoc , Look at Api.java
 */
public class DeezerApi  extends Api {

	public DeezerClient deezerClient;
	UserId uID;
	AppUtils app;
	public Boolean connectedDeezer = false;
	 
	   public DeezerApi() {
		deezerClient = new DeezerClient(new HttpResourceConnection());
		uID = new UserId(905831863L);
		app = new AppUtils();
	}

	@Override
	public Boolean userAuthentication(String login, String pass) {
		 
		if (login.equals("testing.kamel@gmail.com") && pass.equals("azerty123"))
			return true;
		else
			return false;
	}

	@Override
	public User getUser() {
		return deezerClient.get(uID);
	}

	@Override
	public ArrayList<Track> getTracksByUser() {
		return (ArrayList<Track>) deezerClient.getTracks(uID).getData();
	}

	@Override
	public ArrayList<Playlist> getPlaylistByUser() {
		return (ArrayList<Playlist>) deezerClient.getPlaylists(uID).getData();
	}

	@Override
	public ArrayList<Track> getTrack(String trackname) {
		Search querry = new Search(trackname, SearchOrder.RANKING);
		return (ArrayList<Track>) (deezerClient.search(querry)).getData();
	}

	public ArrayList<Track> getTracksofPlaylist(Object playlist) {
		Playlist pl = (Playlist) playlist;
		PlaylistId pID = new PlaylistId(pl.getId());
		return (ArrayList<Track>) deezerClient.getTracks(pID).getData();
	}
	public String[] getStreamUrlPlaylist(Object playlist) {
		
		ArrayList<Track> tracks = getTracksofPlaylist(playlist);
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			urls[i] = tracks.get(i).getPreview();
		}
		return urls;
	}
	public String getPreviewTrack(TrackId tID) {
		return deezerClient.get(tID).getPreview();
	}

	@Override
	public void play(String url) {
		app.readAudioFeed(url);
	}

	@Override
	public String[] getStreamUrl() {// Getting Tracks Stream Url to be used by
									// the AudioPlayer
		ArrayList<Track> tracks = this.getTracksByUser();
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			TrackId tID = new TrackId(tracks.get(i).getId());
			urls[i] = this.getPreviewTrack(tID);
		}
		return urls;

	}

	@Override
	public String[] getStreamUrlSearch(String title) {
		ArrayList<Track> tracks = this.getTrack(title);
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			urls[i] = tracks.get(i).getPreview();
		}
		return urls;
	}

	@Override
	public int[] getLength() {
		ArrayList<Track> tracks = this.getTracksByUser();
		int[] length = new int[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			length[i] = 30;
		}
		return length;
	}

	@Override
	public int[] getLengthSearch(String title) {
		ArrayList<Track> tracks = this.getTrack(title);
		int[] length = new int[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			length[i] = 30;
		}
		return length;
	}
}
