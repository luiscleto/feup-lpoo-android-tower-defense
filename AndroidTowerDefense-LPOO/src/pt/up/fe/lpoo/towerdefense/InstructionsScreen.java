package pt.up.fe.lpoo.towerdefense;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Input.TouchEvent;

/**
 * Screen to be used while the game is in the Help screen.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class InstructionsScreen extends Screen {
	private Paint paint, paint2, buttonPaint;
	private int selectedDescriptionObject = -1;
	private TextParser defaultDescription = new TextParser(Messages.getString("ExtrasScreen.0") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.1") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.2") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.3") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.4") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.5") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.6") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.7") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.8") + //$NON-NLS-1$
			Messages.getString("ExtrasScreen.9") //$NON-NLS-1$
			);

	private ArrayList<DescriptionObject> objects;
	private static int objectSpacing = 70;
	private static int columnSpacing = 70;
	private static int startingX = 50;
	private static int startingY = 90;
	private static int descriptionTextX = 380;
	private static int descriptionTextY = 100;
	private static int descriptionLineSpacing = 30;

	/**
	 * Constructor for the InstructionsScreen. Sets the background music and initalizes the Paint objects for
	 *  drawing fonts and the DescriptionObjects
	 * @param game Game object corresponding to the screen
	 * @see DescriptionObject
	 */
	public InstructionsScreen(Game game) {
		super(game);
		
		if(Messages.getCurrentLang() == Messages.LANG_PIRATE)
			MusicPlayer.changeCurrentTheme(Assets.pirateTheme);
		else
			MusicPlayer.changeCurrentTheme(Assets.menuTheme);
		
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTypeface(Assets.tf);
		
		paint2 = new Paint();
		paint2.setTextSize(25);
		paint2.setTextAlign(Paint.Align.LEFT);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
		paint2.setTypeface(Assets.tf);
		
		buttonPaint = new Paint();
		buttonPaint.setTextSize(100);
		buttonPaint.setTextAlign(Paint.Align.LEFT);
		buttonPaint.setAntiAlias(true);
		buttonPaint.setColor(Color.WHITE);
		buttonPaint.setTypeface(Assets.tf);
		
		initializeDescriptionObjects();
	}

	private void initializeDescriptionObjects() {
		objects = new ArrayList<DescriptionObject>();
		
		String description = Messages.getString("ExtrasScreen.10") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.11") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.12") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.13") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.14") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.15"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.turretTowerLevel1, new TextParser(description), startingX, startingY));
		
		description = Messages.getString("ExtrasScreen.16") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.17") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.18") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.19") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.20") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.21") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.22") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.23") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.24"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.barricade, new TextParser(description), startingX, startingY+objectSpacing));
		
		description = Messages.getString("ExtrasScreen.25") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.26") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.27") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.28") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.29") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.30") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.31") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.32"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.freezeTower, new TextParser(description), startingX, startingY+2*objectSpacing));
		
		description = Messages.getString("ExtrasScreen.33") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.34") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.35") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.36") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.37") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.38") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.39") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.40"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.poisonTower, new TextParser(description), startingX+columnSpacing, startingY));
		
		description = Messages.getString("ExtrasScreen.41") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.42") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.43") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.44") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.45") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.46"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.tarPool, new TextParser(description), startingX+ columnSpacing, startingY+objectSpacing));
		
		
		description = Messages.getString("InstructionsScreen.0") + //$NON-NLS-1$
				Messages.getString("InstructionsScreen.1") + //$NON-NLS-1$
				Messages.getString("InstructionsScreen.2") + //$NON-NLS-1$
				Messages.getString("InstructionsScreen.3") + //$NON-NLS-1$
				Messages.getString("InstructionsScreen.4") + //$NON-NLS-1$
				Messages.getString("InstructionsScreen.5"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.boulderTower, new TextParser(description), startingX+columnSpacing, startingY+2*objectSpacing));
		 
		
		description = Messages.getString("ExtrasScreen.47") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.48") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.49") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.50") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.51") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.52"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.slimeMovement.getImage(0), new TextParser(description), startingX+ 3*columnSpacing, startingY));
		
		description = Messages.getString("ExtrasScreen.53") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.54") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.55") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.56") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.57") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.58"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.skullRightMovement.getImage(0), new TextParser(description), startingX+ 3*columnSpacing, startingY+objectSpacing));
		
		description = Messages.getString("ExtrasScreen.59") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.60") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.61") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.62") + //$NON-NLS-1$
				Messages.getString("ExtrasScreen.63"); //$NON-NLS-1$
		
		objects.add(new DescriptionObject(Assets.golemRightMovement.getImage(0), new TextParser(description), startingX+ 3*columnSpacing, startingY+2*objectSpacing));
	}
	
	/**
	 * Updates the music player and checks if the user has selected any description object or pressed the menu button
	 */
	@Override
	public void update(float deltaTime) {
		MusicPlayer.update(deltaTime);
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				if (inBounds(event, 60, 340, 280, 90)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					goToMenu();
				}
				else
					checkDescriptionObjects(event);
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
	 * Paints the current screen's objects and the description of the currently selected object
	 */
	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.menu, 0, 0);
		
		for(int i = 0; i < objects.size(); i++)
			objects.get(i).draw(g);
		
		if(selectedDescriptionObject >= 0 && selectedDescriptionObject < objects.size())
			objects.get(selectedDescriptionObject).getText().drawText(paint2,descriptionTextX, descriptionTextY, descriptionLineSpacing, g);
		else
			defaultDescription.drawText(paint2,descriptionTextX, descriptionTextY, descriptionLineSpacing, g);
		
		g.drawString(Messages.getString("ExtrasScreen.64"), 210, 50, paint); //$NON-NLS-1$
		g.drawString(Messages.getString("ExtrasScreen.65"), 60, 420, buttonPaint); //$NON-NLS-1$
		g.drawString(Messages.getString("ExtrasScreen.66"), 60, 50, paint); //$NON-NLS-1$
	}
	
	private void nullify() {
		// Set all variables to null. You will be recreating them in the
		// constructor.
		buttonPaint = null;
		paint = null;

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
	
	private void checkDescriptionObjects(TouchEvent e)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			if(inBounds(e, objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight())) 
			{
				selectedDescriptionObject = i;
				return;
			}
		}
		
		selectedDescriptionObject = -1;
	}
}
