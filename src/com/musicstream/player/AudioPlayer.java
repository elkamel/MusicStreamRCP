package com.musicstream.player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An utility class for playing back audio files using Java Sound API.
 * 
 */
public class AudioPlayer implements LineListener {

	/**
	 * this flag indicates whether the playback completes or not.
	 */
	private boolean playCompleted;

	/**
	 * this flag indicates whether the playback is stopped or not.
	 */
	private boolean isStopped;

	private boolean isPaused;

	private Clip audioClip;

	/**
	 * Load audio file before playing back
	 * 
	 * @param audioFilePath
	 *            Path of the audio file.
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public void load(String audioFilePath)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
		File audioFile = new File(audioFilePath);

		AudioInputStream audioStream = AudioSystem
				.getAudioInputStream(audioFile);

		AudioFormat format = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(Clip.class, format);

		audioClip = (Clip) AudioSystem.getLine(info);

		audioClip.addLineListener(this);

		audioClip.open(audioStream);
	}

	public long getClipSecondLength(int sec) {
		long s = sec;
		return s;
	}

	public String getClipLengthString(int seconds, String source) {
		String length = "";
		int hour = 0;
		int minute = 0;

		if (source.equals("Soundcloud")) {
			seconds = (int) TimeUnit.MILLISECONDS.toSeconds(seconds);
			minute = (int) TimeUnit.MILLISECONDS.toMinutes(seconds);
			hour = (int) TimeUnit.MILLISECONDS.toHours(seconds);
		}

		if (seconds >= 3600) {
			hour = ((int) seconds / 3600);
			length = String.format("%02d:", hour);
		} else {
			length += "00:";
		}

		int min = seconds - hour * 3600;
		if (min >= 60) {
			minute = ((int) min / 60);
			length += String.format("%02d:", minute);

		} else {
			length += "00:";
		}

		int second = seconds - hour * 3600 - minute * 60;

		length += String.format("%02d", second);

		return length;
	}

	public int getLength(int seconds, String source) {
		if (source.equals("Soundcloud")) {
			return (int) TimeUnit.MILLISECONDS.toSeconds(seconds);
		} else
			return seconds;
	}

	/**
	 * Play a given audio file.
	 * 
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	void play() throws IOException {

		audioClip.start();

		playCompleted = false;
		isStopped = false;

		while (!playCompleted) {
			// wait for the playback completes
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				if (isStopped) {
					audioClip.stop();
					break;
				}
				if (isPaused) {
					audioClip.stop();
				} else {
					System.out.println("!!!!");
					audioClip.start();
				}
			}
		}

		audioClip.close();

	}

	/**
	 * Stop playing back.
	 */
	public void stop() {
		isStopped = true;
	}

	public void pause() {
		isPaused = true;
	}

	public void resume() {
		isPaused = false;
	}

	/**
	 * Listens to the audio line events to know when the playback completes.
	 */
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			System.out.println("STOP EVENT");
			if (isStopped || !isPaused) {
				playCompleted = true;
			}
		}
	}

	public Clip getAudioClip() {
		return audioClip;
	}
}