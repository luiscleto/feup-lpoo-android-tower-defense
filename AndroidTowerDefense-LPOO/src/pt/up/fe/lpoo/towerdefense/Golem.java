package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class is a type of enemy characterized by being a slow and large rock golem and resists
 *  all attacks except for crushing.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see Enemy
 */
public class Golem extends Enemy {
	private static final int GOLEM_MONEY_WORTH = 25;
	private static final int GOLEM_SCORE_WORTH = 100;
	private static final int GOLEM_SPEED = 1;
	private static final int GOLEM_HEALTH = 100;
	private static final int GOLEM_DAMAGE = 20;
	private static final int GOLEM_PIERCING_ARMOR = 2;
	private static final int GOLEM_CRUSHING_ARMOR = 1;
	private static final int GOLEM_MAGIC_ARMOR = 2;
	private static final float GOLEM_ATTACK_COOLDOWN = 1.0f;
	/** weights for tile types (to be used by the pathfinding algorithm) sorted by TowerTerrain, Path, entrance, goal */
	public static final Float[] tileWeights = {10000f,1f,5f,10f};
	/** indicator for the skull enemy type */
	public static final int enemyType = 3;
	private Animation curAnimation;
	
	private long animTime;
	
	/**
	 * Constructor for Golem objects. Uses the superclass's constructor with the golem base values
	 * @param path path the skull will follow
	 */
	public Golem(ArrayList<Tile> path){
		super(GOLEM_HEALTH,GOLEM_SPEED, GOLEM_SCORE_WORTH, GOLEM_MONEY_WORTH, path, GOLEM_DAMAGE, GOLEM_ATTACK_COOLDOWN);
		animTime = 0;
		piercingArmor = GOLEM_PIERCING_ARMOR;
		crushingArmor = GOLEM_CRUSHING_ARMOR;
		magicArmor = GOLEM_MAGIC_ARMOR;
		updateAnimation();
	}
	
	
	private void updateAnimation(){
		if(speedX > 0)
			curAnimation = Assets.golemRightMovement;
		else if(speedX < 0)
			curAnimation = Assets.golemLeftMovement;
		else if(speedY > 0)
			curAnimation = Assets.golemDownMovement;
		else
			curAnimation = Assets.golemUpMovement;
	}
	
	/**
	 * Uses the superclass update function and updates the golem's animation
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
	 * Checks the movement direction for the golem and updates the animation
	 * 
	 * @return returns the frame of the animation corresponding to the actual image of the golem
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
		return false;
	}

}
