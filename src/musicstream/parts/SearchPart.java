package musicstream.parts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.musicstream.api.DeezerApi;
import com.musicstream.api.SoundCloudApi;
import com.musicstream.player.MusicPlayer;
import com.musicstream.utils.AppUtils;

import de.voidplus.soundcloud.Track;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class SearchPart implements ListSelectionListener, ActionListener {
	public static JTextField textField;
	private Map<String, ImageIcon> imageMap;
	public AppUtils appU;
	private DeezerApi deezerApi;
	private Font fontLabel;
	private String[] nameList;
	private JList list;
	private JScrollPane scroll = null;
	private SoundCloudApi soundCApi;
	private String[] streamU;
	private int[] tracksLength;
	private MusicPlayer player;
	private String title;
	private Object[] tracks;
	private Text txtInput;
	private JButton search;
	private String[] tracksSource;

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.EMBEDDED);
		composite.setBounds(5, 45, 441, 253);
		Frame frame_1 = SWT_AWT.new_Frame(composite);
		JPanel mainPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		mainPanel.setLayout(layout);
		JPanel headPanel = new JPanel();
		tracksSource = new String[100];
		textField = new JTextField();
		textField.setToolTipText("Please Enter The Text of Your Search");
		search = new JButton("Search");
		search.addActionListener(this);
		headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.X_AXIS));

		headPanel.add(textField);
		headPanel.add(search);

		appU = new AppUtils();
		soundCApi = new SoundCloudApi();
		deezerApi = new DeezerApi();
		fontLabel = new Font("helvitica", Font.BOLD, 18);

		list = new JList();
		list.setValueIsAdjusting(true);
		list.addListSelectionListener(this);

		player = new MusicPlayer();
		player.setVisible(false);
		scroll = new JScrollPane(list);

		mainPanel.add(headPanel, BorderLayout.NORTH);
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
				player.setVisible(true);
				String source = tracksSource[list.getSelectedIndex()];
				player.getTrackToPlayLength(tracksLength[list.getSelectedIndex()], source);
				player.getTrackToPlay(streamU[list.getSelectedIndex()]);
			}
		});

	}

	/**
	 * @author Salim
	 * @return JList Renderer
	 */
	public class SearchTrack extends DefaultListCellRenderer {
		String title;
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
	 * @param title
	 * @return a Map that contains the combination of tracks and it respective
	 *         picture
	 * @throws MalformedURLException
	 */
	private Map<String, ImageIcon> createImageMap(String title) throws MalformedURLException {
		Map<String, ImageIcon> map = new HashMap<>();
		ArrayList<Track> tracks = new ArrayList<Track>();
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = new ArrayList<com.zeloon.deezer.domain.Track>();
		tracks = getSearchResult(title);
		tracksDeezer = getSearchResultDeezer(title);
		String artworkUrl;
		for (int i = 0; i < tracks.size(); i++) {
			try {
				artworkUrl = tracks.get(i).getArtworkUrl();
				URL url = new URL(artworkUrl);
				map.put(tracks.get(i).getTitle(), new ImageIcon(url));

			} catch (Exception ex) {
				// Managing Exception when track does'nt have a cover Image
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(tracks.get(i).getTitle(), new ImageIcon(url));
			}
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			try {
				artworkUrl = tracksDeezer.get(i).getAlbum().getCover();
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));
			} catch (Exception ex) {
				// ex.printStackTrace();
				artworkUrl = "https://yt3.ggpht.com/-b05GwzWbqZE/AAAAAAAAAAI/AAAAAAAAAAA/_d2WA1qZyi8/s100-c-k-no/photo.jpg";
				URL url = new URL(artworkUrl);
				map.put(tracksDeezer.get(i).getTitle(), new ImageIcon(url));
			}
		}
		return map;
	}

	/**
	 * @param title
	 * @return the research results
	 */
	public ArrayList<Track> getSearchResult(String title) {

		ArrayList<Track> tracks = new ArrayList<Track>();
		tracks = soundCApi.getTrack(title);
		return tracks;
	}

	public ArrayList<com.zeloon.deezer.domain.Track> getSearchResultDeezer(String title) {
		ArrayList<com.zeloon.deezer.domain.Track> tracks = new ArrayList<com.zeloon.deezer.domain.Track>();
		tracks = deezerApi.getTrack(title);
		return tracks;
	}

	/**
	 * @param title
	 * @return Array that contains the names of the tracks to be associated with
	 *         their images and to be displayed in the JList
	 */
	public String[] setNameList(String title) {

		ArrayList<Track> tracks = getSearchResult(title);
		ArrayList<com.zeloon.deezer.domain.Track> tracksDeezer = getSearchResultDeezer(title);
		String[] nameList = new String[tracks.size() + tracksDeezer.size()];
		for (int i = 0; i < tracks.size(); i++) {
			nameList[i] = tracks.get(i).getTitle();
			tracksSource[i] = "Soundcloud";
		}
		for (int i = 0; i < tracksDeezer.size(); i++) {
			nameList[i + tracks.size()] = tracksDeezer.get(i).getTitle();
			tracksSource[i + tracks.size()] = "Deezer";
		}
		return nameList;
	}

	private Object[] getTracks() {
		try {
			ArrayList<Track> track = soundCApi.getTrack(title);
			ArrayList<com.zeloon.deezer.domain.Track> trackDeezer = deezerApi.getTrack(title);
			Object[] tracks = new Object[track.size() + trackDeezer.size()];
			for (int i = 0; i < track.size(); i++) {
				tracks[i] = track.get(i);
			}
			for (int i = 0; i < trackDeezer.size(); i++) {
				tracks[i + track.size()] = trackDeezer.get(i);
			}
			
		} catch (Exception e) {
		System.out.println("Timeout");
		}
		return tracks;
	}

	/**
	 * @return Array contains the stream URL of each Track from the two Services
	 */
	private String[] getTracksStream() {
		String[] sc = soundCApi.getStreamUrlSearch(title);
		String[] deez = deezerApi.getStreamUrlSearch(title);
		return (String[]) ArrayUtils.addAll(sc, deez);
	}

	private int[] getTrackLength() {
		int[] sc = soundCApi.getLengthSearch(title);
		int[] deez = deezerApi.getLengthSearch(title);
		return (int[]) ArrayUtils.addAll(sc, deez);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (textField.getText().length() != 0) {
			title = textField.getText();
			scroll.setVisible(true);

			nameList = null;
			imageMap = null;

			nameList = setNameList(title);
			try {
				imageMap = createImageMap(title);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			list.setListData(nameList);
			list.setCellRenderer(new SearchTrack());
			scroll.setVisible(true);
			tracksLength = this.getTrackLength();
			streamU = this.getTracksStream();
			tracks = this.getTracks();
		} else {
			JOptionPane.showMessageDialog(null, "Please Type a song to search", "MusicStream - Invalid Input",
					JOptionPane.ERROR_MESSAGE);
		}

	}
}
