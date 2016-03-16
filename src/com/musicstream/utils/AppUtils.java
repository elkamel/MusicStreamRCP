package com.musicstream.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Malek
 * 
 */
public class AppUtils {
	Dimension screenSize;
	private static final int BUFFER_SIZE = 176400; // 44100 x 16 x 2 / 8

	public AppUtils() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * @return Screen Width
	 */
	public int getScreenWidth() {
		return screenSize.width;
	}

	/**
	 * @return Screen Height
	 */
	public int getScreenHeight() {
		return screenSize.height;
	}

	/**
	 * @return Array contains Decrypted User credentials
	 */
	public String[] getUserCredentials() {
		String[] cred = new String[2];
		File encryptedFile = new File("document.encrypted");
		File decryptedFile = new File("document.decrypted");
		String key = "Mary has one cat";
		try {
			CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
		} catch (CryptoException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		BufferedReader br = null;
		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader("document.decrypted"));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				cred[i] = sCurrentLine;
				i++;
			}
			// System.out.println(cred.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		decryptedFile.deleteOnExit();
		return cred;
	}

	/**
	 * Delete files storing user Credentials
	 */
	public void deleteFiles() {
		File encryptedFile = new File("document.encrypted");
		File decryptedFile = new File("document.decrypted");
		encryptedFile.deleteOnExit();
		decryptedFile.deleteOnExit();

	}

	/**
	 * @param url : Plays an audio feed from an url
	 */
	public void readAudioFeed(String url) {

		byte[] buffer = new byte[BUFFER_SIZE];
		AudioInputStream in;

		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

			InputStream is = conn.getInputStream();
			InputStream bufferedIn = new BufferedInputStream(is);

			in = AudioSystem.getAudioInputStream(
					AudioFormat.Encoding.PCM_SIGNED,
					AudioSystem.getAudioInputStream(bufferedIn));
			AudioFormat audioFormat = in.getFormat();
			SourceDataLine line = (SourceDataLine) AudioSystem
					.getLine(new DataLine.Info(SourceDataLine.class,
							audioFormat));
			line.open(audioFormat);
			line.start();
			while (true) {
				int n = in.read(buffer, 0, buffer.length);
				if (n < 0) {
					break;
				}
				line.write(buffer, 0, n);
			}
			line.drain();
			line.close();
		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param v
	 * @return cleaning an list
	 */
	public static String[] clean(final String[] v) {
		List<String> list = new ArrayList<String>(Arrays.asList(v));
		list.removeAll(Collections.singleton(null));
		return list.toArray(new String[list.size()]);
	}
}
