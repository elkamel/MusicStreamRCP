package musicstream.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.utils.AppUtils;

import de.voidplus.soundcloud.Playlist;
import de.voidplus.soundcloud.User; 

public class PlaylistsPart implements ListSelectionListener {
	private Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	SoundCloudApi soundCApi;
	DeezerApi deezerApi;
	Font fontLabel;
	private JList list;
	Object[] playLists;
	private Composite parent_C;

	@PostConstruct
	public void createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		parent_C = parent;
		Frame frame_1 = SWT_AWT.new_Frame(composite);
		JPanel mainPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);

		appU = new AppUtils();
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		String[] nameList = setNameList();
		playLists = setPlaylists();
		imageMap = createImageMap(nameList);
		fontLabel = new Font("helvitica", Font.BOLD, 18);

		list = new JList(nameList);
		list.setCellRenderer(new playlistsListRenderer());
		list.addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(list);
		JLabel label = new JLabel("Welcome : " + getUserName());
		label.setFont(fontLabel);
		mainPanel.add(label, BorderLayout.NORTH);
		mainPanel.add(scroll, BorderLayout.CENTER);
		frame_1.add(mainPanel);
	}

	/**
	 * @author Malek JList Renderer
	 */
	private class playlistsListRenderer extends DefaultListCellRenderer {

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

	/**
	 * @param list
	 * @return a Map that contains the combination of the playlist name and it
	 *         respective picture
	 */
	private Map<String, ImageIcon> createImageMap(String[] list) {
		Map<String, ImageIcon> map = new HashMap<>();
		ArrayList<Playlist> playlists = getUserPlaylists();
		ArrayList<com.zeloon.deezer.domain.Playlist> playlistsDeezer = getUserPlaylistsDeezer();
		for (int i = 0; i < playlists.size(); i++) {
			try {
				String url = playlists.get(i).getArtworkUrl();
				map.put(playlists.get(i).getTitle(), new ImageIcon(new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < playlistsDeezer.size(); i++) {
			try {
				String url = playlistsDeezer.get(i).getPicture();
				map.put(playlistsDeezer.get(i).getTitle(), new ImageIcon(new URL(url)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * @return User Playlists
	 */
	private ArrayList<Playlist> getUserPlaylists() {

		ArrayList<Playlist> playlists = soundCApi.getPlaylistByUser();
		return playlists;
	}

	private ArrayList<com.zeloon.deezer.domain.Playlist> getUserPlaylistsDeezer() {
		ArrayList<com.zeloon.deezer.domain.Playlist> playlistsDeezer = deezerApi.getPlaylistByUser();
		return playlistsDeezer;
	}

	/**
	 * @return Array that contains the names of the playlists to be associated
	 *         with their images and to be displayed in the JList
	 */
	private String[] setNameList() {

		ArrayList<Playlist> playlists = getUserPlaylists();
		ArrayList<com.zeloon.deezer.domain.Playlist> playlistsDeezer = getUserPlaylistsDeezer();
		String[] nameList = new String[playlists.size() + playlistsDeezer.size()];
		for (int i = 0; i < playlists.size(); i++) {
			nameList[i] = playlists.get(i).getTitle();
		}
		for (int i = 0; i < playlistsDeezer.size(); i++) {
			nameList[i + playlists.size()] = playlistsDeezer.get(i).getTitle();
		}
		return nameList;
	}

	private Object[] setPlaylists() {
		ArrayList<Playlist> playlists = getUserPlaylists();
		ArrayList<com.zeloon.deezer.domain.Playlist> playlistsDeezer = getUserPlaylistsDeezer();
		Object[] playList = new Object[playlists.size() + playlistsDeezer.size()];
		for (int i = 0; i < playlists.size(); i++) {
			playList[i] = playlists.get(i);
		}
		for (int i = 0; i < playlistsDeezer.size(); i++) {
			playList[i + playlists.size()] = playlistsDeezer.get(i);
		}
		return playList;
	}

	/**
	 * @return User's User name to be displayed
	 */
	private String getUserName() {
		User UserData = soundCApi.getUser();
		return UserData.getUsername();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		final Object playlist = playLists[list.getSelectedIndex()];
		 
//		Display.getDefault().syncExec(new Runnable() {
//			public void run() {
//				try {
//					DetailedPlaylistWindow window = new DetailedPlaylistWindow(playlist);
//					window.setBlockOnOpen(true);
//					window.open();
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//		}); 

	}
}
