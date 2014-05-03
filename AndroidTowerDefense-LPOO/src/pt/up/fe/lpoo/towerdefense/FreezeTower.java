package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the freeze tower defense object. It has a large cooldown and with every attack it gives all
 *  surrounding enemies the frozen status effect
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class FreezeTower extends DefenseObject{
	private static final float FREEZE_DURATION = 3.0f;
	private static final int FREEZE_TOWER_HP = 700;
	private static final float RELOAD_TIME = 7.0f;
	private static final double FREEZE_RADIUS = 100.0;
	/** cost for building this defense */
	public static final int FREEZE_TOWER_COST = 100;

	/**
	 * Constructor for the FreezeTower class. Only uses the superclass constructor.
	 * @param xPos x position for the tower
	 * @param yPos y position for the tower
	 */
	public FreezeTower(int xPos, int yPos){
		super(RELOAD_TIME, FREEZE_TOWER_HP, xPos, yPos, FREEZE_RADIUS);
	}

	@Override
	protected void attack(ArrayList<Enemy> enemies) {
		for(int i=0; i < enemies.size(); i++){
			if(isWithinRadius(enemies.get(i))){
				if(!enemies.get(i).hasStatusEffect(StatusEffect.EffectType.Frozen)){
					enemies.get(i).addStatusEffect(new StatusEffect(StatusEffect.EffectType.Frozen,FREEZE_DURATION));
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
		return Assets.freezeTower;
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
		return FREEZE_TOWER_COST;
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
		return 3*(FREEZE_TOWER_HP-hitpoints);
	}
}
