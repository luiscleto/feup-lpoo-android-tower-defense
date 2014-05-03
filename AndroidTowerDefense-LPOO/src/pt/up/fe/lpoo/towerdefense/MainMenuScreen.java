package pt.up.fe.lpoo.towerdefense;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Input.TouchEvent;

/**
 * Screen to be used while the game is in the Main Menu
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class MainMenuScreen extends Screen {
	private Paint buttonText, titleText, smallWhiteText;
	/** name of the file with the background music for the menu */
	public static final String MENU_THEME = "menutheme.mp3";
	
	private final int enIconXPos = 540;
	private final int enIconYPos = 0;
	private final int ptIconXPos = 620;
	private final int ptIconYPos = 0;
	private final int pirateIconXPos = 700;
	private final int pirateIconYPos = 0;
	
	/**
	 * Constructor for the MainMenuScreen. Uses the superclass constructor.
	 * 
	 * Sets the background music and initializes the Paint objects for drawing fonts
	 * @param game Game object corresponding to the screen
	 */
	public MainMenuScreen(Game game) {
		super(game);
		
		if(Messages.getCurrentLang() == Messages.LANG_PIRATE)
			MusicPlayer.changeCurrentTheme(Assets.pirateTheme);
		else
			MusicPlayer.changeCurrentTheme(Assets.menuTheme);
		
		buttonText = new Paint();
		buttonText.setTextSize(100);
		buttonText.setTextAlign(Paint.Align.LEFT);
		buttonText.setAntiAlias(true);
		buttonText.setColor(Color.WHITE);
		buttonText.setTypeface(Assets.tf);
		
		titleText = new Paint();
		titleText.setTextSize(110);
		titleText.setTextAlign(Paint.Align.CENTER);
		titleText.setAntiAlias(true);
		titleText.setColor(Color.YELLOW);
		titleText.setTypeface(Assets.tf);
		
		smallWhiteText = new Paint();
		smallWhiteText.setTextSize(30);
		smallWhiteText.setTextAlign(Paint.Align.LEFT);
		smallWhiteText.setAntiAlias(true);
		smallWhiteText.setColor(Color.WHITE);
		smallWhiteText.setTypeface(Assets.tf);
		
	}

	/**
	 * Checks if the user has pressed any of the displayed buttons and updates the music player
	 * 
	 * @param deltaTime elapsed time since the last update
	 */
	@Override
	public void update(float deltaTime) {
		MusicPlayer.update(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {

				if (inBounds(event, 60, 340, 240, 90)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					game.setScreen(new LevelSelectionScreen(game));
				}
				else if (inBounds(event, 500, 340, 240, 90))
				{
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					game.setScreen(new InstructionsScreen(game));
				}
				else if (inBounds(event, enIconXPos, enIconYPos, Assets.enIcon.getWidth(),Assets.enIcon.getHeight()))
				{
					Messages.setLang(Messages.LANG_EN);
					MusicPlayer.changeCurrentTheme(Assets.menuTheme);
					TowerDefenseGame.saveConfig(game.getFileIO());
				}
				else if (inBounds(event, ptIconXPos, ptIconYPos, Assets.ptIcon.getWidth(),Assets.ptIcon.getHeight()))
				{
					Messages.setLang(Messages.LANG_PT);
					MusicPlayer.changeCurrentTheme(Assets.menuTheme);
					TowerDefenseGame.saveConfig(game.getFileIO());
				}
				else if (inBounds(event, pirateIconXPos, pirateIconYPos,
						Assets.pirateIcon.getWidth(),Assets.pirateIcon.getHeight()))
				{
					Messages.setLang(Messages.LANG_PIRATE);
					MusicPlayer.changeCurrentTheme(Assets.pirateTheme);
					TowerDefenseGame.saveConfig(game.getFileIO());
				}
			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	/**
	 * Draws all of the screen's elements on using the game's Graphics object
	 * 
	 * @param deltaTime elapsed time since the last update
	 */
	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.menu, 0, 0);
		g.drawString(Messages.getString("MainMenuScreen.0"), 400, 170, titleText); //$NON-NLS-1$
		g.drawString(Messages.getString("MainMenuScreen.4"), 480, 40, smallWhiteText);
		//g.drawRect(60, 340, 240, 90, Color.YELLOW); //values for the inbound detection
		g.drawString(Messages.getString("MainMenuScreen.2"), 60, 420, buttonText); //$NON-NLS-1$
		//g.drawRect(500, 340, 240, 90, Color.YELLOW); //values for the inbound detection
		g.drawString(Messages.getString("MainMenuScreen.3"), 500, 420, buttonText); //$NON-NLS-1$
		
		g.drawImage(Assets.enIcon, enIconXPos, enIconYPos);
		g.drawImage(Assets.ptIcon, ptIconXPos, ptIconYPos);
		g.drawImage(Assets.pirateIcon, pirateIconXPos, pirateIconYPos);
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

	/**
	 * Exits the application
	 */
	@Override
	public void backButton() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}