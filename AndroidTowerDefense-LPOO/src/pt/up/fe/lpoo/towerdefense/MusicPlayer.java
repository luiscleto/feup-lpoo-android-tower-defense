package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Music;

/**
 * This class serves as a handler for the application's music, allowing the themes to be easily swapped and providing
 *  fade out and fade in effects for smooth transitions between songs
 *  
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class MusicPlayer {
	/** current theme being played */
	public static Music currentTheme = null;
	private static Music nextTheme = null;
	private static boolean changingThemes = false;
	private static boolean resettingSound = false;
	/** Volume for the background music */
	public static final float MAX_VOLUME = 0.85f;
	private static float currentVolume = 0.85f;
	/** Volume for game sounds */
	public static float SOUNDS_VOLUME = 0.5f;

	/**
	 * Sets a new theme to be played next and toggles the theme transition
	 * @param newTheme theme to be played
	 */
	public static void changeCurrentTheme(Music newTheme){
		if(newTheme.equals(currentTheme))
			return;
		if(currentTheme != null){
			nextTheme = newTheme;
			changingThemes = true;
		}
		else{
			currentTheme = newTheme;
			currentTheme.setVolume(MAX_VOLUME);
			currentTheme.play();
		}
	}

	/**
	 * Handles the theme transition to make it smooth by lowering the volume of the current theme before stopping it
	 *  and increasing the volume of the new theme until it reaches the preset volume
	 * @param deltaTime time elapsed since the last update
	 */
	public static void update(float deltaTime){
		if(changingThemes){
			currentVolume -= deltaTime/100.0f;
			if(currentVolume <= 0){
				currentTheme.stop();
				currentTheme.setVolume(0);
				currentTheme = nextTheme;
				nextTheme = null;
				changingThemes = false;
				resettingSound = true;
				currentTheme.setVolume(0);
				currentTheme.play();
			}
			else{
				currentTheme.setVolume(currentVolume);
			}
		}
		else if(resettingSound){
			currentVolume += deltaTime/100.0f;
			if(currentVolume >= MAX_VOLUME){
				currentVolume = MAX_VOLUME;
				resettingSound = false;
			}
			currentTheme.setVolume(currentVolume);
		}
	}
}
