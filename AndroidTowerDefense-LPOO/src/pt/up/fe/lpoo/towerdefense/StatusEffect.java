package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Image;

/**
 * This class contains the information relative to status effects that may affect game objects with several diverse
 *  consequences
 * @author Joao Marinheiro
 * @author Luis Cleto
 *
 */
public class StatusEffect {
	public static final int EFFECT_IMAGE_SIDE = 50; /**< Side occupied by the sprite of a status effect */
	/**
	 * Contains the different types of status effect that are implemented
	 */
	public enum EffectType{
		Frozen, Poisoned, Slowed
	}
	/** Type of this status effect */
	public EffectType type;
	/** Damage type of this status effect */
	public Enemy.DamageType damageType;
	private float duration;
	private float elapsedSeconds;
	/** Signals whether this status effect has finished or not */
	public boolean toRemove = false;
	private int damagePerSecond = 0;
	private int secondOfLastDamage = 0;
	private double reactionTimeModifier = 1.0;
	
	/**
	 * Constructor for a status effect that does not deal damage or affect the enemies reaction time
	 * @param type type of effect
	 * @param duration duration of the effect (seconds)
	 */
	public StatusEffect(EffectType type, float duration){
		this.type = type;
		this.duration = duration;

		if(type == EffectType.Poisoned)
			damageType = Enemy.DamageType.Magic;
		else
			damageType = Enemy.DamageType.Piercing;
	}
	/**
	 * Constructor for a status effect that does not deal damage but affects the enemies reaction time
	 * @param type type of effect
	 * @param duration duration of the effect (seconds)
	 * @param reactionTimeModifier multiplier for the affected enemies' reaction time
	 */
	public StatusEffect(EffectType type, float duration, double reactionTimeModifier){
		this.type = type;
		this.duration = duration;
		this.reactionTimeModifier = reactionTimeModifier;

		if(type == EffectType.Poisoned)
			damageType = Enemy.DamageType.Magic;
		else
			damageType = Enemy.DamageType.Piercing;
	}
	/**
	 * Constructor for a status effect that deals damage
	 * @param type type of effect
	 * @param duration duration of the effect (seconds)
	 * @param dps damage dealt per second
	 */
	public StatusEffect(EffectType type, float duration, int dps){
		this.type = type;
		this.duration = duration;
		this.damagePerSecond = dps;

		if(type == EffectType.Poisoned)
			damageType = Enemy.DamageType.Magic;
		else
			damageType = Enemy.DamageType.Piercing;
	}
	/**
	 * @return returns the image to represent the status effect on the enemy
	 */
	public Image getImage() {
		if(type == EffectType.Frozen)
			return Assets.frozenEffect;
		else if(type == EffectType.Poisoned)
			return Assets.poisonEffect;
		else if(type == EffectType.Slowed)
			return Assets.slowEffect;
		else
			return Assets.frozenEffect;
	}
	
	/**
	 * updates the status effects variables
	 * @param deltaTime elapsed time since last update
	 */
	public void update(float deltaTime){
		if(toRemove)
			return;
		elapsedSeconds += (deltaTime/100.0f);
		if(elapsedSeconds > duration)
			toRemove = true;
	}
	/**
	 * @return returns the time remaining for the status effect
	 */
	public float getRemainingTime(){
		return duration-elapsedSeconds;
	}
	
	/**
	 * @return returns the damage to be dealt by the status effect 
	 */
	public int getDamage(){
		if(damagePerSecond == 0)
			return 0;
		int damage = 0;
		while(elapsedSeconds >= secondOfLastDamage+1 && secondOfLastDamage < (int)duration){
			secondOfLastDamage++;
			damage+=damagePerSecond;
		}
		return damage;
	}
	
	/**
	 * @return returns the reaction time modifier caused by the status effect
	 */
	public double getReactionTimeModifier(){
		return reactionTimeModifier;
	}
	/**
	 * @return returns the damage type inflicted by a status effect object
	 */
	public Enemy.DamageType getDamageType()
	{
		return damageType;
	}
}
