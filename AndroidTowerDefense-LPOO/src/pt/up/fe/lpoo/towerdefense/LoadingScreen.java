package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import android.graphics.Typeface;
import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Graphics.ImageFormat;

/**
 * Screen to be used while the game is loading the main assets
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {

		super(game);
	}
	private boolean isFirstUpdate = true;
	
	/**
	 * Ignores the first update so that the splash loading screen is drawn onto the screen. Loads the game's
	 *  assets
	 * 
	 *  @param deltaTime elapsed time since the last update
	 *  @see Assets
	 */
	@Override
	public void update(float deltaTime) {
		if(isFirstUpdate){
			isFirstUpdate = false;
			return;
		}
		Graphics g = game.getGraphics();

		game.getFileIO().setAppPath(TowerDefenseGame.APP_PATH_NAME);

		Assets.menu = g.newImage(Assets.imagesFolderName+"menu.png", ImageFormat.RGB565);
		Assets.menu_box = g.newImage(Assets.imagesFolderName+"menu_box_bg.png", ImageFormat.ARGB4444);
		
		Assets.infoBarBG = g.newImage(Assets.imagesFolderName+"infoBarBG.png", ImageFormat.RGB565);
		Assets.startIcon = g.newImage(Assets.imagesFolderName+"startIcon.png", ImageFormat.RGB565);
		Assets.lifeIcon = g.newImage(Assets.imagesFolderName+"heartIcon.png", ImageFormat.ARGB4444);
		Assets.coinIcon = g.newImage(Assets.imagesFolderName+"coinIcon.png", ImageFormat.ARGB4444);
		
		Assets.buildingOptionsBG = g.newImage(Assets.imagesFolderName+"buildingOptionsBG.png", ImageFormat.ARGB4444);
		Assets.cancelIcon = g.newImage(Assets.imagesFolderName+"cancelIcon.png", ImageFormat.ARGB4444);
		Assets.removeIcon = g.newImage(Assets.imagesFolderName+"removeIcon.png", ImageFormat.ARGB4444);
		Assets.repairIcon = g.newImage(Assets.imagesFolderName+"repairIcon.png", ImageFormat.ARGB4444);
		Assets.upgradeIcon = g.newImage(Assets.imagesFolderName+"upgradeIcon.png", ImageFormat.ARGB4444);
		
		Assets.ptIcon = g.newImage(Assets.imagesFolderName+"PTFlagIcon.png", ImageFormat.ARGB4444);
		Assets.enIcon = g.newImage(Assets.imagesFolderName+"UKFlagIcon.png", ImageFormat.ARGB4444);
		Assets.pirateIcon = g.newImage(Assets.imagesFolderName+"PirateTalkFlagIcon.png", ImageFormat.ARGB4444);
		
		Assets.barricade = g.newImage(Assets.imagesFolderName+"barricade.png", ImageFormat.ARGB4444);
		
		Assets.frozenEffect = g.newImage(Assets.imagesFolderName+"frozenEffect.png", ImageFormat.ARGB4444);
		Assets.freezeTower = g.newImage(Assets.imagesFolderName+"freezeTower.png", ImageFormat.ARGB4444);
		
		Assets.poisonEffect = g.newImage(Assets.imagesFolderName+"poisonEffect.png", ImageFormat.ARGB4444);
		Assets.poisonTower = g.newImage(Assets.imagesFolderName+"poisonTower.png", ImageFormat.ARGB4444);
		
		Assets.slowEffect = g.newImage(Assets.imagesFolderName+"slowEffect.png", ImageFormat.ARGB4444);
		Assets.tarPool = g.newImage(Assets.imagesFolderName+"tarPool.png", ImageFormat.ARGB4444);
		
		Assets.boulderTower = g.newImage(Assets.imagesFolderName+"boulderTower.png", ImageFormat.ARGB4444);
		Assets.boulderProjectile = g.newImage(Assets.imagesFolderName+"boulderProjectile.png", ImageFormat.ARGB4444);
		
		Assets.turretTowerLevel1 = g.newImage(Assets.imagesFolderName+"turretTowerLevel1.png", ImageFormat.ARGB4444);
		Assets.turretTowerLevel2 = g.newImage(Assets.imagesFolderName+"turretTowerLevel2.png", ImageFormat.ARGB4444);
		Assets.turretProjectile = g.newImage(Assets.imagesFolderName+"turretProjectile.png", ImageFormat.ARGB4444);
		
		Assets.tileEmptyTower = g.newImage(Assets.imagesFolderName+"towerTerrain.png", ImageFormat.RGB565);
		Assets.tilePath = g.newImage(Assets.imagesFolderName+"pathTerrain.png", ImageFormat.RGB565);
		Assets.tileGoal = g.newImage(Assets.imagesFolderName+"goalTerrain.png", ImageFormat.RGB565);
		Assets.tileEntrance = g.newImage(Assets.imagesFolderName+"entranceTerrain.png", ImageFormat.RGB565);
		Assets.selectionOverlay = g.newImage(Assets.imagesFolderName+"selectionOverlay.png", ImageFormat.ARGB4444);
		
		Assets.slimeMovement = new Animation();
		Assets.slimeMovement.addFrame(g.newImage(Assets.imagesFolderName+"slime0.png", ImageFormat.ARGB4444),200);
		Assets.slimeMovement.addFrame(g.newImage(Assets.imagesFolderName+"slime1.png", ImageFormat.ARGB4444),200);
		Assets.slimeMovement.addFrame(g.newImage(Assets.imagesFolderName+"slime2.png", ImageFormat.ARGB4444),200);
		
		Assets.skullDownMovement = new Animation();
		Assets.skullDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullDown0.png", ImageFormat.ARGB4444),200);
		Assets.skullDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullDown1.png", ImageFormat.ARGB4444),200);
		Assets.skullDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullDown2.png", ImageFormat.ARGB4444),200);
		Assets.skullLeftMovement = new Animation();
		Assets.skullLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullLeft0.png", ImageFormat.ARGB4444),200);
		Assets.skullLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullLeft1.png", ImageFormat.ARGB4444),200);
		Assets.skullLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullLeft2.png", ImageFormat.ARGB4444),200);
		Assets.skullRightMovement = new Animation();
		Assets.skullRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullRight0.png", ImageFormat.ARGB4444),200);
		Assets.skullRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullRight1.png", ImageFormat.ARGB4444),200);
		Assets.skullRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullRight2.png", ImageFormat.ARGB4444),200);
		Assets.skullUpMovement = new Animation();
		Assets.skullUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullUp0.png", ImageFormat.ARGB4444),200);
		Assets.skullUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullUp1.png", ImageFormat.ARGB4444),200);
		Assets.skullUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"skullUp2.png", ImageFormat.ARGB4444),200);
		
		Assets.golemDownMovement = new Animation();
		Assets.golemDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemDown0.png", ImageFormat.ARGB4444),200);
		Assets.golemDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemDown1.png", ImageFormat.ARGB4444),200);
		Assets.golemDownMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemDown2.png", ImageFormat.ARGB4444),200);
		Assets.golemLeftMovement = new Animation();
		Assets.golemLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemLeft0.png", ImageFormat.ARGB4444),200);
		Assets.golemLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemLeft1.png", ImageFormat.ARGB4444),200);
		Assets.golemLeftMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemLeft2.png", ImageFormat.ARGB4444),200);
		Assets.golemRightMovement = new Animation();
		Assets.golemRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemRight0.png", ImageFormat.ARGB4444),200);
		Assets.golemRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemRight1.png", ImageFormat.ARGB4444),200);
		Assets.golemRightMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemRight2.png", ImageFormat.ARGB4444),200);
		Assets.golemUpMovement = new Animation();
		Assets.golemUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemUp0.png", ImageFormat.ARGB4444),200);
		Assets.golemUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemUp1.png", ImageFormat.ARGB4444),200);
		Assets.golemUpMovement.addFrame(g.newImage(Assets.imagesFolderName+"golemUp2.png", ImageFormat.ARGB4444),200);
		
		Assets.tf = Typeface.createFromAsset(g.getAssets(), Assets.fontsFolderName+"DARK11.ttf");

		Assets.levelScores = new ArrayList<Integer>();
		TowerDefenseGame.loadLevelScores(game.getFileIO());
		TowerDefenseGame.loadConfig(game.getFileIO());

		Assets.buttonClick = game.getAudio().createSound(Assets.soundsFolderName+"buttonpress.mp3");
		game.setScreen(new MainMenuScreen(game));

	}

	/**
	 * Draws the splash loading screen
	 * 
	 * @param deltaTime elapsed time since the last update
	 */
	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}