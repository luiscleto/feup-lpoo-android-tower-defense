package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * Abstract class to define Projectile objects
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see GameObj
 */
public abstract class Projectile extends GameObj {
	private static final double PROJECTILE_UPDATE_INTERVAL = 0.02;
	protected int centerX, centerY, movementSpeed, projectileDamage;
	protected int targetX, targetY, xSpeed, ySpeed;
	protected Enemy.DamageType damageType;
	/** Indicates if the projectile is to be removed or not */
	public boolean toRemove = false;
	private float elapsedSeconds;

	/**
	 * Constructor for projectile objects.
	 *  Determines the movement in each direction with every time slice for the game.
	 * 
	 * @param iniX initial x position
	 * @param iniY initial y position
	 * @param goalX destination x position
	 * @param goalY destination y position
	 * @param moveSpeed movement speed for the projectile (number of pixels it travels 50 times every second)
	 * @param damage damage the projectile will inflict upon collision with an enemy
	 */
	public Projectile(int iniX, int iniY, int goalX, int goalY, int moveSpeed, int damage){
		this.elapsedSeconds = 0;
		this.centerX = iniX;
		this.centerY = iniY;
		this.movementSpeed = moveSpeed;
		this.projectileDamage = damage;
		double xShift = goalX - iniX; double yShift = goalY-iniY;

		double moveAngle = Math.atan2(yShift, xShift);
		double cosMoveAngle = Math.cos(moveAngle);
		double sinMoveAngle = Math.sin(moveAngle);
		xSpeed = (int) (movementSpeed*cosMoveAngle);
		ySpeed = (int) (movementSpeed*sinMoveAngle);
	}

	/**
	 * Abstract method to determine the sprite of the projectile. Deppends on the subclass.
	 * 
	 * @return returns the image to be drawn to represent the projectile
	 */
	public abstract Image getCurrentImage();
	
	/**
	 * @return returns all the status effects thay on the object
	 */
	@Override
	public ArrayList<StatusEffect> getStatusEffects(){
		return null;
	}
	/**
	 * @return returns the x position of the projectile
	 */
	@Override
	public int getCenterX(){
		return centerX;
	}
	/**
	 * @return returns the x position of the projectile
	 */
	@Override
	public int getCenterY(){
		return centerY;
	}
	/**
	 * @return returns the x position of the upper left corner of the projectile (for drawing)
	 */
	public int getX(){
		return centerX-getCurrentImage().getWidth()/2;
	}
	/**
	 * @return returns the y position of the upper left corner of the projectile (for drawing)
	 */
	public int getY(){
		return centerY-getCurrentImage().getWidth()/2;
	}

	/**
	 * Moves the projectile and check for collision with an enemy. In case of collision, the enemy is damaged
	 *  and the projectile is set to be removed
	 * @param deltaTime elapsed time since last update
	 * @param enemies array of enemies to verify for collision
	 */
	public void update(float deltaTime, ArrayList<Enemy> enemies){
		if(!toRemove){
			elapsedSeconds += (deltaTime/100.0f);
			while(elapsedSeconds > PROJECTILE_UPDATE_INTERVAL){
				elapsedSeconds-=PROJECTILE_UPDATE_INTERVAL;
				centerX += xSpeed;
				centerY += ySpeed;

				if(centerX < 0 || centerX > LevelScreen.MAP_WIDTH || centerY < LevelScreen.INFO_BAR_HEIGHT 
						|| centerY > LevelScreen.INFO_BAR_HEIGHT+LevelScreen.MAP_HEIGHT){
					toRemove = true;
					this.xSpeed = 0;
					this.ySpeed = 0;
					return;
				}

				for(int i=0; i < enemies.size(); i++){
					Enemy curEnemy = enemies.get(i);
					if(collidedWith(curEnemy)){
						curEnemy.damage(projectileDamage, damageType);
						this.toRemove = true;
						this.xSpeed = 0;
						this.ySpeed = 0;
						this.centerX = -100;
						this.centerY = -100;
						break;
					}
				}
			}
		}
	}

	protected boolean collidedWith(Enemy curEnemy) {
		int enemyIniX = curEnemy.getX(); int enemyIniY = curEnemy.getY();
		int enemyFinalX = enemyIniX+curEnemy.getCurrentImage().getWidth();
		int enemyFinalY = enemyIniY+curEnemy.getCurrentImage().getHeight();

		if(centerX >= enemyIniX && centerX <= enemyFinalX && centerY >= enemyIniY && centerY <= enemyFinalY)
			return true;
		else
			return false;
	}
	
	/**
	 * This indicates if the game object is part of the ground terrain or not (for visibility purposes)
	 * 
	 * @return always returns false for projectiles
	 */
	@Override
	public boolean isGroundObject() {
		return false;
	}
}
