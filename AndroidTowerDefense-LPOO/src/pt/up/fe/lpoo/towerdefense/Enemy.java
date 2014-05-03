package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

/**
 * Abstract class to define Enemy objects
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see GameObj
 */
public abstract class Enemy extends GameObj{

	private static final double MOVE_REACTION_TIME = 0.02;
	protected int centerX, centerY, speedX, speedY;
	protected int movementSpeed, health, damage;
	protected ArrayList<Tile> path;
	protected ArrayList<StatusEffect> statusEffects;
	protected int curTileIndex;
	/** indicates whether the enemy is to be removed from the enemy list or not */
	public boolean toRemove = false;
	protected boolean dead = false;
	protected boolean isAttacking = false;
	protected int scoreWorth, moneyWorth;
	private float elapsedSeconds;
	private float attackCooldown;
	protected double reactionTimeModifier = 1.0;
	private boolean resettingToTilePosition =false;
	protected int piercingArmor, crushingArmor, magicArmor;
	private boolean usingUniquePath = false;
	private boolean firstUpdate = true;

	/**
	 * Damage types allowed by the game
	 */
	public enum DamageType{
		Piercing, Crushing, Magic
	}

	private static final int TOWER_TERRAIN = 0;
	private static final int PATH = 1;
	private static final int MONSTER_ENTRANCE = 2;
	private static final int MONSTER_GOAL = 3;

	/**
	 * Constructor for the Enemy objects. Sets the position of the enemy at the beggining of the path
	 * @param health hp of the enemy
	 * @param moveSpeed number of pixels the enemy can move 50 times per second
	 * @param score score the user will gain if he defeats the enemy
	 * @param money money the user will gain if he defeats the enemy
	 * @param path path the enemy will follow
	 * @param damage base damage the enemy will deal to defense objects
	 * @param attCooldown cooldown between enemy attacks on defense objects
	 */
	public Enemy(int health, int moveSpeed, int score, int money, ArrayList<Tile> path, int damage, float attCooldown){
		this.statusEffects = new ArrayList<StatusEffect>();
		this.elapsedSeconds = 0;
		this.path = path;
		this.curTileIndex = 0;
		this.attackCooldown = attCooldown;
		this.centerX = path.get(0).getCenterX();
		this.centerY = path.get(0).getCenterY();
		this.health = health;
		this.movementSpeed = moveSpeed;
		this.scoreWorth = score;
		this.moneyWorth = money;
		this.speedX = 0;
		this.speedY = 0;
		this.damage = damage;
	}

	public boolean isDead(){
		return dead;
	}
	public int getHealth(){
		return health;
	}
	/**
	 * Updates the status effects first.
	 * 
	 * If the enemy is not frozen, increments the elapsed time counter and if its greater than the enemy's reaction
	 *  time moves the enemy if it can move, attacks the defense object blocking the path otherwise.
	 * @param deltaTime elapsed time since the last update
	 */
	public void update(float deltaTime) {
		if(!toRemove){
			if(firstUpdate){
				firstUpdate = false;
				determineDirection();
			}
			updateStatusEffects(deltaTime);
			if(hasStatusEffect(StatusEffect.EffectType.Frozen))
				return;
			elapsedSeconds = elapsedSeconds + (deltaTime/100.0f);


			if(curTileIndex < path.size()-1){
				if(path.get(curTileIndex+1).hasDefenseObject()){
					if(!path.get(curTileIndex+1).getDefenseObject().isCrossable()){
						isAttacking = true;
						while(elapsedSeconds >= attackCooldown*reactionTimeModifier 
								&& !path.get(curTileIndex+1).getDefenseObject().toRemove){

							elapsedSeconds -= attackCooldown*reactionTimeModifier;
							path.get(curTileIndex+1).getDefenseObject().receiveDamage(damage);
						}
						return;
					}
				}
			}
			if(isAttacking)
				elapsedSeconds = (deltaTime/100.0f);

			isAttacking = false;
			int lastPosX = centerX;
			int lastPosY = centerY;

			while(elapsedSeconds >= MOVE_REACTION_TIME*reactionTimeModifier){
				elapsedSeconds -= MOVE_REACTION_TIME*reactionTimeModifier;
				if(resettingToTilePosition){
					resetToPositionMove();
					break;
				}
				centerX += speedX;
				centerY += speedY;


				if(hasArrivedAtNextTile(lastPosX, lastPosY, centerX, centerY)){
					centerX = path.get(curTileIndex+1).getCenterX();
					centerY = path.get(curTileIndex+1).getCenterY();
					advanceInPath();
					if(toRemove)
						return;
				}
			}
		}
	}

