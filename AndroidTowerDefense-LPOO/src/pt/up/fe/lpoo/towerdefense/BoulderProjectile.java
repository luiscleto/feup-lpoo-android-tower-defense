package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class is the type of projectile fired by the BoulderTower defense
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see Projectile
 * @see BoulderTower
 */
public class BoulderProjectile extends Projectile {
	
	/**
	 * Constructor for the BoulderProjectile class. Calls the superclass's constructor and sets the damage type
	 * @param iniX original x position
	 * @param iniY original y position
	 * @param goalX destination x position
	 * @param goalY destionation y position
	 * @param movementSpeed speed at which the projectile travels (number of pixels it travels 50 times every second) 
	 * @param projectileDamage base damage the projectile inflicts on an enemy
	 */
	public BoulderProjectile(int iniX, int iniY, int goalX, int goalY, int movementSpeed, int projectileDamage) {
		super(iniX, iniY, goalX, goalY, movementSpeed, projectileDamage);
		damageType = Enemy.DamageType.Crushing;
		
	}
	/**
	 * @return returns the current Image to represent the projectile
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.boulderProjectile;
	}

}