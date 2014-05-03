package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the poison tower defense object. It has a large cooldown and with every attack it gives all
 *  surrounding enemies the poisoned status effect
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class PoisonTower extends DefenseObject{
	private static final float POISON_DURATION = 5.0f;
	private static final int POISON_TOWER_HP = 700;
	private static final float RELOAD_TIME = 10.0f;
	private static final double POISON_RADIUS = 150.0;
	private static final int POISON_DPS = 4;
	/** cost for building this defense */
	public static final int POISON_TOWER_COST = 70;

	/**
	 * Constructor for the PoisonTower class. Only uses the superclass constructor.
	 * @param xPos x position for the tower
	 * @param yPos y position for the tower
	 */
	public PoisonTower(int xPos, int yPos){
		super(RELOAD_TIME, POISON_TOWER_HP, xPos, yPos, POISON_RADIUS);
	}

	@Override
	protected void attack(ArrayList<Enemy> enemies) {
		for(int i=0; i < enemies.size(); i++){
			if(isWithinRadius(enemies.get(i))){
				if(!enemies.get(i).hasStatusEffect(StatusEffect.EffectType.Poisoned)){
					enemies.get(i).addStatusEffect(
							new StatusEffect(StatusEffect.EffectType.Poisoned,POISON_DURATION,POISON_DPS));
					foundEnemies = true;
				}
			}
		}
	}

	/**
	 * Returns the current Image to represent the tower.
	 * 
	 * @return returns the Image object corresponding the current image of the tower
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.poisonTower;
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
	 * Only a stub since this tower can't be upgraded yet
	 */
	@Override
	public void upgrade() {
	}

	/**
	 * @return returns the cost to build this tower
	 */
	@Override
	public int getCost() {
		return POISON_TOWER_COST;
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
		return 2*(POISON_TOWER_HP-hitpoints);
	}
}
