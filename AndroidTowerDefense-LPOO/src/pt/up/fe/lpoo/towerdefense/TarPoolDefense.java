package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the Tar Pool defense object. It does not block the enemies' paths (although they will go
 *  around it if its easy to do so) and gives all enemies on it a slow effect
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class TarPoolDefense extends DefenseObject{
	private static final double SLOW_REACTION_TIME_MODIFIER = 5.0;
	private static final float SLOW_DURATION = 5f;
	private static final int TARPOOL_HP = 100;
	private static final float RELOAD_TIME = 0.1f;
	private static final double TARPOOL_RADIUS = Tile.TILE_SIDE/2;
	/** Cost to build this defense object */
	public static final int TARPOOL_DEFENSE_COST = 50;

	/**
	 * Constructor for the TarPoolDefense class. Calls the constructor for the DefenseObject class
	 * @param xPos position of the defense in the x axis of the game map
	 * @param yPos position of the defense in the y axis of the game map
	 */
	public TarPoolDefense(int xPos, int yPos){
		super(RELOAD_TIME, TARPOOL_HP, xPos, yPos, TARPOOL_RADIUS);
	}

	@Override
	protected void attack(ArrayList<Enemy> enemies) {
		for(int i=0; i < enemies.size(); i++){
			if(isWithinRadius(enemies.get(i)) && !enemies.get(i).canFly()){
				if(!enemies.get(i).hasStatusEffect(StatusEffect.EffectType.Slowed)){
					enemies.get(i).addStatusEffect(
							new StatusEffect(StatusEffect.EffectType.Slowed,SLOW_DURATION,SLOW_REACTION_TIME_MODIFIER));
					foundEnemies = true;
				}
				else
					enemies.get(i).addStatusEffect(
							new StatusEffect(StatusEffect.EffectType.Slowed,SLOW_DURATION,SLOW_REACTION_TIME_MODIFIER));

			}
		}
	}

	/**
	 * Returns the current Image to represent the tar pool.
	 * 
	 * @return returns the Image object corresponding the current image of the tar pool
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.tarPool;
	}
	/**
	 * Indicates whether this defense object can be upgraded or not
	 * 
	 * @return returns true if it can be upgraded, false otherwise
	 */
	@Override
	public boolean isUpgradable() {
		return false;
	}
	/**
	 * Returns the cost to upgrade the defense
	 * 
	 * @return returns the necessary credit to upgrade this defense
	 */
	@Override
	public int getUpgradeCost() {
		return 0;
	}

	/**
	 * Merely a stub since this defense can not be upgraded yet
	 */
	@Override
	public void upgrade() {
	}

	/**
	 * Returns the cost to build this defense
	 */
	@Override
	public int getCost() {
		return TARPOOL_DEFENSE_COST;
	}
	
	/**
	 * Indicates whether this defense will block an enemies path or not
	 * 
	 * @return returns false if enemies cant walk across this defense, true otherwise
	 */
	@Override
	public boolean isCrossable() {
		return true;
	}
	/**
	 * Indicates whether this defense is part of the ground terrain or if it has a height (for visibility purposes)
	 * 
	 * @return returns true if the defense is at ground level, false otherwise
	 */
	@Override
	public boolean isGroundObject() {
		return true;
	}
	/**
	 * @return returns the cost required to repair the defense
	 */
	@Override
	public int getRepairCost() {
		return TARPOOL_HP-hitpoints;
	}
}
