package com.musicstream.api;

import java.util.ArrayList;

import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Playlist;
import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

/**
 * @author Malek
 * 
 */
public class SoundCloudApi extends Api {
	private SoundCloud soundcloud;
	// private User me;
	AppUtils appU;

	/**
	 * Initializing SoundCould APi
	 */
	public SoundCloudApi() {
		soundcloud = new SoundCloud("481ca2dc5f8ff18044c77239659a5b59",
				"1067035a0baee3932e79847bae144fa1","testing.kamel@gmail.com","azerty123");
		appU = new AppUtils();
	}

	/**
	 * @param login
	 * @param pass
	 * @return Boolean to indication if the Authentication was Successful
	 */
	@Override
	public Boolean userAuthentication(String login, String pass) {
		return soundcloud.login("testing.kamel@gmail.com", "azerty123");
	}

	/**
	 * @return User Object : contains all the user's information
	 */
	@Override
	public User getUser() {
		return soundcloud.getMe();
	}

	/**
	 * @param trackname
	 * @return track with the name track name
	 */
	@Override
	public ArrayList<Track> getTrack(String trackname) {
		ArrayList<Track> result = soundcloud.findTrack(trackname);
		return (result);
	}

	/**
	 * @return ArrayList contains User's Tracks
	 */
	@Override
	public ArrayList<Track> getTracksByUser() {
	//	String[] c = appU.getUserCredentials();
	//	this.userAuthentication(c[0], c[1]);

		User me = soundcloud.getMe();

		Integer count = me.getPublicFavoritesCount();
		Integer limit = 50; // = max
		Integer pages = (count / limit) + 1;

		ArrayList<Track> all_tracks = null;
		try {
			all_tracks = new ArrayList<Track>();

			for (int i = 0; i < pages; i++) {
				ArrayList<Track> temp_tracks = soundcloud.getMeFavorites(
						(i * limit), limit);
				all_tracks.addAll(temp_tracks);
			}

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			// JOptionPane jop = new JOptionPane();
			/*
			 * JOptionPane.showMessageDialog(null,
			 * "You Have No Tracks in Your Account",
			 * "MusicStream - Empty Account", JOptionPane.ERROR_MESSAGE);
			 */
		}
		return all_tracks;
	}

	/**
	 * @return ArrayList contains User's Playlists
	 */
	@Override
	public ArrayList<Playlist> getPlaylistByUser() {

		//String[] c = appU.getUserCredentials();
	//	this.userAuthentication(c[0], c[1]);
		User me = soundcloud.getMe();
		Integer count = me.getPlaylistCount();

		ArrayList<Playlist> all_playlists = new ArrayList<Playlist>();
		try {
			for (int i = 0; i < count; i++) {
				ArrayList<Playlist> temp_playlists = soundcloud
						.getMePlaylists();
				all_playlists.addAll(temp_playlists);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			// JOptionPane jop = new JOptionPane();
			/*
			 * JOptionPane.showMessageDialog(null,
			 * "You Have No Playlists in Your Account",
			 * "MusicStream - Empty Account", JOptionPane.ERROR_MESSAGE);
			 */
		}

		return all_playlists;
	}

	@Override
	public ArrayList<Track> getTracksofPlaylist(Object playlist) {
		return ((Playlist) playlist).getTracks();
	}

	@Override
	public void play(String url) {
		appU.readAudioFeed(url);
	}

	@Override
	public String[] getStreamUrl() {
		ArrayList<Track> tracks = this.getTracksByUser();
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			urls[i] = tracks.get(i).getStreamUrl();
		}
		return urls;
	}

	@Override
	public String[] getStreamUrlSearch(String title) {
		ArrayList<Track> tracks = this.getTrack(title);
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			urls[i] = tracks.get(i).getStreamUrl();
		}
		return urls;
	}

	@Override
	public int[] getLength() {
		ArrayList<Track> tracks = this.getTracksByUser();
		int[] length = new int[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			length[i] = tracks.get(i).getDuration();
		}
		return length;
	}

	@Override
	public int[] getLengthSearch(String title) {
		ArrayList<Track> tracks = this.getTrack(title);
		int[] length = new int[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			length[i] = tracks.get(i).getDuration();
		}
		return length;
	}
	/**
	 * @param playlist
	 * @return Stream Url of the Playlist Tracks
	 */
	public String[] getStreamUrlPlaylist(Object playlist) {
		ArrayList<Track> tracks = getTracksofPlaylist(playlist);
		String[] urls = new String[tracks.size()];
		for (int i = 0; i < tracks.size(); i++) {
			urls[i] = tracks.get(i).getStreamUrl();
		}
		return urls;
	}

	@Override
	public String getPreviewTrack(TrackId tID) {
		// TODO Auto-generated method stub
		return null;
	}
}
