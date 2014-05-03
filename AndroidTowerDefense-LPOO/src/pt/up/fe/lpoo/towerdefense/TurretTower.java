package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the turret tower defense object. It shoots small, rapid projectiles at a high rate
 *  of fire to enemies within a large radius. It targets enemies closer to their goal first.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class TurretTower extends DefenseObject{
	private static final int TURRET_HP = 500;
	private static final float RELOAD_TIME = 1f;
	private static final double TURRET_RADIUS = 200.0;
	/** cost to build a turret tower */
	public static final int TURRET_COST = 50;
	private static final int TURRET_MAX_LEVEL = 2;
	private int projectileDamage, projectileSpeed;

	/**
	 * Constructor for the TurretTower class. Calls the constructor for the DefenseObject class and sets the base
	 *  stats for projectile damage and speed
	 * @param xPos position of the tower in the x axis of the game map
	 * @param yPos position of the tower in the y axis of the game map
	 */
	public TurretTower(int xPos, int yPos){
		super(RELOAD_TIME, TURRET_HP, xPos, yPos, TURRET_RADIUS);
		projectileDamage = 4; projectileSpeed = 10;

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
			LevelScreen.gameElems.addProjectile( new TurretProjectile(this.getCenterX(), this.getCenterY(), 
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
		if(level == 1)
			return Assets.turretTowerLevel1;
		else
			return Assets.turretTowerLevel2;
	}
	
	/**
	 * Indicates whether this tower can be upgraded or not
	 * 
	 * @return returns true if it can be upgraded, false otherwise
	 */
	@Override
	public boolean isUpgradable() {
		return (level < TURRET_MAX_LEVEL);
	}

	/**
	 * Returns the cost to upgrade the tower
	 * 
	 * @return returns the necessary credit to upgrade this tower
	 */
	@Override
	public int getUpgradeCost() {
		return level*200;
	}

	/**
	 * Upgrades the tower as long as the user has enough credits to cover the upgrade.
	 * 
	 * Increases the tower level and base stats as well as the stats for any new projectiles it creates
	 */
	@Override
	public void upgrade() {
		int cost = getUpgradeCost();
		if(LevelScreen.gameElems.currentBalance >= cost){
			LevelScreen.gameElems.currentBalance -= cost;
			level++;
			attackRadius += 50;
			projectileDamage += 2;
			maxHitpoints += 500;
			hitpoints = maxHitpoints;
			projectileSpeed += 5;
			reloadTime -= 0.3f;
		}
	}

	/**
	 * @return returns the cost to build this tower
	 */
	@Override
	public int getCost() {
		return TURRET_COST;
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
		return level*(maxHitpoints-hitpoints);
	}
}
