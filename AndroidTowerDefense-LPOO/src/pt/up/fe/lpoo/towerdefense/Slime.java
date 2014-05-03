package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class is a type of enemy characterized by being a weak red blob that moves fast and resists piercing attacks.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see Enemy
 */
public class Slime extends Enemy {
	private static final float SLIME_ATTACK_COOLDOWN = 1.0f;
	private static final int SLIME_HEALTH = 12;
	private static final int SLIME_SPEED = 2;
	private static final int SLIME_SCORE_WORTH = 20;
	private static final int SLIME_MONEY_WORTH = 10;
	private static final int SLIME_DAMAGE = 5;
	private static final int SLIME_PIERCING_ARMOR = 2;
	private static final int SLIME_CRUSHING_ARMOR = 1;
	private static final int SLIME_MAGIC_ARMOR = 1;
	/** weights for tile types (to be used by the pathfinding algorithm) sorted by TowerTerrain, Path, entrance, goal */
	public static final Float[] tileWeights = {10000f,1f,5f,10f};
	/** indicator for the slime enemy type */
	public static final int enemyType = 1;
	
	
	private long animTime;
	
	/**
	 * Constructor for Slime objects. Uses the superclass's constructor with the slime base values
	 * @param path path the slime will follow
	 */
	public Slime(ArrayList<Tile> path){
		super(SLIME_HEALTH,SLIME_SPEED, SLIME_SCORE_WORTH, SLIME_MONEY_WORTH, path, SLIME_DAMAGE, SLIME_ATTACK_COOLDOWN);
		animTime = 0;
		piercingArmor = SLIME_PIERCING_ARMOR;
		crushingArmor = SLIME_CRUSHING_ARMOR;
		magicArmor = SLIME_MAGIC_ARMOR;
	}
	
	/**
	 * Uses the superclass update function and updates the slime's animation
	 * 
	 * @param deltaTime elapsed time since last update
	 */
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(hasStatusEffect(StatusEffect.EffectType.Frozen))
			return;
		animTime = animTime+(int)(deltaTime*10.0f);
		if(animTime > Assets.slimeMovement.getTotalDuration())
			animTime = animTime % Assets.slimeMovement.getTotalDuration();
	}
	
	/**
	 * @return returns the current image of the slime animation to be drawn on screen
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.slimeMovement.getImage(animTime);
	}
	/**
	 * @return returns the tile weights for this enemy
	 */
	@Override
	public Float[] getTileWeights() {
		return tileWeights;
	}
	/**
	 * @return returns true if the enemy can fly, false otherwise (always false for slimes)
	 */
	@Override
	public boolean canFly() {
		return false;
	}

}
