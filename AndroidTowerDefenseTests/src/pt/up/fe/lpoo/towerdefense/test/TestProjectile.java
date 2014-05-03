package pt.up.fe.lpoo.towerdefense.test;

import pt.up.fe.lpoo.framework.Image;
import pt.up.fe.lpoo.towerdefense.Projectile;
import pt.up.fe.lpoo.towerdefense.Enemy;

public class TestProjectile extends Projectile {
	
	public TestProjectile(int iniX, int iniY, int goalX, int goalY, int movementSpeed, int projectileDamage) {
		super(iniX, iniY, goalX, goalY, movementSpeed, projectileDamage);
		damageType = Enemy.DamageType.Piercing;
		
	}
	@Override
	public Image getCurrentImage() {
		return null;
	}
	
	public int getYSpeed(){
		return ySpeed;
	}
	public int getXSpeed(){
		return xSpeed;
	}
	
	@Override
	protected boolean collidedWith(Enemy curEnemy) {
		int enemyIniX = curEnemy.getX(); int enemyIniY = curEnemy.getY();
		int enemyFinalX = enemyIniX+50;
		int enemyFinalY = enemyIniY+50;

		if(centerX >= enemyIniX && centerX <= enemyFinalX && centerY >= enemyIniY && centerY <= enemyFinalY)
			return true;
		else
			return false;
	}

}
