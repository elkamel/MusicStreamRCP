package com.musicstream.homeTabs;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

public class TracksTab implements ListSelectionListener {
	private Composite composite;
	private final Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	private SoundCloudApi soundCApi;
	private DeezerApi deezerApi;
	private MusicPlayer player;
	private List list;
	private JList listSwing;
	private String[] nameList;
	private String[] streamU;
	private int[] tracksLength;
	private Object[] tracks;

	public TracksTab(CTabFolder folder) {
		composite = new Composite(folder, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		composite.setLayout(new FillLayout());
		Frame frame = SWT_AWT.new_Frame(composite);// Allowing Java Swing
													// components to be added
													// the swt composite
		JPanel mypan = new JPanel();
		BorderLayout layout = new BorderLayout();
		mypan.setLayout(layout);

		appU = new AppUtils();
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		player = new MusicPlayer();
		nameList = setNameList();
		imageMap = createImageMap(nameList);
		streamU = this.getTracksStream();
		tracksLength = this.getTrackLength();
		tracks = this.getTracks();
		// / Setting up the Screen
		listSwing = new JList(nameList);
		listSwing.setCellRenderer(new tracksListRenderer());
		listSwing.addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(listSwing);
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 60, appU
				.getScreenHeight() - 100));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 40,
				appU.getScreenHeight() - 300);

		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setBounds(10, -15, 250, 75);
		player.setBounds(10, appU.getScreenHeight() - 150,
				appU.getScreenWidth() - 40, 80);
		player.setVisible(false);
		mypan.add(label, BorderLayout.NORTH);
		mypan.add(scroll, BorderLayout.CENTER);
		mypan.add(player, BorderLayout.SOUTH);
		frame.add(mypan);
	}
	@PostConstruct
	public void createComposite(Composite parent) {
		
	}


	private class tracksListRenderer extends DefaultListCellRenderer {

		// Font font = new Font("helvitica", Font.BOLD, 24);

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			label.setIcon(imageMap.get(value));
			label.setHorizontalTextPosition(SwingConstants.RIGHT);
			// label.setFont(font);
			return label;
		}
	}

	/**
	 * @param list
	 * @return a Map that contains the combination of the track name and it
	 *         respective picture
	 */
	private Map<String, ImageIcon> createImageMap(String[] list) {
		Map<String, ImageIcon> map = new HashMap<>();
		ArrayList<Track> tracks = getUserTracks();
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getUserTracksDeezer();
		for (int i = 0; i < tracks.size(); i++) {
			try {
				String url = tracks.get(i).getArtworkUrl();
				map.put(tracks.get(i).getTitle(), new ImageIcon(new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			try {

				TrackId tID = new TrackId(tracksDeezer.get(i).getId());
				deezerApi.getPreviewTrack(tID);
				String artworkUrl = tracksDeezer.get(i).getAlbum().getCover();
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));

			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * @return User's Tracks
	 */
	private ArrayList<Track> getUserTracks() {

		ArrayList<Track> tracks = soundCApi.getTracksByUser();
		return tracks;
	}

	private ArrayList<com.zeloon.deezer.domain.Track> getUserTracksDeezer() {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi
				.getTracksByUser();
		return tracks;
	}

	/**
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 */
	private String[] setNameList() {

		ArrayList<Track> tracks = getUserTracks();
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getUserTracksDeezer();
		String[] nameList = new String[tracks.size() + tracksDeezer.size()];
		for (int i = 0; i < tracks.size(); i++) {
			nameList[i] = tracks.get(i).getTitle();
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			nameList[i + tracks.size()] = tracksDeezer.get(i).getTitle();
		}
		return nameList;
	}

	/**
	 * @return User's User name to be displayed
	 */
	private String getUserName() {
		User UserData = soundCApi.getUser();
		return UserData.getUsername();
	}

	/*
	 * @Override public void valueChanged(ListSelectionEvent arg0) {
	 * 
	 * try {
	 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
	 * catch (Exception ex) { ex.printStackTrace(); }
	 * 
	 * SwingUtilities.invokeLater(new Runnable() {
	 * 
	 * @Override public void run() { int index = list.getSelectedIndex(); Object
	 * obj = tracks[index]; player.setVisible(true);
	 * player.getTrackToPlayLength(tracksLength[index], obj);
	 * player.getTrackToPlay(streamU[index]); } });
	 * 
	 * // if (currentThreadId == 0) { // CHeck if it the first Thread to be //
	 * created // player.setVisible(true); //
	 * player.getTrackToPlay(streamU[list.getSelectedIndex()]);
	 * 
	 * /* t = new Thread(new Runnable() { public void run() {
	 * appU.readAudioFeed(streamU[list.getSelectedIndex()]); }
	 * 
	 * }); t.start(); } else { // If it is not the first Thread created, we stop
	 * the first one // and start a new Thread to listen to the selected Song
	 * (To // Avoid playing all the songs Simultaneously) t.stop(); t = new
	 * Thread(new Runnable() { public void run() {
	 * appU.readAudioFeed(streamU[list.getSelectedIndex()]); //
	 * currentThreadId=t.currentThread().getId(); } }); t.start();
	 */
	// }
	// currentThreadId++;

	// }

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 */
	private String[] getTracksStream() {
		String[] sc = soundCApi.getStreamUrl();
		String[] deez = deezerApi.getStreamUrl();
		return (String[]) ArrayUtils.addAll(sc, deez);
	}

	private int[] getTrackLength() {
		int[] sc = soundCApi.getLength();
		int[] deez = deezerApi.getLength();
		return (int[]) ArrayUtils.addAll(sc, deez);
	}

	private Object[] getTracks() {
		ArrayList<Track> track = soundCApi.getTracksByUser();
		ArrayList<com.zeloon.deezer.domain.Track> trackDeezer = deezerApi
				.getTracksByUser();
		Object[] tracks = new Object[track.size() + trackDeezer.size()];
		for (int i = 0; i < track.size(); i++) {
			tracks[i] = track.get(i);
		}
		for (int i = 0; i < trackDeezer.size(); i++) {
			tracks[i + track.size()] = trackDeezer.get(i);
		}
		return tracks;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				int index = listSwing.getSelectedIndex();
				Object obj = tracks[index];
				player.setVisible(true);
				player.getTrackToPlayLength(tracksLength[index], obj);
				player.getTrackToPlay(streamU[index]);
			}
		});
	}
}
