package pt.up.fe.lpoo.towerdefense;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Typeface;
import pt.up.fe.lpoo.framework.Image;
import pt.up.fe.lpoo.framework.Music;
import pt.up.fe.lpoo.framework.Sound;

/**
 * This class contains the assets to be used by the game such as images, sounds, musics and typefaces
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class Assets {
    
    public static Image menu, splash, menu_box, lifeIcon, coinIcon, infoBarBG, startIcon;
    public static Image tilePath, tileEmptyTower, tileGoal, tileEntrance, selectionOverlay;
    public static Image turretTowerLevel1, turretTowerLevel2, turretProjectile;
    public static Image boulderTower, boulderProjectile;
    public static Image barricade, frozenEffect, freezeTower, poisonTower, poisonEffect, slowEffect, tarPool;
    public static Image cancelIcon, removeIcon, upgradeIcon, repairIcon, buildingOptionsBG;
    
    public static Image ptIcon, enIcon, pirateIcon;
    
    public static Animation slimeMovement;
    public static Animation skullUpMovement,skullDownMovement,skullLeftMovement,skullRightMovement;
    public static Animation golemUpMovement,golemDownMovement,golemLeftMovement,golemRightMovement;
    
    public static Music menuTheme, buildUpTheme, gameOverTheme, victoryTheme, pirateTheme;
    public static Sound buttonClick;
    /** Theme songs for each level stored here */
    public static ArrayList<Music> levelThemes;
    /** Highscores for each level */
	public static ArrayList<Integer> levelScores; 
	/** Typeface used for font drawing */
    public static Typeface tf;
    
    public static final String fontsFolderName = "fonts"+File.separator;
    public static final String musicFolderName = "music"+File.separator;
    public static final String soundsFolderName = "sounds"+File.separator;
    public static final String imagesFolderName = "images"+File.separator;
    
    /**
     * Loads all the Music objects
     * @param game Game object with the Audio interface to be used to load the music
     */
    public static void load(TowerDefenseGame game) {
    	levelThemes = new ArrayList<Music>();
    	
        menuTheme = game.getAudio().createMusic(musicFolderName+MainMenuScreen.MENU_THEME);
        menuTheme.setLooping(true);
        menuTheme.setVolume(0.85f);
        
        buildUpTheme = game.getAudio().createMusic(musicFolderName+"buildUpTheme.mp3");
        buildUpTheme.setLooping(true);
        buildUpTheme.setVolume(0.85f);
        
        gameOverTheme = game.getAudio().createMusic(musicFolderName+"gameOverTheme.mp3");
        gameOverTheme.setLooping(true);
        gameOverTheme.setVolume(0.85f);
        
        pirateTheme = game.getAudio().createMusic(musicFolderName+"pirateTheme.mp3");
        pirateTheme.setLooping(true);
        pirateTheme.setVolume(0.85f);
        
        victoryTheme = game.getAudio().createMusic(musicFolderName+"victoryTheme.mp3");
        victoryTheme.setLooping(true);
        victoryTheme.setVolume(0.85f);
        
        levelThemes.add(game.getAudio().createMusic(musicFolderName+"levelEasy.mp3"));
        levelThemes.get(0).setLooping(true);
    	levelThemes.get(0).setVolume(0.85f);
        for(int i=1; i < TowerDefenseGame.NUM_LEVELS; i++){
        	if(i==4){
        		levelThemes.add(game.getAudio().createMusic(musicFolderName+"levelMid.mp3"));
        		levelThemes.get(i).setLooping(true);
        		levelThemes.get(i).setVolume(0.85f);
        	}
        	else if(i==8){
        		levelThemes.add(game.getAudio().createMusic(musicFolderName+"levelHard.mp3"));
        		levelThemes.get(i).setLooping(true);
        		levelThemes.get(i).setVolume(0.85f);
        	}
        	else
        		levelThemes.add(levelThemes.get(i-1));
        }
    }
    
}