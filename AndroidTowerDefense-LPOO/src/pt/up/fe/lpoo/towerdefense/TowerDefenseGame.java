package pt.up.fe.lpoo.towerdefense;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import pt.up.fe.lpoo.framework.FileIO;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.implementation.AndroidGame;

import android.util.Log;

import pt.up.fe.lpoo.towerdefense.R;

/**
 * This class contains the game's generic information, loading and saving methods as well as methods inherited from
 *  the framework adapted for the development of this game
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.implementation.AndroidGame
 */
public class TowerDefenseGame extends AndroidGame {

	/** information regarding the current level being played */
	public static String level;
	private boolean firstTimeCreate = true;
	/** Number of levels currently available */
	public static final int NUM_LEVELS = 12;
	private static final String LEVEL_SCORES_FILENAME = "levelScores.tds";
	private static final String CONFIG_FILENAME = "config.tdg";
	/** path for the storage area for game files */
	public static final String APP_PATH_NAME = "Android"+File.separator+"data"
			+File.separator+"pt.up.fe.lpoo.towerdefense"+File.separator;
	/** Maximum number of lives allowed in a level */
	public static final int MAX_LIVES = 5;

	/**
	 * Calls the Assets loading function when the game is created for the first time and returns the initial screen for the app.
	 * 
	 * @return returns a new SplashLoadingScreen
	 */
	@Override
	public Screen getInitScreen() {

		if (firstTimeCreate) {
			Assets.load(this);
			firstTimeCreate = false;
		}


		return new SplashLoadingScreen(this);
	}

	/**
	 * Calls the current screen's backButton function
	 */
	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}

	
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			}
		}
		return sb.toString();
	}

	/**
	 * Resumes the activity using the superclass's onResume and resumes the music
	 */
	@Override
	public void onResume() {
		super.onResume();

		if(MusicPlayer.currentTheme != null){
			if(!MusicPlayer.currentTheme.isPlaying())
				MusicPlayer.currentTheme.play();
		}

	}

	/**
	 * Pauses the activity with the superclass's onPause method and pauses the current music
	 */
	@Override
	public void onPause() {
		super.onPause();
		if(MusicPlayer.currentTheme != null)
			MusicPlayer.currentTheme.pause();
	}

	/**
	 * Loads the levels' highscores in the storage file in case it exists, sets them to 0 otherwise
	 * @param fio FileIO object to be used for reading the File
	 */
	public static void loadLevelScores(FileIO fio) {
		try {
			InputStream levels = fio.readFile(TowerDefenseGame.LEVEL_SCORES_FILENAME);

			BufferedReader reader = new BufferedReader(new InputStreamReader(levels));

			String line = null;
			try {
				int i=0;
				while ((line = reader.readLine()) != null && i < TowerDefenseGame.NUM_LEVELS) {
					Assets.levelScores.add(Integer.parseInt(line));
					i++;
				}
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			} finally {
				try {
					levels.close();
				} catch (IOException e) {
					Log.w("LOG", e.getMessage());
				}
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		}
		for(int i=Assets.levelScores.size(); i < TowerDefenseGame.NUM_LEVELS; i++){
			Assets.levelScores.add(0);
		}
	}
	/**
	 * Loads the current language configuration of the game from file in case file exists
	 * @param fio FileIO object to be used for reading the File
	 */
	public static void loadConfig(FileIO fio) {
		try {
			InputStream configStr = fio.readFile(TowerDefenseGame.CONFIG_FILENAME);

			BufferedReader reader = new BufferedReader(new InputStreamReader(configStr));

			String line = null;
			try {
				line = reader.readLine();
				if(line != null)
					Messages.setLang(Integer.parseInt(line));
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			} finally {
				try {
					configStr.close();
				} catch (IOException e) {
					Log.w("LOG", e.getMessage());
				}
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		}
	}
	
	/**
	 * Saves the current levels' highschores to the level highschores file
	 * @param fio FileIO object to be used for writing to the File
	 */
	public static void saveLevelScores(FileIO fio){
		try {
			OutputStream writeToFile = fio.writeFile(LEVEL_SCORES_FILENAME);
			for(int i=0; i < Assets.levelScores.size(); i++){
				writeToFile.write((Integer.toString(Assets.levelScores.get(i))+'\n').getBytes());
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		}
	}
	
	/**
	 * Saves the current language configuration to the configuration file
	 * @param fio FileIO object to be used for writing to the File
	 */
	public static void saveConfig(FileIO fio){
		try {
			OutputStream writeToFile = fio.writeFile(CONFIG_FILENAME);
			writeToFile.write((Integer.toString(Messages.getCurrentLang())).getBytes());
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		}
	}
	
	/**
	 * Loads a given level from the raw resources, storing the information in the level static string
	 * 
	 * @param chosenLevel number of the level to load
	 */
	public void loadLevel(int chosenLevel) {
		InputStream is = null;
		if(chosenLevel < NUM_LEVELS && chosenLevel >= 0)
			is = getResources().openRawResource(R.raw.level000+chosenLevel);
		else
			is = getResources().openRawResource(R.raw.level011);
		level = convertStreamToString(is);
	}
}