package com.starterkit.javafx.textospeech.impl;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.starterkit.javafx.textospeech.Speaker;

import javazoom.jl.player.Player;

/**
 * Uses Google Translate to transform text to speech.
 * <p>
 * This class calls Google server and plays the MP3 from returned stream.
 * Unfortunately sometimes Google detects that we are not a real web browser and
 * returns an error page instead of a MP3 stream.
 * </p>
 *
 * @author Leszek
 */
public class SpeakerImpl implements Speaker {

	private static final Logger LOG = Logger.getLogger(SpeakerImpl.class);

	private static final String TTS_SERVICE_URL = "http://translate.google.com/translate_tts?client=t&";

	@Override
	public void say(String text) {
		Locale locale = Locale.getDefault();
		LOG.debug("Saying '" + text + "' in language '" + locale + "'");

		Player player = null;
		try {
			String urlStr = TTS_SERVICE_URL + "tl=" + locale + "&q=" + URLEncoder.encode(text, "UTF-8");

			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();

			/*
			 * Make Google think we are a web browser :)
			 */
			urlConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");

			LOG.debug("Returned Content-Type: " + urlConnection.getContentType());

			player = new Player(urlConnection.getInputStream());
			player.play();
		} catch (Exception e) {
			throw new RuntimeException("Could not say '" + text + "' in language '" + locale + "'", e);
		} finally {
			if (player != null) {
				player.close();
			}
		}
	}
}
