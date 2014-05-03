package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class contains game objects to be used for a level as well as other game variables such as the score,
 *  money and number of lives left
 *  
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class GameElements {
	/** current amount of money the user has */
	public int currentBalance;
	/** current amount of lives the user has left */
	public int livesLeft;
	/** current score the user has achieved */
	public int curScore; 
	/** array with the enemies in the level */
	public ArrayList<Enemy> enemies;
	/** array with the defenses in the level */
	public ArrayList<DefenseObject> defenses;
	/** array with the projectiles in the level */
	public ArrayList<Projectile> projectiles;
	
	/**
	 * Constructor for the GameElemnts class. Initializes the game variables
	 * @param chosenLevel level being played
	 */
	public GameElements(int chosenLevel){
		enemies = new ArrayList<Enemy>();
		defenses = new ArrayList<DefenseObject>();
		projectiles = new ArrayList<Projectile>();

		livesLeft = TowerDefenseGame.MAX_LIVES;
		curScore = 0;
		currentBalance = 100 + chosenLevel*50;
	}
	/**
	 * Adds an enemy to the game elements
	 * @param enemy Enemy to be added
	 */
	public void addEnemy(Enemy enemy){
		enemies.add(enemy);
	}
	/**
	 * Adds a defense to the game elements
	 * @param def DefenseObject to be added
	 */
	public void addDefense(DefenseObject def){
		defenses.add(def);
	}
	/**
	 * Adds a projectile to the game elements
	 * @param proj Projectile to be added
	 */
	public void addProjectile(Projectile proj){
		projectiles.add(proj);
	}
	/**
	 * Sorts the game elements into a priority queue and returns it for drawing
	 * @return returns a priority queue with all the game objects to be drawn
	 */
	public PriorityQueue<GameObj> getGameObjsForDrawing(){
		PriorityQueue<GameObj> queue = new PriorityQueue<GameObj>(Math.max(defenses.size()+enemies.size()+projectiles.size(),1));
		for(int i=0; i < defenses.size(); i++)
			queue.add(defenses.get(i));
		for(int i=0; i < enemies.size(); i++)
			queue.add(enemies.get(i));
		for(int i=0; i < projectiles.size(); i++)
			queue.add(projectiles.get(i));
		
		return queue;
	}
}
