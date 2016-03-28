package com.musicstream.player;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

import com.musicstream.utils.AppUtils;

public class MusicPlayer extends JPanel implements ActionListener {

	private AudioPlayer player;
	private Thread playbackThread;
	private PlayingTimer timer;
	public AppUtils appU;
	public String url;
	public int length;
	public String source;
	// private static Thread t;
	private boolean isPlaying = false;
	private boolean isPause = false;

	// private String audioFilePath;
	// private String lastOpenPath;

	// private JLabel labelFileName = new JLabel("Playing File:");
	private JLabel labelTimeCounter = new JLabel("00:00:00");
	private JLabel labelDuration = new JLabel("00:00:00");

	// private JButton buttonOpen = new JButton("Open");
	private JButton buttonPlay = new JButton("Play");
	private JButton buttonPause = new JButton("Pause");

	private JSlider sliderTime = new JSlider();

	ActionListener tache_timer;
	private static int heure = 0, minute = 0, seconde = 0;
	final Timer chrono;
	private int actualSeconds = 0;

	// Icons used for buttons
	/*
	 * private ImageIcon iconOpen = new ImageIcon(getClass().getResource(
	 * "images/Open.png"));
	 */
	private ImageIcon iconPlay = new ImageIcon("icons/Play.gif");
	private ImageIcon iconStop = new ImageIcon("icons/Stop.gif");
	private ImageIcon iconPause = new ImageIcon("icons/Pause.png");

	public MusicPlayer() {
		appU = new AppUtils();
		player = new AudioPlayer();
		setLayout(new FlowLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;

		/*
		 * buttonOpen.setFont(new Font("Sans", Font.BOLD, 14));
		 * buttonOpen.setIcon(iconOpen);
		 */

		buttonPlay.setFont(new Font("Sans", Font.BOLD, 14));
		buttonPlay.setIcon(iconPlay);
		buttonPlay.setEnabled(true);
		buttonPlay.setPreferredSize(new Dimension(120, 30));

		buttonPause.setFont(new Font("Sans", Font.BOLD, 14));
		buttonPause.setIcon(iconPause);
		buttonPause.setEnabled(false);
		buttonPause.setPreferredSize(new Dimension(120, 30));

		labelTimeCounter.setFont(new Font("Sans", Font.BOLD, 12));
		labelDuration.setFont(new Font("Sans", Font.BOLD, 12));

		sliderTime.setPreferredSize(new Dimension(400, 10));
		sliderTime.setEnabled(false);
		sliderTime.setValue(0);

		// constraints.gridx = 0;
		// constraints.gridy = 0;
		// constraints.gridwidth = 3;
		// add(labelFileName, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		add(buttonPlay, constraints);

		constraints.gridx = 2;
		add(buttonPause, constraints);

		constraints.gridx = 3;
		add(labelTimeCounter, constraints);

		constraints.gridx = 5;
		add(sliderTime, constraints);

		constraints.gridx = 6;
		add(labelDuration, constraints);

		// JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER,
		// 20, 5));
		// panelButtons.add(buttonOpen);
		// panelButtons.add(buttonPlay);
		// panelButtons.add(buttonPause);

		// constraints.gridwidth = 3;
		// constraints.gridx = 0;
		// constraints.gridy = 2;
		// add(panelButtons, constraints);

		// buttonOpen.addActionListener(this);
		buttonPlay.addActionListener(this);
		buttonPause.addActionListener(this);

		timer = new PlayingTimer(labelTimeCounter, sliderTime);
		tache_timer = new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				if (!isPause) {
					seconde++;
				}
				actualSeconds = seconde;
				timer.setActualSeconds(actualSeconds);
				if (seconde == 60) {
					seconde = 0;
					minute++;
				}
				if (minute == 60) {
					minute = 0;
					heure++;
				}
			}
		};
		chrono = new Timer(1000, tache_timer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// Runnable
		// Thread timer = new Thread();

		if (source instanceof JButton) {
			JButton button = (JButton) source;
			if (button == buttonPlay) {
				if (!isPlaying) {
					playBack(url, length);

				} else {
					stopPlaying();
				}
			} else if (button == buttonPause) {
				if (!isPause) {
					pausePlaying();
				} else {
					resumePlaying();
				}
			}
		}

	}

	/**
	 * Start playing back the sound.
	 */
	public void playBack(final String url, final int length) {
		timer = new PlayingTimer(labelTimeCounter, sliderTime);
		timer.start();
		isPlaying = true;
		playbackThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					buttonPlay.setText("Stop");
					buttonPlay.setIcon(iconStop);
					buttonPlay.setEnabled(true);

					buttonPause.setText("Pause");
					buttonPause.setEnabled(true);

					labelDuration.setText(player.getClipLengthString(length,
							source));
					sliderTime.setMaximum(player.getLength(length, source));
					chrono.start();
					appU.readAudioFeed(url);
					// player.play();
					if (sliderTime.getValue() == player.getLength(length,
							source)) {
						resetControls();
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(MusicPlayer.this,
							"I/O error while playing the audio file!", "Error",
							JOptionPane.ERROR_MESSAGE);
					resetControls();
					ex.printStackTrace();
				}

			}
		});

		playbackThread.start();
	}

	@SuppressWarnings("deprecation")
	private void stopPlaying() {
		isPause = false;
		buttonPause.setText("Pause");
		buttonPause.setEnabled(false);
		chrono.stop();
		timer.reset();
		timer.interrupt();
		player.stop();
		sliderTime.setValue(0);
		playbackThread.stop();
		resetControls();
	}

	@SuppressWarnings("deprecation")
	private void pausePlaying() {
		buttonPause.setText("Resume");
		buttonPause.setIcon(iconPlay);
		isPause = true;
		player.pause();
		timer.pauseTimer(actualSeconds);
		playbackThread.suspend();
	}

	@SuppressWarnings("deprecation")
	private void resumePlaying() {
		buttonPause.setText("Pause");
		buttonPause.setIcon(iconPause);
		isPause = false;
		player.resume();
		timer.resumeTimer();
		playbackThread.resume();
	}

	private void resetControls() {
		timer.reset();
		timer.interrupt();
		chrono.stop();
		buttonPlay.setText("Play");
		buttonPlay.setIcon(iconPlay);
		buttonPause.setEnabled(false);
		isPlaying = false;

	}

	public void getTrackToPlay(String url) {
		this.url = url;

	}

	public void getTrackToPlayLength(int length, String source) {
		this.length = length;
		this.source = source;
	}

}
