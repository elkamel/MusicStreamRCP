package com.musicstream.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;

import musicstream.parts.SearchPart;

public class JUnitTest {
	SoundCloudApi soundCloudAPI = new SoundCloudApi();
	DeezerApi deezerAPI = new DeezerApi();

	@Test
	public void testSoundCloudApi() {
		assertNotNull("Soundcloud API instancié ", soundCloudAPI);
	}

	@Test
	public void testDeezerApi() {
		assertNotNull("Deezer API instancié ", deezerAPI);
	}

	@Test
	public void tesGetSCUser() {
		assertNotNull("Session Soundcloud ouverte: ", soundCloudAPI.getUser());
	}

	@Test
	public void tesGetDZUser() {
		assertNotNull("Session Deezer ouverte: ", deezerAPI.getUser());
	}

	@Test
	public void testGetTracksByUser() {
		assertNotNull("Liste des chansons chargées de Soundcloud ", soundCloudAPI.getTracksByUser());
		assertNotNull("Liste des chansons chargées de Deezer ", deezerAPI.getTracksByUser());
	}

	@Test
	public void testGetPlaylistByUser() {
		assertNotNull("Liste des playlists chargées de Soundcloud ", soundCloudAPI.getPlaylistByUser());
		assertNotNull("Liste des playlists chargées de Deezer ", deezerAPI.getPlaylistByUser());
	}

	@Test
	public void testGetTracksSearch() {
		assertNotNull("Liste des chansons recherchées chargées de Soundcloud ",
				soundCloudAPI.getTrack(SearchPart.textField.getText()));
		assertNotNull("Liste des chansons recherchées chargées de Deezer ",
				deezerAPI.getTrack(SearchPart.textField.getText()));
	}

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("com.musicstream.test.JUnitTest");
	}
}