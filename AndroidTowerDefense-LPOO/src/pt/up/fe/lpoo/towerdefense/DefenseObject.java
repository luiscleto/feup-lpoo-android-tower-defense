package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

/**
 * Abstract class to define Defense objects
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see GameObj
 */
public abstract class DefenseObject extends GameObj {
	private int xPos, yPos;
	protected float reloadTime, elapsedSeconds;
	protected int maxHitpoints;
	protected int hitpoints;
	/** indicates if this defense object is to be removed or not */
	public boolean toRemove = false;
	protected double attackRadius;
	protected int level;
	protected boolean foundEnemies;

	/**
	 * Constructor for the DefenseObject
	 * @param reloadTime time it takes to reload after an attack
	 * @param hitpoints health the defense object has
	 * @param xPos x position of the defense object
	 * @param yPos y position of the defense object
	 * @param attRadius radius of attack the defense object can cover
	 */
	public DefenseObject(float reloadTime, int hitpoints, int xPos, int yPos, double attRadius){
		this.reloadTime = reloadTime;
		this.elapsedSeconds = reloadTime;
		if(reloadTime == 0)
			reloadTime = 0.01f;
		this.xPos = xPos;
		this.yPos = (int) (yPos - ((getTileHeight()-1) * Tile.TILE_SIDE));
		this.maxHitpoints = hitpoints;
		this.hitpoints = hitpoints;
		this.attackRadius = attRadius;
		this.level = 1;
	}
	
	public double getAttackRadius(){
		return attackRadius;
	}
	/**
	 * @return returns the cost to build the defense object
	 */
	public abstract int getCost();

	/**
	 * Updates a counter and if it is greater than the reload time, attacks the enemies. If no enemies can
	 *  currently be attacked, sets the elapsed time counter equal to the reload time
	 * @param deltaTime time elapsed since last update
	 * @param enemies array of enemies to verify for attack
	 */
	public void update(float deltaTime, ArrayList<Enemy> enemies){
		if(!toRemove){
			this.elapsedSeconds += (deltaTime/100.0f);
			while(elapsedSeconds >= reloadTime){
				elapsedSeconds -= reloadTime;
				foundEnemies = false;
				attack(enemies);
				if(!foundEnemies){
					elapsedSeconds = reloadTime;
					break;
				}
			}
		}
	}

	protected boolean isWithinRadius(Enemy enemy){
		double enemyX = enemy.getCenterX(); double enemyY = enemy.getCenterY();
		double centerX = getCenterX(); double centerY = getCenterY();

		double distance = Math.sqrt(Math.pow((enemyX-centerX),2.0)+Math.pow((enemyY-centerY),2.0));

		return (distance <= attackRadius);
	}

	@Override
	public int getCenterX(){
		return xPos +Tile.TILE_SIDE/2;
	}
	@Override
	public int getCenterY(){
		return (int) (yPos +getTileHeight()*Tile.TILE_SIDE - Tile.TILE_SIDE/2);
	}
	@Override
	public ArrayList<StatusEffect> getStatusEffects(){
		return null;
	}
	/**
	 * @return returns true if the defense object can be upgraded, false otherwise
	 */
	public abstract boolean isUpgradable();
	/**
	 * @return returns the cost of upgrading the defense object
	 */
	public abstract int getUpgradeCost();
	/**
	 * Upgrades the defense object, raising its level and base stats
	 */
	public abstract void upgrade();

	protected abstract void attack(ArrayList<Enemy> enemies);

	/**
	 * Damages the defense object, setting it to be removed if hitpoints fall below 0
	 * @param damage damage received
	 */
	public void receiveDamage(int damage){
		hitpoints -= damage;
		if(hitpoints <= 0){
			toRemove = true;
		}
	}
	
	public int getHitpoints(){
		return hitpoints;
	}
	/**
	 * @return returns the x position of the upper left corner of the defense object
	 */
	@Override
	public int getX(){
		return xPos;
	}
	/**
	 * @return returns the y position of the upper left corner of the defense object
	 */
	@Override
	public int getY(){
		return yPos;
	}

	protected float getTileHeight(){
		return ((float)getCurrentImage().getHeight() / (float)Tile.TILE_SIDE);
	}
	/**
	 * Repairs the defense object if the player has enough credits to do so
	 */
	public void repair(){
		int cost = getRepairCost();
		if(LevelScreen.gameElems.currentBalance >= cost){
			LevelScreen.gameElems.currentBalance -= cost;
			hitpoints=maxHitpoints;
		}
	}
	/**
	 * @return returns the hitpoint capacity for the defense object
	 */
	public int getMaxHitpoints(){
		return maxHitpoints;
	}
	/**
	 * @return returns true if enemies can walk across this defense object, false otherwise
	 */
	public abstract boolean isCrossable();
	/**
	 * @return returns the money cost to repair the defense object
	 */
	public abstract int getRepairCost();
}
