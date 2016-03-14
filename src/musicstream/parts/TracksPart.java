package musicstream.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

public class TracksPart implements ListSelectionListener {
	private Map<String, ImageIcon> imageMap;
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
	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.EMBEDDED);
		Frame frame_1 = SWT_AWT.new_Frame(composite);
		JPanel mainPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);

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
		scroll.setPreferredSize(new Dimension(appU.getScreenWidth() - 60, appU.getScreenHeight() - 100));
		scroll.setBounds(10, 50, appU.getScreenWidth() - 40, appU.getScreenHeight() - 300);
		java.awt.Font fontLabel = new java.awt.Font("helvitica", java.awt.Font.BOLD, 18);
		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setBounds(10, -15, 250, 75);
		label.setFont(fontLabel);
		player.setBounds(10, appU.getScreenHeight() - 150, appU.getScreenWidth() - 40, 80);
		player.setVisible(false);
		mainPanel.add(label, BorderLayout.NORTH);
		mainPanel.add(scroll, BorderLayout.CENTER);
		mainPanel.add(player, BorderLayout.SOUTH);
		frame_1.add(mainPanel);

	}

	@Focus
	public void setFocus() {
		// tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	private class tracksListRenderer extends DefaultListCellRenderer {

		// Font font = new Font("helvitica", Font.BOLD, 24);

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
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
		ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi.getTracksByUser();
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
		ArrayList<com.zeloon.deezer.domain.Track> trackDeezer = deezerApi.getTracksByUser();
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