package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the boulder tower defense object. It shoots large, heavy projectiles at a low rate
 *  of fire to enemies within a large radius. It targets enemies closer to their goal first.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class BoulderTower extends DefenseObject{
	private static final int BOULDERTOWER_HP = 750;
	private static final float RELOAD_TIME = 3f;
	private static final double BOULDERTOWER_RADIUS = 250.0;
	 /** cost to build a boulder tower */
	public static final int BOULDERTOWER_COST = 200;
	private int projectileDamage, projectileSpeed;

	/**
	 * Constructor for the BoulderTower class. Calls the constructor for the DefenseObject class and sets the base
	 *  stats for projectile damage and speed
	 * @param xPos position of the tower in the x axis of the game map
	 * @param yPos position of the tower in the y axis of the game map
	 */
	public BoulderTower(int xPos, int yPos){
		super(RELOAD_TIME, BOULDERTOWER_HP, xPos, yPos, BOULDERTOWER_RADIUS);
		projectileDamage = 15; projectileSpeed = 8;

	}

	@Override
	protected void attack(ArrayList<Enemy> enemies) {
		Enemy toAttack = null;
		for(int i=0; i < enemies.size(); i++){
			if(isWithinRadius(enemies.get(i))){
				if(toAttack == null)
					toAttack = enemies.get(i);
				else if(toAttack.getDistToGoal() > enemies.get(i).getDistToGoal())
					toAttack = enemies.get(i);
			}
		}
		if(toAttack != null){
			LevelScreen.gameElems.addProjectile( new BoulderProjectile(this.getCenterX(), this.getCenterY(), 
					toAttack.getCenterX(), toAttack.getCenterY(), projectileSpeed, projectileDamage));
			foundEnemies = true;
		}
	}

	/**
	 * Returns the current Image to represent the tower.
	 * 
	 * @return returns the Image object corresponding the current image of the tower
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.boulderTower;
	}

	/**
	 * Indicates whether this tower can be upgraded or not
	 * 
	 * @return returns true if it can be upgraded, false otherwise
	 */
	@Override
	public boolean isUpgradable() {
		return false;
	}

	/**
	 * Returns the cost to upgrade the tower
	 * 
	 * @return returns the necessary credit to upgrade this tower
	 */
	@Override
	public int getUpgradeCost() {
		return 0;
	}

	/**
	 * Just a stub as this tower can't be upgraded yet
	 */
	@Override
	public void upgrade() {

	}

	/**
	 * @return returns the cost to build this tower
	 */
	@Override
	public int getCost() {
		return BOULDERTOWER_COST;
	}

	/**
	 * Indicates whether this defense will block an enemies path or not
	 * 
	 * @return returns false if enemies cant walk across this defense, true otherwise
	 */
	@Override
	public boolean isCrossable() {
		return false;
	}

	/**
	 * Indicates whether this tower is part of the ground terrain or if it has a height (for visibility purposes)
	 * 
	 * @return returns true if the tower is at ground level, false otherwise
	 */
	@Override
	public boolean isGroundObject() {
		return false;
	}

	/**
	 * @return returns the cost required to repair the tower
	 */
	@Override
	public int getRepairCost() {
		return 3*(BOULDERTOWER_HP-hitpoints);
	}
}