	private void resetToPositionMove() {
		if(centerX < path.get(curTileIndex).getCenterX()){
			centerX += movementSpeed;
			if(!(centerX < path.get(curTileIndex).getCenterX()))
				resettingToTilePosition = false;
		}
		else if(centerX > path.get(curTileIndex).getCenterX()){
			centerX -= movementSpeed;
			if(!(centerX > path.get(curTileIndex).getCenterX()))
				resettingToTilePosition = false;
		}
		else if(centerY < path.get(curTileIndex).getCenterY()){
			centerY += movementSpeed;
			if(!(centerY < path.get(curTileIndex).getCenterY()))
				resettingToTilePosition = false;
		}
		else if(centerY > path.get(curTileIndex).getCenterY()){
			centerY -= movementSpeed;
			if(!(centerY > path.get(curTileIndex).getCenterY()))
				resettingToTilePosition = false;
		}
		else
			resettingToTilePosition = false;
		if(!resettingToTilePosition){
			this.centerX = path.get(curTileIndex).getCenterX();
			this.centerY = path.get(curTileIndex).getCenterY();
		}
	}

	private void resetReactionTime(){
		this.reactionTimeModifier = 1.0;
	}
	
	/**
	 * Checks if the enemy is currently affected by a certain type of status effect
	 * @param type type of status effect to check for
	 * @return returns true if the enemy has the status effect, false otherwise
	 */
	public boolean hasStatusEffect(StatusEffect.EffectType type) {
		for(int i=0; i < statusEffects.size(); i++){
			if(statusEffects.get(i).type == type && !statusEffects.get(i).toRemove)
				return true;
		}
		return false;
	}
	/**
	 * Returns an array with all of the status effect on the enemy object
	 */
	@Override
	public ArrayList<StatusEffect> getStatusEffects(){
		return statusEffects;
	}
	/**
	 * Adds a new status effect to the enemy
	 * @param newEffect status effect to be added
	 */
	public void addStatusEffect(StatusEffect newEffect){
		for(int i=0; i < statusEffects.size(); i++){
			if(newEffect.type == statusEffects.get(i).type){
				if(newEffect.getRemainingTime() > statusEffects.get(i).getRemainingTime())
					statusEffects.set(i, newEffect);
				return;
			}
		}
		statusEffects.add(newEffect);
	}
	
	/**
	 * Updates the status effects of the enemy, dealing damage when necessary and updating the reaction time
	 *  modifier
	 * @param deltaTime elapsed time since the last update
	 */
	public void updateStatusEffects(float deltaTime) {
		for(int i=0; i < statusEffects.size(); i++){
			statusEffects.get(i).update(deltaTime);
			if(reactionTimeModifier < statusEffects.get(i).getReactionTimeModifier())
				reactionTimeModifier = statusEffects.get(i).getReactionTimeModifier();
			damage(statusEffects.get(i).getDamage(), statusEffects.get(i).getDamageType());
			if(statusEffects.get(i).toRemove){
				if(statusEffects.get(i).type == StatusEffect.EffectType.Slowed)
					resetReactionTime();
				statusEffects.remove(i);
				i--;
			}
		}
	}
	public double getReactionTimeModifier(){
		return reactionTimeModifier;
	}

	private boolean hasArrivedAtNextTile(int lastPosX, int lastPosY, int newPosX, int newPosY){
		if(curTileIndex >= path.size()-1){
			LevelScreen.gameElems.livesLeft--;
			toRemove = true;
			return false;
		}
		int tileCenterX = path.get(curTileIndex+1).getCenterX();
		int tileCenterY = path.get(curTileIndex+1).getCenterY();

		if(lastPosX == newPosX && 
				((lastPosY <= tileCenterY && newPosY > tileCenterY) || (lastPosY >= tileCenterY && newPosY < tileCenterY)))
			return true;
		else if(lastPosY == newPosY && 
				((lastPosX <= tileCenterX && newPosX > tileCenterX) || (lastPosX >= tileCenterX && newPosX < tileCenterX)))
			return true;
		else
			return false;
	}

	/**
	 * Moves to the next tile of the path
	 */
	public void advanceInPath(){
		curTileIndex++;
		if(curTileIndex >= path.size()-1){
			LevelScreen.gameElems.livesLeft--;
			toRemove = true;
			return;
		}
		determineDirection();
	}

