package com.starterkit.javafx.texttospeech;

import com.starterkit.javafx.texttospeech.impl.SpeakerImpl;

/**
 * Transforms text to speech.
 *
 * @author Leszek
 */
public interface Speaker {

	/**
	 * Instance of this interface.
	 */
	Speaker INSTANCE = new SpeakerImpl();

	/**
	 * Says given text in system default language.
	 *
	 * @param text
	 *            text to say
	 */
	void say(String text);
}