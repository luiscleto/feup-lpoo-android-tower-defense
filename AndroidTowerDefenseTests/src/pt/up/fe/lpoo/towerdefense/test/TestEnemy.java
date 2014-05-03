package pt.up.fe.lpoo.towerdefense.test;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;
import pt.up.fe.lpoo.towerdefense.Enemy;
import pt.up.fe.lpoo.towerdefense.Tile;

public class TestEnemy extends Enemy {
	private static final float SLIME_ATTACK_COOLDOWN = 1.0f;
	private static final int SLIME_HEALTH = 12;
	public static final int SLIME_SPEED = 2;
	public static final int SLIME_SCORE_WORTH = 20;
	public static final int SLIME_MONEY_WORTH = 10;
	public static final int SLIME_DAMAGE = 5;
	public static final int SLIME_PIERCING_ARMOR = 2;
	public static final int SLIME_CRUSHING_ARMOR = 1;
	public static final int SLIME_MAGIC_ARMOR = 1;
	public static final Float[] tileWeights = {10000f,1f,5f,10f};
	public static final int enemyType = 1;
	public boolean wasAttacked = false;
	
	long animTime;
	public TestEnemy(ArrayList<Tile> path){
		super(SLIME_HEALTH,SLIME_SPEED, SLIME_SCORE_WORTH, SLIME_MONEY_WORTH, path, SLIME_DAMAGE, SLIME_ATTACK_COOLDOWN);
		animTime = 0;
		piercingArmor = SLIME_PIERCING_ARMOR;
		crushingArmor = SLIME_CRUSHING_ARMOR;
		magicArmor = SLIME_MAGIC_ARMOR;
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
	}
	
	@Override
	public Image getCurrentImage() {
		return null;
	}
	@Override
	public void damage(int damageHit, DamageType type) {
		wasAttacked = true;
		super.damage(damageHit, type);
	}
	@Override
	public Float[] getTileWeights() {
		return tileWeights;
	}
	@Override
	public float getTileHeight(){
		return 1.0f;
	}
	@Override
	public boolean canFly() {
		return false;
	}

}
