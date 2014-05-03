package pt.up.fe.lpoo.towerdefense;

import java.util.List;

import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Input.TouchEvent;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Screen to be used while the game is in the Level Selection Menu
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class LevelSelectionScreen extends Screen {
	private Paint buttonPaint, titlePaint, levelPaint, lockedLevelPaint;
	private boolean hasNextPage;
	private int curPage;
	private final int displayPerPage = 4;
	private final int distanceBetweenLevelIcon = 50;
	private final int levelHitboxesYStart = 210;
	private final int levelHitboxesXStart = 390;
	private final int levelHitboxesWidth = 200;
	private static final int levelStringYSpacing = 40;
	private static final int levelStringXSpacing = 10;
	private int selectedLevel;
	private boolean confirmingSelection;
	
	/**
	 * Constructor for the LevelSelectionScreen. Uses the superclass constructor.
	 * 
	 * Sets the background music and initializes the Paint objects for drawing fonts
	 * @param game Game object corresponding to the screen
	 */
	public LevelSelectionScreen(Game game) {
		super(game);
		
		if(Messages.getCurrentLang() == Messages.LANG_PIRATE)
			MusicPlayer.changeCurrentTheme(Assets.pirateTheme);
		else
			MusicPlayer.changeCurrentTheme(Assets.menuTheme);
		
		curPage = 0;
		confirmingSelection = false;
		buttonPaint = new Paint();
		buttonPaint.setTextSize(100);
		buttonPaint.setTextAlign(Paint.Align.LEFT);
		buttonPaint.setAntiAlias(true);
		buttonPaint.setColor(Color.WHITE);
		buttonPaint.setTypeface(Assets.tf);

		titlePaint = new Paint();
		titlePaint.setTextSize(110);
		titlePaint.setTextAlign(Paint.Align.CENTER);
		titlePaint.setAntiAlias(true);
		titlePaint.setColor(Color.YELLOW);
		titlePaint.setTypeface(Assets.tf);

		levelPaint = new Paint();
		levelPaint.setTextSize(50);
		levelPaint.setTextAlign(Paint.Align.LEFT);
		levelPaint.setAntiAlias(true);
		levelPaint.setColor(Color.WHITE);
		levelPaint.setTypeface(Assets.tf);
		
		lockedLevelPaint = new Paint();
		lockedLevelPaint.setTextSize(50);
		lockedLevelPaint.setTextAlign(Paint.Align.LEFT);
		lockedLevelPaint.setAntiAlias(true);
		lockedLevelPaint.setColor(Color.DKGRAY);
		lockedLevelPaint.setTypeface(Assets.tf);

		if(Assets.levelScores.size() > displayPerPage)
			hasNextPage = true;
		else
			hasNextPage = false;
	}

	/**
	 * Checks for user commands through buttons and executes them
	 * 
	 * @param deltaTime elapsed time since the last update
	 */
	@Override
	public void update(float deltaTime) {
		MusicPlayer.update(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if(confirmingSelection)
			updateConfirm(touchEvents);
		else
			updateSelection(touchEvents);

	}

	private void updateConfirm(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 100, 350, 210, 100)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					game.setScreen(new LevelScreen(game, selectedLevel));
				}
				else if (inBounds(event, 440, 350, 320, 100)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					confirmingSelection = false;
				}

			}
		}
	}

	private void updateSelection(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {

				if (inBounds(event, 60, 340, 280, 90)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					goToMenu();
				}
				if (inBounds(event, levelHitboxesXStart, levelHitboxesYStart, levelHitboxesWidth, displayPerPage*distanceBetweenLevelIcon)) {
					selectedLevel = getSelectedLevel(event);
					boolean unlockedLevel = false;
					if(selectedLevel == 0)
						unlockedLevel = true;
					else if(Assets.levelScores.get(selectedLevel-1) > 0)
						unlockedLevel = true;
					
					if(unlockedLevel && selectedLevel < Assets.levelScores.size() && selectedLevel >= 0){
						confirmingSelection = true;
					}
					
				}
				else if(hasNextPage){
					if (inBounds(event, 690,290,50,50))
						forwardPage();
				}
				if(curPage > 0){
					if (inBounds(event, 640,290,50,50))
						previousPage();
				}

			}
		}
	}
	
	private int getSelectedLevel(TouchEvent event) {
		int i;
		for(i=0; i < displayPerPage; i++){
			if(inBounds(event, levelHitboxesXStart, levelHitboxesYStart+i*distanceBetweenLevelIcon, levelHitboxesWidth, distanceBetweenLevelIcon))
				return curPage*displayPerPage+i;
		}
		return Assets.levelScores.size();
	}

	private void forwardPage(){
		int numMaxPage;
		if(Assets.levelScores.size()%displayPerPage == 0)
			numMaxPage = Assets.levelScores.size()/displayPerPage-1;
		else
			numMaxPage = Assets.levelScores.size()/displayPerPage;
		curPage++;
		if(curPage < numMaxPage)
			hasNextPage = true;
		else
			hasNextPage = false;
	}
	private void previousPage(){
		curPage--;
		hasNextPage = true;
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	/**
	 * Paints the contents of the Screen for the user
	 */
	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawImage(Assets.menu, 0, 0);
		g.drawString(Messages.getString("LevelSelectionScreen.0"), 400, 170, titlePaint); //$NON-NLS-1$
		g.drawString(Messages.getString("LevelSelectionScreen.1"), 60, 420, buttonPaint); //$NON-NLS-1$

		g.drawImage(Assets.menu_box, 370, 190);

		//g.drawRect(390, 210, 200, 50, Color.YELLOW); //Values to determine hitbox for the first displayed level
		for(int i = curPage*displayPerPage, c=0; i < Assets.levelScores.size() && c < displayPerPage; c++, i++){
			if(i==0)
				g.drawString(Messages.getString("LevelSelectionScreen.2")+Integer.toString(i), levelHitboxesXStart+levelStringXSpacing,  //$NON-NLS-1$
						levelHitboxesYStart+levelStringYSpacing+c*distanceBetweenLevelIcon, levelPaint);
			else if(Assets.levelScores.get(i-1) > 0) //Completed last level
				g.drawString(Messages.getString("LevelSelectionScreen.3")+Integer.toString(i), levelHitboxesXStart+levelStringXSpacing, //$NON-NLS-1$
						levelHitboxesYStart+levelStringYSpacing+c*distanceBetweenLevelIcon, levelPaint);
			else
				g.drawString(Messages.getString("LevelSelectionScreen.4")+Integer.toString(i), levelHitboxesXStart+levelStringXSpacing,  //$NON-NLS-1$
						levelHitboxesYStart+levelStringYSpacing+c*distanceBetweenLevelIcon, lockedLevelPaint);
		}
		
		if(hasNextPage){
			//g.drawRect(690, 290, 50, 50, Color.YELLOW); //Values to determine next button hitbox
			g.drawString(">", 700, 340, buttonPaint); //$NON-NLS-1$
		}
		if(curPage > 0){
			//g.drawRect(640, 290, 50, 50, Color.YELLOW); //Values to determine back button hitbox
			g.drawString("<", 650, 340, buttonPaint); //$NON-NLS-1$
		}
		//g.drawRect(370, 190, 400, 230, Color.DKGRAY);
		
		if(confirmingSelection)
			drawConfirmingLevelUI();
	}
	
	private void drawConfirmingLevelUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(200, 0, 0, 0);
		g.drawString(Messages.getString("LevelSelectionScreen.7")+Integer.toString(selectedLevel),  //$NON-NLS-1$
				400, 165, titlePaint);
		g.drawString(Messages.getString("LevelSelectionScreen.8")+Assets.levelScores.get(selectedLevel).toString(), 400, 265, titlePaint); //$NON-NLS-1$
		//g.drawRect(100, 350, 210, 100, Color.WHITE); //Values to determine Play hitbox
		g.drawString(Messages.getString("LevelSelectionScreen.9"), 200, 450, titlePaint); //$NON-NLS-1$
		//g.drawRect(440, 350, 320, 100, Color.WHITE); //Values to determine Cancel hitbox
		g.drawString(Messages.getString("LevelSelectionScreen.10"), 600, 450, titlePaint); //$NON-NLS-1$
	}
	
	private void nullify() {
		// Set all variables to null. You will be recreating them in the
		// constructor.
		buttonPaint = null;
		titlePaint = null;
		levelPaint = null;
		lockedLevelPaint = null;

		// Call garbage collector to clean up memory.
		System.gc();

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
	 * Returns to the main menu
	 */
	@Override
	public void backButton() {
		goToMenu();
	}

	private void goToMenu() {
		game.setScreen(new MainMenuScreen(game));
	}

}