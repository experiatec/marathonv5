package net.sourceforge.marathon.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public final class GlobalKeyListener implements NativeKeyListener {
	public static final Logger LOGGER = Logger.getLogger(GlobalKeyListener.class.getName());

	Logger JNH_LOGGER = Logger.getLogger(GlobalScreen.class.getPackage().getName());
	
	private static GlobalKeyListener globalkeyListener;

	private boolean windowRecognitionModeEnabled = false;

	private GlobalKeyListener() {
		super();
		JNH_LOGGER.setLevel(Level.SEVERE);
		JNH_LOGGER.setUseParentHandlers(false);
		LOGGER.log(Level.INFO, "Initializing GlobalKeyListener...");

	}

	public static GlobalKeyListener getInstance() {
		if (globalkeyListener == null) {
			globalkeyListener = new GlobalKeyListener();
		}
		return globalkeyListener;
	}

	public boolean isWindowRecognitionModeEnabled() {
		return windowRecognitionModeEnabled;
	}

	public void setWindowRecognitionModeEnabled(boolean windowRecognitionModeEnabled) {
		this.windowRecognitionModeEnabled = windowRecognitionModeEnabled;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			windowRecognitionModeEnabled = true;
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			windowRecognitionModeEnabled = false;
		}
	}

	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
		// N/A
	}
}