	private void determineDirection() {
		int tileCenterX,tileCenterY, nextTileCenterX, nextTileCenterY;
		if(curTileIndex < path.size()-1){
			tileCenterX = path.get(curTileIndex).getCenterX();
			tileCenterY = path.get(curTileIndex).getCenterY();
			nextTileCenterX = path.get(curTileIndex+1).getCenterX();
			nextTileCenterY = path.get(curTileIndex+1).getCenterY();
		}
		else if(curTileIndex < path.size()){
			tileCenterX = centerX;
			tileCenterY = centerY;
			nextTileCenterX = path.get(curTileIndex).getCenterX();
			nextTileCenterY = path.get(curTileIndex).getCenterY();
		}
		else
			return;

		if(tileCenterX == nextTileCenterX){
			speedX = 0;
			if(tileCenterY > nextTileCenterY)
				speedY = -movementSpeed;
			else
				speedY = movementSpeed;
		}
		else{
			speedY = 0;
			if(tileCenterX > nextTileCenterX)
				speedX = -movementSpeed;
			else
				speedX = movementSpeed;
		}
	}

	private void die(){
		dead = true;
		centerX = -100;
		centerY = -100;
		movementSpeed = 0;
		LevelScreen.gameElems.curScore += scoreWorth;
		LevelScreen.gameElems.currentBalance += moneyWorth;
		toRemove = true;
	}
	
	/**
	 * @return returns the distance left for the enemy to reach its goal (in pixels)
	 */
	public int getDistToGoal(){
		return Math.abs(path.get(curTileIndex).getCenterX()-centerX)+Math.abs(path.get(curTileIndex).getCenterY()-centerY)
				+(path.size()-1-curTileIndex)*Tile.TILE_SIDE;
	}

	/**
	 * Indicates whether the object is part of the ground terrain. Default for an enemy is false.
	 * 
	 * @return returns true if the object is part of the ground terrain, false otherwise.
	 */
	@Override
	public boolean isGroundObject(){
		return false;
	}

	/**
	 * Deals damage of a given type to the enemy. Sets the enemy dead if health goes below 0.
	 * @param hitDamage base value of the damage
	 * @param type type of the damage
	 */
	public void damage(int hitDamage, DamageType type){

		if(type == DamageType.Piercing)
			health -= hitDamage/piercingArmor;
		else if (type == DamageType.Crushing)
			health -= hitDamage/crushingArmor;
		else
			health -= hitDamage/magicArmor;

		if(health <= 0)
			die();
	}

