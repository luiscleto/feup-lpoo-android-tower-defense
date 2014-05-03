package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class is a type of enemy characterized by being fast, flying flaming skull and resists crushing attacks.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see Enemy
 */
public class Skull extends Enemy {
	private static final int SKULL_MONEY_WORTH = 20;
	private static final int SKULL_SCORE_WORTH = 50;
	private static final int SKULL_SPEED = 2;
	private static final int SKULL_HEALTH = 40;
	private static final int SKULL_DAMAGE = 10;
	private static final int SKULL_PIERCING_ARMOR = 1;
	private static final int SKULL_CRUSHING_ARMOR = 2;
	private static final int SKULL_MAGIC_ARMOR = 1;
	private static final float SKULL_ATTACK_COOLDOWN = 0.5f;
	/** weights for tile types (to be used by the pathfinding algorithm) sorted by TowerTerrain, Path, entrance, goal */
	public static final Float[] tileWeights = {1f,1f,5f,10f};
	/** indicator for the skull enemy type */
	public static final int enemyType = 2;
	private Animation curAnimation;
	
	private long animTime;
	
	/**
	 * Constructor for Skull objects. Uses the superclass's constructor with the skull base values
	 * @param path path the skull will follow
	 */
	public Skull(ArrayList<Tile> path){
		super(SKULL_HEALTH, SKULL_SPEED, SKULL_SCORE_WORTH, SKULL_MONEY_WORTH, path, SKULL_DAMAGE, SKULL_ATTACK_COOLDOWN);
		animTime = 0;
		piercingArmor = SKULL_PIERCING_ARMOR;
		crushingArmor = SKULL_CRUSHING_ARMOR;
		magicArmor = SKULL_MAGIC_ARMOR;
		updateAnimation();
	}
	private void updateAnimation(){
		if(speedX > 0)
			curAnimation = Assets.skullRightMovement;
		else if(speedX < 0)
			curAnimation = Assets.skullLeftMovement;
		else if(speedY > 0)
			curAnimation = Assets.skullDownMovement;
		else
			curAnimation = Assets.skullUpMovement;
	}
	/**
	 * Uses the superclass update function and updates the skull's animation
	 * 
	 * @param deltaTime elapsed time since last update
	 */
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(hasStatusEffect(StatusEffect.EffectType.Frozen))
			return;
		animTime = animTime+(int)(deltaTime*10.0f);
		if(animTime > curAnimation.getTotalDuration())
			animTime = animTime % curAnimation.getTotalDuration();
	}
	/**
	 * Checks the movement direction for the skull and updates the animation
	 * 
	 * @return returns the frame of the animation corresponding to the actual image of the skull
	 */
	@Override
	public Image getCurrentImage() {
		updateAnimation();
		Image curImage = curAnimation.getImage(animTime);
		return curImage;
	}
	
	/**
	 * @return returns the tile weights for this enemy
	 */
	@Override
	public Float[] getTileWeights() {
		return tileWeights;
	}
	
	/**
	 * @return returns true if the enemy can fly, false otherwise (always true for skulls)
	 */
	@Override
	public boolean canFly() {
		return true;
	}

}
