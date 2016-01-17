package com.starterkit.javafx.texttospeech.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.starterkit.javafx.texttospeech.Speaker;

import javafx.scene.media.AudioClip;

/**
 * Uses Google Translate to transform text to speech.
 * <p>
 * This class calls Google server and plays the MP3 from returned stream.
 * Unfortunately sometimes Google detects that we are not a real web browser and
 * returns an error page or CAPTCHA page instead of a MP3 stream.
 * </p>
 * <p>
 * From time to time Google changes the TTS API. If this class stops working see
 * the following links:
 * <ul>
 * <li><a href=
 * "http://stackoverflow.com/questions/9893175/google-text-to-speech-api/34687566#34687566">
 * http://stackoverflow.com/questions/9893175/google-text-to-speech-api/34687566
 * #34687566</a>
 * <li><a href=
 * "http://stackoverflow.com/questions/32053442/google-translate-tts-api-blocked">
 * http://stackoverflow.com/questions/32053442/google-translate-tts-api-blocked
 * </a>
 * </ul>
 * </p>
 *
 * @author Leszek
 */
public class SpeakerImpl implements Speaker {

	private static final Logger LOG = Logger.getLogger(SpeakerImpl.class);

	private static final String TEXT_ENCODING = "UTF-8";

	private static final String TTS_SERVICE_URL = "https://translate.google.com/translate_tts" //
			+ "?ie=" + TEXT_ENCODING //
			+ "&total=1" //
			+ "&idx=0" //
			+ "&prev=input" //
			+ "&ttsspeed=0.35" //
			+ "&client=tw-ob";

	private final TokenGenerator tokenGenerator = new TokenGenerator();

	@Override
	public void say(String text) {
		String language = Locale.getDefault().getLanguage();
		LOG.debug("Saying '" + text + "' in language '" + language + "'");

		InputStream is = null;
		try {
			String urlStr = TTS_SERVICE_URL //
					+ "&tk=" + tokenGenerator.generateToken(text) //
					+ "&tl=" + language //
					+ "&textlen=" + text.length() //
					+ "&q=" + URLEncoder.encode(text, TEXT_ENCODING);

			LOG.debug("TTS query URL: " + urlStr);

			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();

			/*
			 * Make Google think we are a web browser :)
			 */
			urlConnection.addRequestProperty("Host", url.getHost());
			urlConnection.addRequestProperty("Referer", url.getProtocol() + "://" + url.getHost() + "/");
			urlConnection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			urlConnection.addRequestProperty("Accept",
					"audio/webm,audio/ogg,audio/wav,audio/*;q=0.9,application/ogg;q=0.7,video/*;q=0.6,*/*;q=0.5");
			urlConnection.addRequestProperty("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
			urlConnection.addRequestProperty("Range", "bytes=0-");

			urlConnection.connect();

			LOG.debug("Response headers: " + urlConnection.getHeaderFields());

			String contentType = urlConnection.getContentType();
			if (!"audio/mpeg".equals(contentType)) {
				throw new IllegalStateException("Invalid Content-Type returned: " + contentType);
			}

			/*
			 * Store the MP3 in a temporary file, because JavaFX cannot play
			 * sounds directly from an InputStream.
			 */
			File tempFile = File.createTempFile("starterkit-javafx-", ".mp3");
			is = urlConnection.getInputStream();
			Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			is.close();

			AudioClip clip = new AudioClip(tempFile.toURI().toURL().toExternalForm());
			clip.play();

			/*
			 * AudioClip sets a lock on the file, so we try to delete it when
			 * application stops.
			 */
			tempFile.delete();
			tempFile.deleteOnExit();
		} catch (Exception e) {
			throw new RuntimeException("Could not say '" + text + "' in language '" + language + "'", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.warn("Could not close input stream", e);
				}
			}
		}
	}
}
