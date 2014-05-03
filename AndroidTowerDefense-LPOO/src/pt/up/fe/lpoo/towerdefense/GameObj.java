package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * Super class for all drawable game objects other than tiles.
 *  Implements comparable for visibility calculations purposes.
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public abstract class GameObj implements Comparable<GameObj>{
	/**
	 * @return returns the x position of the center of the object in the map
	 */
	public abstract int getCenterX();
	/**
	 * @return returns the y position of the center of the object in the map
	 */
	public abstract int getCenterY();
	
	/**
	 * Compares two game objects. A game object is 'smaller' than another if it should be drawn before the other
	 *  (deppending on whether any of them is a ground object and their y position)
	 */
	@Override
	public int compareTo(GameObj other){
		if ((getCenterY() < other.getCenterY() && !(other.isGroundObject())) || this.isGroundObject())
        {
            return -1;
        }
        if ((getCenterY() > other.getCenterY() && !(this.isGroundObject())) || other.isGroundObject())
        {
            return 1;
        }
        return 0;
	}
	/**
	 * @return returns true if the object is part of the ground terrain, false otherwise
	 */
	public abstract boolean isGroundObject();
	/**
	 * @return returns the object's current image to be drawn
	 */
	public abstract Image getCurrentImage();
	/**
	 * @return returns any status effects currently affecting the object
	 */
	public abstract ArrayList<StatusEffect> getStatusEffects();
	/**
	 * @return returns the x position of the upper left corner of the object (for drawing)
	 */
	public abstract int getX();
	/**
	 * @return returns the y position of the upper left corner of the object (for drawing)
	 */
	public abstract int getY();
}
