package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;
import pt.up.fe.lpoo.framework.Image;

/**
 * This class represents the Barricade defense object. It blocks enemy's paths, making them either go around it
 *  when possible, or attack it.
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see DefenseObject
 */
public class BarricadeDefense extends DefenseObject{
	private static final int BARRICADE_HP = 100;
	private static final float RELOAD_TIME = 2f;
	private static final double BARRICADE_RADIUS = 0.0;
	/** Cost of building a barricade defense */
	public static final int BARRICADE_COST = 20;

	/**
	 * Constructor for the BarricadeDefense class. Calls the constructor for the DefenseObject class
	 * @param xPos position of the defense in the x axis of the game map
	 * @param yPos position of the defense in the y axis of the game map
	 */
	public BarricadeDefense(int xPos, int yPos){
		super(RELOAD_TIME, BARRICADE_HP, xPos, yPos, BARRICADE_RADIUS);
	}

	@Override
	protected void attack(ArrayList<Enemy> enemies) {
		foundEnemies = true;
	}

	/**
	 * Returns the current Image to represent the tar pool.
	 * 
	 * @return returns the Image object corresponding the current image of the tar pool
	 */
	@Override
	public Image getCurrentImage() {
		return Assets.barricade;
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
		return BARRICADE_COST;
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
	 * Indicates whether this defense is part of the ground terrain or if it has a height (for visibility purposes)
	 * 
	 * @return returns true if the defense is at ground level, false otherwise
	 */
	@Override
	public boolean isGroundObject() {
		return false;
	}

	/**
	 * @return returns the cost required to repair the defense
	 */
	@Override
	public int getRepairCost() {
		return BARRICADE_HP-hitpoints;
	}
}
