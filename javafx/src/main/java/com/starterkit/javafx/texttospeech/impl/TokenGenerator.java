package com.starterkit.javafx.texttospeech.impl;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Token generator for Google TTS service call.
 * <p>
 * The {@code generate-token.js} script is extracted from <a href=
 * "https://translate.google.com/translate/releases/twsfe_w_20160111_RC00/r/js/desktop_module_main.js">
 * https://translate.google.com/translate/releases/twsfe_w_20160111_RC00/r/js/
 * desktop_module_main.js</a>
 * </p>
 *
 * @author Leszek
 */
class TokenGenerator {

	private final Invocable inv;

	TokenGenerator() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			Reader reader = new InputStreamReader(getClass().getResourceAsStream("generate-token.js"));
			engine.eval(reader);
			inv = (Invocable) engine;
		} catch (ScriptException e) {
			throw new RuntimeException("Could not initialize " + getClass().getSimpleName(), e);
		}
	}

	/**
	 * Generates token for given text.
	 *
	 * @param text
	 *            text
	 * @return token
	 */
	String generateToken(String text) {
		try {
			Object ret = inv.invokeFunction("fM", text);

			return ret.toString();
		} catch (ScriptException | NoSuchMethodException e) {
			throw new RuntimeException("Could not generate token", e);
		}
	}
}