	public void setPath(ArrayList<Tile> newPath){
		this.path = newPath;
	}

	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
	}

	@Override
	public int getCenterX() {
		return centerX;
	}

	@Override
	public int getCenterY() {
		return centerY;
	}
	/**
	 * @return returns the x position of the upper left corner of the enemy
	 */
	@Override
	public int getX() {
		return centerX-Tile.TILE_SIDE/2;
	}
	/**
	 * @return returns the y position of the upper left corner of the enemy
	 */
	@Override
	public int getY() {
		return centerY-Tile.TILE_SIDE/2 - (int)((getTileHeight()-1)*Tile.TILE_SIDE);
	}
	
	protected float getTileHeight(){
		return ((float)getCurrentImage().getHeight() / (float)Tile.TILE_SIDE);
	}

	/**
	 * Verifies if the enemy's path is still valid (enemies may be left outside the path after a PathCluster update).
	 *  If it isn't valid, calculates a new path
	 * @param tileGrid grid with all the map's tiles
	 * @param affectedTile the tile that was altered, causing an update
	 * @see PathCluster
	 */
	public void verifyValidPath(ArrayList<ArrayList<Tile>> tileGrid, Tile affectedTile){
		if(toRemove)
			return;
		boolean isOnPath = false;
		int gridX = centerX/Tile.TILE_SIDE;
		int gridY = (centerY-LevelScreen.INFO_BAR_HEIGHT)/Tile.TILE_SIDE;
		Tile curTile = tileGrid.get(gridY).get(gridX);

		int prevYSpeed = speedY; int prevXSpeed = speedX;

		if(usingUniquePath && (pathContainsTile(affectedTile) || !affectedTile.hasDefenseObject())){
			path = Enemy.getPathToPoint(gridX, gridY, path.get(path.size()-1).getGridX(),
					path.get(path.size()-1).getGridY(), tileGrid, getTileWeights());
			curTileIndex = 0;
			isOnPath = true;
		}
		else{
			for(int i=0; i < path.size(); i++){
				if(path.get(i).equals(curTile)){
					curTileIndex = i;
					isOnPath = true;
					break;
				}
			}
		}

		if(!isOnPath){
			path = Enemy.getPathToPoint(gridX, gridY, path.get(path.size()-1).getGridX(),
					path.get(path.size()-1).getGridY(), tileGrid, getTileWeights());
			curTileIndex = 0;
			usingUniquePath = true;
		}

		determineDirection();
		if(speedY*prevYSpeed == 0 && prevXSpeed*speedX == 0 && (centerX != path.get(curTileIndex).getCenterX() || 
				centerY != path.get(curTileIndex).getCenterY())){ //changed movement direction and is not centered
			resettingToTilePosition = true;
		}
	}
	
	private boolean pathContainsTile(Tile tile) {
		for(int i=0; i < path.size(); i++){
			if(path.get(i).equals(tile))
				return true;
		}
		return false;
	}

	/**
	 * @return returns an array with the weights for each type of tile for the enemy (used in the pathfinding)
	 */
	public abstract Float[] getTileWeights();


	//using Manhattan heuristics
	private static float heuristicFunction(int origX, int origY, int destX, int destY)
	{
		float difX = Math.abs(origX-destX);
		float difY = Math.abs(origY-destY);

		return difX+difY;
	}

	private static int isOnList(ArrayList<Tile> list, Tile tile)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) == tile)
				return i;
		}

		return -1;
	}

	/**
	 * Calculates the shortest path between two tiles, using weights for each type of tile. Uses the A* algorithm.
	 * @param origX x position of the starting tile
	 * @param origY y position of the starting tile
	 * @param destX x position of the destination tile
	 * @param destY y position of the destination tile
	 * @param tileMap grid with all the map's tiles
	 * @param tileWeights array with the tileweights to be used for determining the path 
	 * @return returns an array with the path between the two points
	 */
	public static ArrayList<Tile> getPathToPoint(int origX, int origY, int destX, int destY, ArrayList<ArrayList<Tile> > tileMap, Float[] tileWeights)
	{
		ArrayList<Tile> openList = new ArrayList<Tile>();
		ArrayList<Tile> closedList = new ArrayList<Tile>();

		//resets the necessary values of the tiles
		for(int j = 0; j < tileMap.size(); j++)
		{
			ArrayList<Tile> row = tileMap.get(j);

			for(int i = 0; i < row.size(); i++)
			{
				Tile column = row.get(i);

				column.setDistanceToStart(Float.MAX_VALUE);
				column.setDistanceToEnd(heuristicFunction(i, j, destX, destY));
				column.setPath(null);
			}
		}

		//sets the starting tile's distance to 0 and adds it to the open list
		Tile startingTile = tileMap.get(origY).get(origX);
		startingTile.setDistanceToStart(0);

		openList.add(startingTile);
		Collections.sort(openList);

		while(!openList.isEmpty())
		{
			Tile activeTile = openList.get(0);


			//if we're at the destination tile
			if(activeTile.getGridX() == destX && activeTile.getGridY() == destY)
			{
				ArrayList<Tile> shortestPath = getPath(tileMap, destX, destY);
				return shortestPath;
			}

			Tile neighbor;
			int neighborX, neighborY;
			float weight = Float.MAX_VALUE;

			//checks node on top
			neighborX = activeTile.getGridX();
			neighborY = activeTile.getGridY()-1;

			if(neighborY >= 0 && neighborY < tileMap.size())
			{	
				neighbor =  tileMap.get(neighborY).get(neighborX);

				switch(neighbor.getType())
				{
				case 'X':
					weight = tileWeights[Enemy.TOWER_TERRAIN];
					break;
				case '_':
					weight = tileWeights[Enemy.PATH];
					break;
				case 'O':
					weight = tileWeights[Enemy.MONSTER_GOAL];
					break;
				case 'E':
					weight = tileWeights[Enemy.MONSTER_ENTRANCE];
					break;
				}
				if(neighbor.hasDefenseObject()){
					if(!neighbor.getDefenseObject().isCrossable())
						weight += 50;
					else
						weight += 10;	
				}

				if(isOnList(openList, neighbor) == -1 && isOnList(closedList, tileMap.get(neighborY).get(neighborX)) == -1)
					openList.add(neighbor);

				if(activeTile.getDistanceToStart() + weight < neighbor.getDistanceToStart())
				{
					neighbor.setPath(activeTile);
					neighbor.setDistanceToStart(activeTile.getDistanceToStart() + weight);
				}
			}

			//checks node on bottom
			neighborX = activeTile.getGridX();
			neighborY = activeTile.getGridY()+1;

			if(neighborY >= 0 && neighborY < tileMap.size())
			{	
				neighbor =  tileMap.get(neighborY).get(neighborX);

				switch(neighbor.getType())
				{
				case 'X':
					weight = tileWeights[Enemy.TOWER_TERRAIN];
					break;
				case '_':
					weight = tileWeights[Enemy.PATH];
					break;
				case 'O':
					weight = tileWeights[Enemy.MONSTER_GOAL];
					break;
				case 'E':
					weight = tileWeights[Enemy.MONSTER_ENTRANCE];
					break;
				}
				if(neighbor.hasDefenseObject()){
					if(!neighbor.getDefenseObject().isCrossable())
						weight += 50;
					else
						weight += 10;	
				}

				if(isOnList(openList, neighbor) == -1 && isOnList(closedList, tileMap.get(neighborY).get(neighborX)) == -1)
					openList.add(neighbor);

				if(activeTile.getDistanceToStart() + weight < neighbor.getDistanceToStart())
				{
					neighbor.setPath(activeTile);
					neighbor.setDistanceToStart(activeTile.getDistanceToStart() + weight);
				}
			}

			//checks node on left
			neighborX = activeTile.getGridX()-1;
			neighborY = activeTile.getGridY();

			if(neighborX >= 0 && neighborX < tileMap.get(neighborY).size())
			{	
				neighbor =  tileMap.get(neighborY).get(neighborX);

				switch(neighbor.getType())
				{
				case 'X':
					weight = tileWeights[Enemy.TOWER_TERRAIN];
					break;
				case '_':
					weight = tileWeights[Enemy.PATH];
					break;
				case 'O':
					weight = tileWeights[Enemy.MONSTER_GOAL];
					break;
				case 'E':
					weight = tileWeights[Enemy.MONSTER_ENTRANCE];
					break;
				}
				if(neighbor.hasDefenseObject()){
					if(!neighbor.getDefenseObject().isCrossable())
						weight += 50;
					else
						weight += 10;	
				}

				if(isOnList(openList, neighbor) == -1 && isOnList(closedList, tileMap.get(neighborY).get(neighborX)) == -1)
					openList.add(neighbor);

				if(activeTile.getDistanceToStart() + weight < neighbor.getDistanceToStart())
				{
					neighbor.setPath(activeTile);
					neighbor.setDistanceToStart(activeTile.getDistanceToStart() + weight);
				}
			}


			//checks node on right
			neighborX = activeTile.getGridX()+1;
			neighborY = activeTile.getGridY();

			if(neighborX >= 0 && neighborX < tileMap.get(neighborY).size())
			{	
				neighbor =  tileMap.get(neighborY).get(neighborX);

				switch(neighbor.getType())
				{
				case 'X':
					weight = tileWeights[Enemy.TOWER_TERRAIN];
					break;
				case '_':
					weight = tileWeights[Enemy.PATH];
					break;
				case 'O':
					weight = tileWeights[Enemy.MONSTER_GOAL];
					break;
				case 'E':
					weight = tileWeights[Enemy.MONSTER_ENTRANCE];
					break;
				}
				if(neighbor.hasDefenseObject()){
					if(!neighbor.getDefenseObject().isCrossable())
						weight += 50;
					else
						weight += 10;	
				}

				if(isOnList(openList, neighbor) == -1 && isOnList(closedList, tileMap.get(neighborY).get(neighborX)) == -1)
					openList.add(neighbor);

				if(activeTile.getDistanceToStart() + weight < neighbor.getDistanceToStart())
				{
					neighbor.setPath(activeTile);
					neighbor.setDistanceToStart(activeTile.getDistanceToStart() + weight);
				}
			}

			closedList.add(openList.remove(0));
			Collections.sort(openList);
		}

		return null;

	}

	private static ArrayList<Tile> getPath(ArrayList<ArrayList<Tile>> tileMap, int destX, int destY) 
	{
		if(destY < 0 || destY >= tileMap.size())
			return null;

		if(destX < 0 || destY >= tileMap.get(destY).size())
			return null;

		ArrayList<Tile> shortestPath = new ArrayList<Tile>();

		Tile tile = tileMap.get(destY).get(destX);
		shortestPath.add(tile);

		while(tile.getPath() != null)
		{
			tile = tile.getPath();
			shortestPath.add(tile);
		}

		Collections.reverse(shortestPath);

		return shortestPath;
	}

	/**
	 * @return returns true if the enemy can fly, false otherwise
	 */
	public abstract boolean canFly();

}