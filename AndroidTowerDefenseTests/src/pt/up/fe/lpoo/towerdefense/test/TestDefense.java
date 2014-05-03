package pt.up.fe.lpoo.towerdefense.test;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;
import pt.up.fe.lpoo.towerdefense.DefenseObject;
import pt.up.fe.lpoo.towerdefense.Enemy;
import pt.up.fe.lpoo.towerdefense.LevelScreen;

public class TestDefense extends DefenseObject{
	public static final int TURRET_HP = 500;
	public static final float RELOAD_TIME = 1f;
	public static final double TURRET_RADIUS = 200.0;
	public static final int TURRET_COST = 50;
	public static final int TURRET_MAX_LEVEL = 2;
	//private int projectileDamage, projectileSpeed;
	public boolean attacked = false;

	public TestDefense(int xPos, int yPos){
		super(RELOAD_TIME, TURRET_HP, xPos, yPos, TURRET_RADIUS);
	}

	@Override
	public void attack(ArrayList<Enemy> enemies) {
		for(int i=0; i < enemies.size(); i++){
			if(isWithinRadius(enemies.get(i))){
				attacked = true;
				foundEnemies = true;
				break;
			}
		}
	}

	@Override
	public float getTileHeight(){
		return 1.0f;
	}
	@Override
	public Image getCurrentImage() {
		return null;
	}

	@Override
	public boolean isUpgradable() {
		return (level < TURRET_MAX_LEVEL);
	}

	@Override
	public int getUpgradeCost() {
		return level*200;
	}

	public int getLevel(){
		return level;
	}
	
	@Override
	public void upgrade() {
		int cost = getUpgradeCost();
		if(LevelScreen.gameElems.currentBalance >= cost){
			LevelScreen.gameElems.currentBalance -= cost;
			level++;
			attackRadius += 50;
			hitpoints += 500;
			this.reloadTime -= 0.3f;
		}
	}

	@Override
	public int getCost() {
		return TURRET_COST;
	}

	@Override
	public boolean isCrossable() {
		return false;
	}
	
	@Override
	public boolean isGroundObject() {
		return false;
	}

	@Override
	public int getRepairCost() {
		return 0;
	}

}
