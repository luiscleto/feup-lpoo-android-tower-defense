package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class contains the information of the game map's tiles.
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class Tile implements Comparable<Tile> {
	private static final int upgradeOptionIndex = 1;

	private static final BuildingOption[] emptyTowerTileOptions = {new BuildingOption(BuildingOption.OptionType.cancel),
		new BuildingOption(BuildingOption.OptionType.turretTower, TurretTower.TURRET_COST),
		new BuildingOption(BuildingOption.OptionType.poisonTower, PoisonTower.POISON_TOWER_COST),
		new BuildingOption(BuildingOption.OptionType.freezeTower, FreezeTower.FREEZE_TOWER_COST),
		new BuildingOption(BuildingOption.OptionType.boulderTower, BoulderTower.BOULDERTOWER_COST)};
	
	private static final BuildingOption[] emptyPathTileOptions = {new BuildingOption(BuildingOption.OptionType.cancel),
		new BuildingOption(BuildingOption.OptionType.barricade, BarricadeDefense.BARRICADE_COST),
		new BuildingOption(BuildingOption.OptionType.tarPool, TarPoolDefense.TARPOOL_DEFENSE_COST)};
	
	private static final BuildingOption[] upgradeableOptions = {new BuildingOption(BuildingOption.OptionType.cancel),
		new BuildingOption(BuildingOption.OptionType.upgrade),
		new BuildingOption(BuildingOption.OptionType.repair),
		new BuildingOption(BuildingOption.OptionType.destroy)};
	
	private static final BuildingOption[] notUpgradeableOptions = {new BuildingOption(BuildingOption.OptionType.cancel),
		new BuildingOption(BuildingOption.OptionType.repair),
		new BuildingOption(BuildingOption.OptionType.destroy)};
	
	/** Indicator of a terrain where you can build towers */
	public static final char TOWER_TERRAIN = 'X';
	/** Indicator of a terrain through which enemies can travel */
	public static final char PATH = '_';
	/** Indicator of the enemies' goal terrain */
	public static final char MONSTER_GOAL = 'O';
	/** Indicator of the enemies' spawning terrain */
	public static final char MONSTER_ENTRANCE = 'E';
	/** Length of the side of a tile in pixels */
	public static final int TILE_SIDE = 50;
	private char type; 
	private Image tileImage; 

	private int tileX;
	private int tileY;
	private boolean hasDefenseObject;
	private DefenseObject currentConstruct;

	//Pathfinding specific variables
	private Tile path;
	private float distanceToStart;
	private float distanceToEnd;

	/**
	 * Constructor for the tile class
	 * @param x x position of the upper left corner of the tile
	 * @param y y position of the upper left corner of the tile
	 * @param type type of terrain for the tile
	 */
	public Tile(int x, int y, char type) {
		this.tileX = x*Tile.TILE_SIDE;
		this.tileY = LevelScreen.INFO_BAR_HEIGHT+y*Tile.TILE_SIDE;
		this.type = type;
		this.hasDefenseObject = false;
		this.currentConstruct = null;

		if (type == TOWER_TERRAIN) {
			tileImage = Assets.tileEmptyTower;
		} else if (type == PATH) {
			tileImage = Assets.tilePath;
		} else if (type == MONSTER_GOAL) {
			tileImage = Assets.tileGoal;
		} else if (type == MONSTER_ENTRANCE) {
			tileImage = Assets.tileEntrance;
		}
	}
	
	/**
	 * Indicates whether a defense has been built on this tile or not
	 * @return true if there is a defense object on this tile, false otherwise
	 */
	public boolean hasDefenseObject(){
		return hasDefenseObject;
	}

	/**
	 * @return returns the x position of the tile's upper left corner
	 */
	public int getTileX() {
		return tileX;
	}
	/**
	 * @return returns the y position of the tile's upper left corner
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * @return returns the row number of the tile in the game map tile grid
	 */
	public int getGridX() {
		return tileX/tileImage.getWidth();
	}
	/**
	 * @return returns the line number of the tile in the game map tile grid
	 */
	public int getGridY() {
		return tileY/tileImage.getHeight();
	}

	/**
	 * Indicates whether the user can select this tile or not
	 * @return returns true if the tile can be selected, false otherwise
	 */
	public boolean isSelectable(){
		return (type != MONSTER_GOAL && type != MONSTER_ENTRANCE);
	}

	/**
	 * @return returns the current image to represent the tile
	 */
	public Image getTileImage() {
		return tileImage;
	}
	/**
	 * Determines whether a given point is within the tile's borders or not
	 * @param x x position of the point
	 * @param y y position of the point
	 * @return returns true if the point is within the tile's borders, false otherwise
	 */
	public boolean isInsideTile(int x, int y){
		return (x >= tileX && x <= tileX+tileImage.getWidth() && y >= tileY && y <= tileY+tileImage.getHeight());
	}
	/**
	 * @return returns the x position of the center of the tile
	 */
	public int getCenterX(){
		return tileX+TILE_SIDE/2;
	}
	/**
	 * @return returns the y position of the center of the tile
	 */
	public int getCenterY(){
		return tileY+TILE_SIDE/2;
	}
	/**
	 * @return returns the type of terrain of the tile
	 */
	public char getType() {
		return type;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @return returns current path from the tile
	 */
	public Tile getPath() {
		return path;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @param path new path for the tile
	 */
	public void setPath(Tile path) {
		this.path = path;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @return returns distance from this tile to the starting point of the path
	 */
	public float getDistanceToStart() {
		return distanceToStart;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @param distanceToStart new distance to start from this tile to the starting point of the path
	 */
	public void setDistanceToStart(float distanceToStart) {
		this.distanceToStart = distanceToStart;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @return returns distance from this tile to the end point of the path
	 */
	public float getDistanceToEnd() {
		return distanceToEnd;
	}
	/**
	 * Used for the enemies' pathfinding algorithm
	 * @param distanceToStart new distance to start from this tile to the end point of the path
	 */
	public void setDistanceToEnd(float distanceToEnd) {
		this.distanceToEnd = distanceToEnd;
	}
	/**
	 * @return returns the current DefenseObject on this tile, null if there isn't one
	 */
	public DefenseObject getDefenseObject(){
		return currentConstruct;
	}

	/**
	 * Builds a DefenseObject in this tile and adds it to the game's list of defenses
	 *  if the user has enough credits to build it
	 * @param defenseObj defense object to be added to the tile
	 */
	public void buildDefense(DefenseObject defenseObj){
		if(LevelScreen.gameElems.currentBalance >= defenseObj.getCost()){
			LevelScreen.gameElems.currentBalance -= defenseObj.getCost();
			this.currentConstruct = defenseObj;
			this.hasDefenseObject = true;
			LevelScreen.gameElems.addDefense(defenseObj);
		}
	}
	/**
	 * Fatally injures the DefenseObject in this tile in case there is one, setting it to be removed
	 */
	public void destroyDefense(){
		if(currentConstruct != null)
			currentConstruct.receiveDamage(currentConstruct.getHitpoints());
	}
	/**
	 * Resets the tile's information relative to the current DefenseObject on the tile
	 */
	public void removeDefense(){
		currentConstruct = null;
		hasDefenseObject = false;
	}
	
	/**
	 * @return returns an array with the available building options for this tile
	 */
	public BuildingOption[] getBuildingOptions(){
		if(currentConstruct != null){
			if(currentConstruct.isUpgradable()){
				upgradeableOptions[upgradeOptionIndex].setOptionCost(currentConstruct.getUpgradeCost());
				return upgradeableOptions;
			}
			else
				return notUpgradeableOptions;
		}
		else{
			if(type == TOWER_TERRAIN)
				return emptyTowerTileOptions;
			else
				return emptyPathTileOptions;
		}
	}

	/**
	 * Used in the enemies' pathfinding algorithm
	 * @return compares the Tiles by their distance to the start and to the end of the path
	 */
	@Override
	public int compareTo(Tile another) {

		float diference = (distanceToStart + distanceToEnd) - (another.distanceToStart + another.distanceToEnd);

		if( diference < 0 )
			return -1;
		else if ( diference > 0)
			return 1;
		else
			return 0;
	}
}