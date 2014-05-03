package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Image;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Input.TouchEvent;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Screen to be used while the user is playing one of the game's levels.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class LevelScreen extends Screen {
	private static final int TRANSLUCID_MASK = 0x1fffffff;

	private enum GameState {
		DisplayingEnemyUnits, Ready, Running, Paused, GameOver, Victory
	}
	private boolean containsSkulls = false;
	private boolean containsSlimes = false;
	private boolean containsGolems = false;
	private Image[] monstersInLevelSprites;
	private GameState lastState = GameState.DisplayingEnemyUnits;
	private GameState state = GameState.DisplayingEnemyUnits;

	// Variable Setup

	/** height occupied by the top UI bar */
	public static final int INFO_BAR_HEIGHT = 30;
	private static final int BUILDING_OPTIONS_Y_POS = 40;
	/** Width of the current map being played */
	public static int MAP_WIDTH;
	/** Height of the current map being played */
	public static int MAP_HEIGHT;
	private ArrayList<ArrayList<Tile>> tileGrid = new ArrayList<ArrayList<Tile>>();

	/** Game elements for the current level being played */
	public static GameElements gameElems;

	private Paint paint, paint2, smallYellowText, smallWhiteText, smallCenteredText;
	private int chosenLevel;
	private final int lifeIconXStart = 30;
	private final int coinIconXStart = 300;
	private final int scoreXStart = 500;
	private final int startIconYPos = 5;
	private final int startIconXPos = 668;
	private int curX = 0;
	private int curY = 0;
	private TouchEvent lastDragEv = null;
	private TouchEvent firstDownEv = null;

	private PathCluster paths;
	private ArrayList<Wave> enemyWaves;
	private boolean hasMoved = false;

	private Tile selectedTile = null;
	private BuildingOption[] selectedTileOptions = null;

	/**
	 * Constructor for the LevelScreen.
	 * 
	 * Sets the game variables, theme music and loads the game map for the given level
	 * @param game Game object corresponding to the screen
	 * @param levelIndex level chosen by the user
	 */
	public LevelScreen(Game game, int levelIndex) {
		super(game);
		chosenLevel = levelIndex;
		gameElems = new GameElements(chosenLevel);

		MusicPlayer.changeCurrentTheme(Assets.buildUpTheme);

		paths = new PathCluster();
		enemyWaves = new ArrayList<Wave>();
		loadMap();
		MAP_HEIGHT = tileGrid.size()*Tile.TILE_SIDE;
		MAP_WIDTH = tileGrid.get(0).size()*Tile.TILE_SIDE;

		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(40);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTypeface(Assets.tf);

		paint2 = new Paint();
		paint2.setTextSize(100);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
		paint2.setTypeface(Assets.tf);

		smallYellowText = new Paint();
		smallYellowText.setTextSize(24);
		smallYellowText.setTextAlign(Paint.Align.LEFT);
		smallYellowText.setAntiAlias(true);
		smallYellowText.setColor(Color.YELLOW);

		smallWhiteText = new Paint();
		smallWhiteText.setTextSize(24);
		smallWhiteText.setTextAlign(Paint.Align.LEFT);
		smallWhiteText.setAntiAlias(true);
		smallWhiteText.setColor(Color.WHITE);

		smallCenteredText = new Paint();
		smallCenteredText.setTextSize(24);
		smallCenteredText.setTextAlign(Paint.Align.CENTER);
		smallCenteredText.setAntiAlias(true);
		smallCenteredText.setColor(Color.WHITE);

	}

	private void loadMap() {
		game.loadLevel(chosenLevel);
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		
		Scanner scanner = new Scanner(TowerDefenseGame.level);
		loadLevelMap(lines, width, scanner);
		loadLevelEnemies(scanner);
		checkEnemyTypes();

	}

	private void checkEnemyTypes() {
		int numTypeMonsters = 0;
		if(this.containsSlimes)
			numTypeMonsters++;
		if(this.containsSkulls)
			numTypeMonsters++;
		if(this.containsGolems)
			numTypeMonsters++;
		monstersInLevelSprites = new Image[numTypeMonsters];
		int curArrayPos = 0;
		if(this.containsSlimes)
			monstersInLevelSprites[curArrayPos++] = Assets.slimeMovement.getImage(0);
		if(this.containsSkulls)
			monstersInLevelSprites[curArrayPos++] = Assets.skullDownMovement.getImage(0);
		if(this.containsGolems)
			monstersInLevelSprites[curArrayPos++] = Assets.golemDownMovement.getImage(0);
	}

	private void loadLevelEnemies(Scanner scanner) {
		Wave curWave = null;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line == null) {
				break;
			}
			if (line.startsWith("!") || line.length() == 0) { //$NON-NLS-1$
				continue;
			}
			else if(line.startsWith("WAVE")){ //$NON-NLS-1$
				if(curWave != null)
					enemyWaves.add(curWave);
				curWave = new Wave();
			}
			else if(line.startsWith("STACK")){ //$NON-NLS-1$
				LinkedList<Enemy> newStack = new LinkedList<Enemy>();
				line = scanner.nextLine();
				Float[] weights;
				int mobType;
				if(line.equals("slime")){ //$NON-NLS-1$
					weights = Slime.tileWeights;
					mobType = Slime.enemyType;
				}
				else if (line.equals("skull")){ //$NON-NLS-1$
					weights = Skull.tileWeights;
					mobType = Skull.enemyType;
				}
				else{
					weights = Golem.tileWeights;
					mobType = Golem.enemyType;
				}
				line = scanner.nextLine();
				Scanner lineParser = new Scanner(line);
				lineParser.useDelimiter(" "); //$NON-NLS-1$
				int numUnits = lineParser.nextInt();int origX = lineParser.nextInt();int origY = lineParser.nextInt();
				int destX = lineParser.nextInt();int destY = lineParser.nextInt();int stackStartTime = lineParser.nextInt();
				int unitDelay = lineParser.nextInt();

				ArrayList<Tile> stackPath = paths.getPath(origX, origY, destX, destY, mobType);
				if(stackPath == null){
					paths.addPath(Enemy.getPathToPoint(origX, origY, destX, destY, tileGrid, weights), 
							origX, origY, destX, destY, mobType);
					stackPath = paths.getPath(origX, origY, destX, destY, mobType);
				}

				for(int i=0; i < numUnits; i++){
					switch(mobType){
					case Slime.enemyType:
						newStack.add(new Slime(stackPath));
						containsSlimes = true;
						break;
					case Skull.enemyType:
						newStack.add(new Skull(stackPath));
						containsSkulls = true;
						break;
					case Golem.enemyType:
						newStack.add(new Golem(stackPath));
						containsGolems = true;
						break;
					default:
						break;
					}
				}
				curWave.addEnemyStack(newStack, stackStartTime, unitDelay);
			}
		}
		if(curWave != null)
			enemyWaves.add(curWave);
	}

	private void loadLevelMap(ArrayList<String> lines, int width, Scanner scanner) {
		int height;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line == null || line.equals("DONE")) { //$NON-NLS-1$
				break;
			}
			else if (!line.startsWith("!") && line.length()>0) { //$NON-NLS-1$
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size();

		for (int j = 0; j < height; j++) {
			String line = lines.get(j);
			tileGrid.add(new ArrayList<Tile>());
			for (int i = 0; i < width; i++) {

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, ch);
					tileGrid.get(j).add(t);
				}

			}
		}
	}

	/**
	 * Updates the music player and the game deppending on current game state.
	 * 
	 * Displaying enemy units: starts the game when the user taps the screen
	 * Ready: allows the user to build defenses or start the enemy waves, enemies not yet active
	 * Running: waves start and enemies and defenses are updated along with allowing the user to build defenses
	 * Paused: allows the user to continue the game or go back to the menu
	 * GameOver: returns to the menu when the user taps the screen
	 * Victory: allows the user to return to the menu or to proceed to the next level if there is one
	 * 
	 * @param deltaTime time elapsed since the last update
	 */
	@Override
	public void update(float deltaTime) {
		MusicPlayer.update(deltaTime);
		if(gameElems == null)
			return;
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		updateScroll(touchEvents);

		if (state == GameState.DisplayingEnemyUnits)
			updateDisplayingEnemyUnits(touchEvents);
		else if (state == GameState.Ready)
			updateReady(touchEvents);
		else if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		else if (state == GameState.Paused)
			updatePaused(touchEvents);
		else if (state == GameState.GameOver)
			updateGameOver(touchEvents);
		else if (state == GameState.Victory)
			updateVictory(touchEvents);
	}

	private void updateDisplayingEnemyUnits(List<TouchEvent> touchEvents) {
		if(touchEvents.size() > 0){
			state = GameState.Ready;
		}
	}

	private void updateEnemies(float deltaTime) {
		for(int i=0; i < gameElems.enemies.size();i++){
			gameElems.enemies.get(i).update(deltaTime);
			if(gameElems.enemies.get(i).toRemove){
				gameElems.enemies.remove(i);
				i--;
			}
		}
	}

	private void updateWaves(float deltaTime) {
		for(int i=0; i < enemyWaves.size();i++){
			enemyWaves.get(i).update(deltaTime);
			if(enemyWaves.get(i).finished){
				enemyWaves.remove(i);
				if(i < enemyWaves.size())
					enemyWaves.get(i).started = true;
				i--;
			}
		}
	}

	private void updateScroll(List<TouchEvent> touchEvents) {
		if(touchEvents.size() == 0)
			return;

		TouchEvent event = touchEvents.get(0);
		if(lastDragEv == null){
			lastDragEv = event;
			return;
		}
		if(event.type == TouchEvent.TOUCH_DRAGGED && lastDragEv.type == TouchEvent.TOUCH_DRAGGED){
			scrollScreen(event.x-lastDragEv.x, event.y-lastDragEv.y);
		}
		lastDragEv = event;

	}

	private void scrollScreen(int xScroll, int yScroll) {
		curX -= xScroll;
		curY -= yScroll;
		if(curX < 0)
			curX = 0;
		else if(curX > tileGrid.get(0).size()*Tile.TILE_SIDE-800)
			curX = tileGrid.get(0).size()*Tile.TILE_SIDE-800;
		if(curY < 0)
			curY = 0;
		else if(curY > tileGrid.size()*Tile.TILE_SIDE-480+LevelScreen.INFO_BAR_HEIGHT)
			curY = tileGrid.size()*Tile.TILE_SIDE-480+LevelScreen.INFO_BAR_HEIGHT;
	}

	private void updateReady(List<TouchEvent> touchEvents) {
		updateDefenses(0);
		for(int i=0; i < touchEvents.size(); i++){
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN && firstDownEv == null) {
				firstDownEv = event;
			}
			else if(event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED){
				if(firstDownEv != null)
					if(event.x != firstDownEv.x || event.y != firstDownEv.y)
						hasMoved = true;
			}
			else if (event.type == TouchEvent.TOUCH_UP && firstDownEv != null) {

				if(!hasMoved &&  inBounds(event, startIconXPos, startIconYPos,
						Assets.startIcon.getWidth(), Assets.startIcon.getHeight())){

					if(event.x == firstDownEv.x && event.y == firstDownEv.y){

						if(enemyWaves.size() > 0)
							enemyWaves.get(0).started = true;
						state = GameState.Running;
						MusicPlayer.changeCurrentTheme(Assets.levelThemes.get(chosenLevel));
					}
				}
				else if(!hasMoved &&  inBounds(event, 0, INFO_BAR_HEIGHT, 800, 480)){

					if(event.x == firstDownEv.x && event.y == firstDownEv.y){
						if(!selectedABuildingOption(event))
							selectedTile = getSelectedTile(curX+event.x, curY+event.y);
					}
				}
				firstDownEv = null;
				hasMoved = false;
			}
		}
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		updateWaves(deltaTime);
		updateEnemies(deltaTime);
		updateDefenses(deltaTime);
		updateProjectiles(deltaTime);
		if(selectedTile != null){
			selectedTileOptions = selectedTile.getBuildingOptions();
			BuildingOption.selectedDefObj = selectedTile.getDefenseObject();
		}

		for(int i=0; i < touchEvents.size(); i++){
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN && firstDownEv == null) {
				firstDownEv = event;
			}
			else if(event.type == TouchEvent.TOUCH_DOWN || event.type == TouchEvent.TOUCH_DRAGGED){
				if(firstDownEv != null)
					if(event.x != firstDownEv.x || event.y != firstDownEv.y)
						hasMoved = true;
			}
			else if (event.type == TouchEvent.TOUCH_UP && firstDownEv != null) {

				if(!hasMoved &&  inBounds(event, 0, INFO_BAR_HEIGHT, 800, 480)){

					if(event.x == firstDownEv.x && event.y == firstDownEv.y){
						if(!selectedABuildingOption(event))
							selectedTile = getSelectedTile(curX+event.x, curY+event.y);
					}
				}
				firstDownEv = null;
				hasMoved = false;
			}
		}

		if (gameElems.livesLeft <= 0) {
			state = GameState.GameOver;
			MusicPlayer.changeCurrentTheme(Assets.gameOverTheme);
		}
		else if(enemyWaves.size() == 0 && gameElems.enemies.size() == 0){
			state = GameState.Victory;
			MusicPlayer.changeCurrentTheme(Assets.victoryTheme);
			gameElems.curScore += gameElems.livesLeft*100 + gameElems.currentBalance;
			if(gameElems.curScore > Assets.levelScores.get(chosenLevel)){
				Assets.levelScores.set(chosenLevel, gameElems.curScore);
				TowerDefenseGame.saveLevelScores(game.getFileIO());
			}
		}
	}

	private boolean selectedABuildingOption(TouchEvent event) {
		if(selectedTile == null)
			return false;

		int xStart = 400-selectedTileOptions.length*BuildingOption.OPTION_ICON_WIDTH/2;
		int panelWidth = selectedTileOptions.length*BuildingOption.OPTION_ICON_WIDTH;

		if(inBounds(event, xStart, BUILDING_OPTIONS_Y_POS, panelWidth, BuildingOption.OPTION_ICON_WIDTH)){
			int optionIndex = (event.x-xStart)/BuildingOption.OPTION_ICON_WIDTH;
			executeBuildOption(selectedTileOptions[optionIndex]);
			selectedTile = null;
			selectedTileOptions = null;
			return true;
		}
		return false;
	}

	private void executeBuildOption(BuildingOption buildingOption) {
		//if(buildingOption.getOptionType() == BuildingOption.OptionType.cancel);
		if(buildingOption.getOptionType() == BuildingOption.OptionType.upgrade){
			selectedTile.getDefenseObject().upgrade();
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.repair){
			selectedTile.getDefenseObject().repair();
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.destroy){
			gameElems.currentBalance+=selectedTile.getDefenseObject().getCost()/2;
			selectedTile.destroyDefense();
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.turretTower){
			selectedTile.buildDefense(new TurretTower(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.barricade){
			selectedTile.buildDefense(new BarricadeDefense(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.freezeTower){
			selectedTile.buildDefense(new FreezeTower(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.poisonTower){
			selectedTile.buildDefense(new PoisonTower(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.tarPool){
			selectedTile.buildDefense(new TarPoolDefense(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
		else if(buildingOption.getOptionType() == BuildingOption.OptionType.boulderTower){
			selectedTile.buildDefense(new BoulderTower(selectedTile.getTileX(), selectedTile.getTileY()));
			paths.recalculatePathsWithTile(selectedTile, tileGrid);
			revalidateEnemyPaths(selectedTile);
		}
	}

	private void revalidateEnemyPaths(Tile affectedTile) {
		for(int i=0; i < gameElems.enemies.size(); i++){
			gameElems.enemies.get(i).verifyValidPath(tileGrid, affectedTile);
		}
	}

	private void updateDefenses(float deltaTime) {
		for(int i=0; i < gameElems.defenses.size();i++){
			gameElems.defenses.get(i).update(deltaTime, gameElems.enemies);
			if(gameElems.defenses.get(i).toRemove){
				Tile affectedTile = getTileInGrid(gameElems.defenses.get(i).getCenterX(),
						gameElems.defenses.get(i).getCenterY());
				affectedTile.removeDefense();
				gameElems.defenses.remove(i);
				i--;
				paths.recalculatePathsWithTile(affectedTile, tileGrid);
				revalidateEnemyPaths(affectedTile);
			}
		}
	}

	private void updateProjectiles(float deltaTime) {
		for(int i=0; i < gameElems.projectiles.size();i++){
			gameElems.projectiles.get(i).update(deltaTime, gameElems.enemies);
			if(gameElems.projectiles.get(i).toRemove){
				gameElems.projectiles.remove(i);
				i--;
			}
		}
	}



	private Tile getSelectedTile(int x, int y) {
		Tile tapped = getTileInGrid(x, y);
		if(tapped.isSelectable()){
			selectedTileOptions = tapped.getBuildingOptions();
			BuildingOption.selectedDefObj = tapped.getDefenseObject();
			return tapped;
		}
		else {
			selectedTileOptions = null;
			return null;
		}
	}

	private Tile getTileInGrid(int x, int y) {
		int gridX = x/Tile.TILE_SIDE;
		int gridY = (y-INFO_BAR_HEIGHT)/Tile.TILE_SIDE;
		Tile tapped = tileGrid.get(gridY).get(gridX);
		return tapped;
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = (TouchEvent) touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 240)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 240, 800, 240)) {
					nullify();
					goToMenu();
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 0, 0, 800, 480)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}

	}

	/**
	 * Paints the game objects and top info bar and then the user interface specific objects deppending
	 *  on the game state
	 */
	@Override
	public void paint(float deltaTime) {
		if(gameElems == null)
			return;
		Graphics g = game.getGraphics();

		paintTiles(g);
		if(selectedTile != null && selectedTile.getTileX()+Tile.TILE_SIDE >= curX && selectedTile.getTileX() <= curX+800
				&& selectedTile.getTileY()+Tile.TILE_SIDE >= curY && selectedTile.getTileY() <= curY+480){

			g.drawImage(Assets.selectionOverlay, selectedTile.getTileX()-curX, selectedTile.getTileY()-curY);
			if(selectedTile.hasDefenseObject()){
				DefenseObject defObj =  selectedTile.getDefenseObject();
				double radius = selectedTile.getDefenseObject().getAttackRadius();
				g.drawCircle(defObj.getCenterX()-curX, defObj.getCenterY()-curY, (float) radius, turnColorTranslucid(Color.GREEN));
				double remaingHP = (double)defObj.getHitpoints()/(double)defObj.getMaxHitpoints();
				g.drawRect(defObj.getX()-curX, defObj.getY()-6-curY, defObj.getCurrentImage().getWidth(), 5, Color.RED);
				g.drawRect(defObj.getX()-curX, defObj.getY()-6-curY, 
						(int) (defObj.getCurrentImage().getWidth()*remaingHP), 5, Color.GREEN);
				
			}
		}
		paintGameObjs(g);
		paintInfoBar(g);

		// Draw the UI above the game elements.
		if (state == GameState.DisplayingEnemyUnits)
			drawDisplayingEnemyUnitsUI();
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
		if (state == GameState.Victory)
			drawVictoryUI();
	}

	private int turnColorTranslucid(int color) {
		return (color & TRANSLUCID_MASK);
	}

	private void paintGameObjs(Graphics g) {
		PriorityQueue<GameObj> objects = gameElems.getGameObjsForDrawing();
		while(!objects.isEmpty()){
			GameObj curObject = objects.remove();
			Image curImage = curObject.getCurrentImage();
			int xPos = curObject.getX();
			int yPos = curObject.getY();

			if(xPos+curImage.getWidth() >= curX && xPos <= curX+800 
					&& yPos+curImage.getHeight() >= curY && yPos <= curY + 480){
				g.drawImage(curImage, xPos-curX, yPos-curY);
				ArrayList<StatusEffect> statusEffs = curObject.getStatusEffects();
				if(statusEffs != null)
					for(int i=0; i < statusEffs.size(); i++)
						g.drawImage(statusEffs.get(i).getImage(), xPos-curX, yPos-curY+(curImage.getHeight()-StatusEffect.EFFECT_IMAGE_SIDE));
			}
		}
	}

	private void updateVictory(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 30, 340, 270, 90)) {
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					game.setScreen(new LevelSelectionScreen(game));
				}
				else if(inBounds(event, 480, 340, 250, 90) && chosenLevel < TowerDefenseGame.NUM_LEVELS && chosenLevel<TowerDefenseGame.NUM_LEVELS-1){
					Assets.buttonClick.play(MusicPlayer.SOUNDS_VOLUME);
					nullify();
					game.setScreen(new LevelScreen(game, chosenLevel+1));
				}

			}
		}
	}

	private void drawVictoryUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(200, 0, 0, 0);
		g.drawString(Messages.getString("LevelScreen.8"), 400, 140, paint2); //$NON-NLS-1$
		g.drawString(Messages.getString("LevelScreen.9")+Integer.toString(gameElems.curScore), 400, 240, paint2); //$NON-NLS-1$
		//g.drawRect(30, 340, 270, 90, Color.YELLOW);
		g.drawString(Messages.getString("LevelScreen.10"), 160, 420, paint2); //$NON-NLS-1$
		//.drawRect(480, 340, 250, 90, Color.YELLOW);
		if(chosenLevel < TowerDefenseGame.NUM_LEVELS-1)
			g.drawString(Messages.getString("LevelScreen.11"), 600, 420, paint2); //$NON-NLS-1$
	}

	private void drawDisplayingEnemyUnitsUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(155, 0, 0, 0);
		g.drawString(Messages.getString("LevelScreen.12"), 400, 180, paint); //$NON-NLS-1$
		int xPos = 400-monstersInLevelSprites.length*60/2;
		for(Image curImage: monstersInLevelSprites){
			g.drawImage(curImage, xPos, 220);
			xPos += 60;
		}
		g.drawString(Messages.getString("LevelScreen.13"), 400, 350, paint); //$NON-NLS-1$
	}

	private void paintInfoBar(Graphics g) {
		g.drawImage(Assets.infoBarBG, 0, 0);
		for(int i=0; i < gameElems.livesLeft; i++)
			g.drawImage(Assets.lifeIcon, lifeIconXStart+i*(Assets.lifeIcon.getWidth()), 0);
		g.drawImage(Assets.coinIcon, coinIconXStart, 0);
		g.drawString(Integer.toString(gameElems.currentBalance), coinIconXStart+Assets.lifeIcon.getWidth(), 
				LevelScreen.INFO_BAR_HEIGHT-7, smallYellowText);
		g.drawString(Messages.getString("LevelScreen.14")+Integer.toString(gameElems.curScore), scoreXStart,  //$NON-NLS-1$
				LevelScreen.INFO_BAR_HEIGHT-7, smallWhiteText);
	}


	private void paintTiles(Graphics g) {
		for(int j=0; j < tileGrid.size(); j++){
			for (int i = 0; i < tileGrid.get(j).size(); i++) {
				Tile t = tileGrid.get(j).get(i);
				int xPos = t.getTileX();
				int yPos = t.getTileY();
				if (xPos+t.getTileImage().getWidth() >= curX && xPos <= curX+800 && 
						yPos+t.getTileImage().getHeight() >= curY && yPos <= curY+480) {
					g.drawImage(t.getTileImage(), xPos-curX, yPos-curY);
				}
			}
		}
	}

	private void nullify() {

		paint = null;
		paint2 = null;
		smallYellowText = null;
		smallWhiteText = null;
		gameElems = null;
		smallCenteredText = null;
		paths = null;
		enemyWaves = null;

		// Call garbage collector to clean up memory.
		System.gc();

	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.startIcon, startIconXPos, startIconYPos);
		drawSelectionUI();
	}

	private void drawRunningUI() {
		//Graphics g = game.getGraphics();
		drawSelectionUI();
	}

	private void drawSelectionUI() {
		Graphics g = game.getGraphics();
		if(selectedTile != null){
			int xStart = 400 - BuildingOption.OPTION_ICON_WIDTH*selectedTileOptions.length/2;
			for(int i=0; i < selectedTileOptions.length; i++){
				g.drawImage(Assets.buildingOptionsBG, xStart+i*BuildingOption.OPTION_ICON_WIDTH, BUILDING_OPTIONS_Y_POS);
				g.drawImage(selectedTileOptions[i].getOptionIcon(), 
						xStart+i*BuildingOption.OPTION_ICON_WIDTH, BUILDING_OPTIONS_Y_POS);
				g.drawString(selectedTileOptions[i].getIconDescription(),
						xStart+i*BuildingOption.OPTION_ICON_WIDTH+BuildingOption.OPTION_ICON_WIDTH/2,
						BUILDING_OPTIONS_Y_POS+BuildingOption.OPTION_ICON_WIDTH, smallCenteredText);
			}
		}
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(155, 0, 0, 0);
		g.drawString(Messages.getString("LevelScreen.15"), 400, 165, paint2); //$NON-NLS-1$
		g.drawString(Messages.getString("LevelScreen.16"), 400, 360, paint2); //$NON-NLS-1$
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 1281, 801, Color.BLACK);
		g.drawString(Messages.getString("LevelScreen.17"), 400, 240, paint2); //$NON-NLS-1$
		g.drawString(Messages.getString("LevelScreen.18"), 400, 290, paint); //$NON-NLS-1$

	}

	/**
	 * Pauses the game, changing its state to Paused
	 */
	@Override
	public void pause() {
		if (state == GameState.Running || state == GameState.Ready){
			lastState = state;
			state = GameState.Paused;
		}

	}
	/**
	 * Continues the game, resuming with the state the game was in when it was paused
	 */
	@Override
	public void resume() {
		if (state == GameState.Paused)
			state = lastState;
	}

	@Override
	public void dispose() {
	}

	/**
	 * Pauses the game
	 */
	@Override
	public void backButton() {
		pause();
	}

	private void goToMenu() {
		game.setScreen(new MainMenuScreen(game));
	}
}