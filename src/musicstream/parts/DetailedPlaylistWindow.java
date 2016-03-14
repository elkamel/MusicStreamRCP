package musicstream.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;
import com.zeloon.deezer.domain.internal.TrackId;

import de.voidplus.soundcloud.Playlist;
import de.voidplus.soundcloud.Track;
import de.voidplus.soundcloud.User;

public class DetailedPlaylistWindow extends ApplicationWindow implements ListSelectionListener {
	private Object playlist;
	private Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	private SoundCloudApi soundCApi;
	private DeezerApi deezerApi;
	private MusicPlayer player;
	private JList list;
	private String[] nameList;
	private String[] streamU;
	private int[] tracksLength;
	private String[] tracksSource;
	private JScrollPane scroll;
	private String title = "";

	public DetailedPlaylistWindow(Object playlist) {
		super(null);
		this.playlist = playlist;
		tracksSource = new String[100];
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		player = new MusicPlayer();
		tracksLength = this.getTrackLength();
	}

	protected Control createContents(Composite parent) {
		// Composite composite = new Composite(parent, SWT.EMBEDDED);
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		composite.setLayout(new FillLayout());
		Frame frame = SWT_AWT.new_Frame(composite);
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 884, 661);
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		nameList = setNameList();
		imageMap = createImageMap(nameList);
		list = new JList(nameList);
		Font font = new Font("helvitica", Font.BOLD, 18);
		list.setCellRenderer(new tracksListRenderer());
		list.addListSelectionListener(this);
		scroll = new JScrollPane(list);
		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setFont(font);
		player.setVisible(false);

		mainPanel.add(label, BorderLayout.NORTH);
		mainPanel.add(scroll, BorderLayout.CENTER);
		mainPanel.add(player, BorderLayout.SOUTH);
		frame.add(mainPanel);
		return composite;
	}

	private void createActions() {
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Playlist  " + this.title);
	}

	protected Point getInitialSize() {
		return new Point(900, 875);
	}

	private Map<String, ImageIcon> createImageMap(String[] list) {
		Map<String, ImageIcon> map = new HashMap<>();
		if (playlist instanceof Playlist) {
			this.title = ((Playlist) playlist).getTitle();
			streamU = this.getTracksStream(playlist);
			ArrayList<Track> tracks = getPlaylistTracks((Playlist) playlist);
			for (int i = 0; i < tracks.size(); i++) {
				try {
					String url = tracks.get(i).getArtworkUrl();

					map.put(tracks.get(i).getTitle(), new ImageIcon(new URL(url)));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else {
			ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getPlaylistTracksDeezer(
					(com.zeloon.deezer.domain.Playlist) playlist);
			streamU = this.getTracksStream(playlist);
			for (int i = 0; i < tracksDeezer.size(); i++) {
				try {

					TrackId tID = new TrackId(tracksDeezer.get(i).getId());
					this.title = ((com.zeloon.deezer.domain.Playlist) playlist).getTitle();
					deezerApi.getPreviewTrack(tID);
					String artworkUrl = tracksDeezer.get(i).getAlbum().getCover();
					URL url = new URL(artworkUrl);
					map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));

				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		}

		return map;
	}

	private ArrayList<com.zeloon.deezer.domain.Track> getPlaylistTracksDeezer(com.zeloon.deezer.domain.Playlist pl) {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = deezerApi.getTracksofPlaylist(pl);
		return tracks;
	}

	private ArrayList<Track> getPlaylistTracks(Playlist playlist) {
		ArrayList<Track> tracks = soundCApi.getTracksofPlaylist(playlist);
		return tracks;
	}

	private String[] setNameList() {
		String[] nameListTemp;
		if (playlist instanceof Playlist) {
			ArrayList<Track> tracks = getPlaylistTracks((Playlist) playlist);
			nameListTemp = new String[tracks.size()];
			for (int i = 0; i < tracks.size(); i++) {
				nameListTemp[i] = tracks.get(i).getTitle();
				System.out.println(tracks.get(i).getTitle());
				tracksSource[i] = "Soundcloud";
			}
		} else {
			ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getPlaylistTracksDeezer(
					(com.zeloon.deezer.domain.Playlist) playlist);
			nameListTemp = new String[tracksDeezer.size()];
			for (int i = 0; i < tracksDeezer.size(); i++) {
				nameListTemp[i] = tracksDeezer.get(i).getTitle();
				tracksSource[i] = "Deezer";
			}
		}

		return nameListTemp;
	}

	private String getUserName() {
		User UserData = soundCApi.getUser();
		return UserData.getUsername();
	}

	private String[] getTracksStream(Object playlist) {
		if (playlist instanceof Playlist) {
			return (soundCApi.getStreamUrlPlaylist(playlist));
		} else {
			return deezerApi.getStreamUrlPlaylist(playlist);
		}

	}

	private int[] getTrackLength() {
		int[] sc = soundCApi.getLength();
		int[] deez = deezerApi.getLength();
		return (int[]) ArrayUtils.addAll(sc, deez);
	}

	private class tracksListRenderer extends DefaultListCellRenderer {

		Font font = new Font("helvitica", Font.BOLD, 24);

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setIcon(imageMap.get(value));
			label.setHorizontalTextPosition(SwingConstants.RIGHT);
			label.setFont(font);
			return label;
		}
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
				int index = list.getSelectedIndex();
				String source = tracksSource[index];
				player.setVisible(true);
				player.getTrackToPlayLength(tracksLength[index], source);
				player.getTrackToPlay(streamU[index]);
			}
		});
	}
}
