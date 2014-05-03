package pt.up.fe.lpoo.towerdefense.test;

import java.util.ArrayList;

import pt.up.fe.lpoo.towerdefense.Enemy;
import pt.up.fe.lpoo.towerdefense.GameElements;
import pt.up.fe.lpoo.towerdefense.LevelScreen;
import pt.up.fe.lpoo.towerdefense.Skull;
import pt.up.fe.lpoo.towerdefense.Golem;
import pt.up.fe.lpoo.towerdefense.Slime;
import pt.up.fe.lpoo.towerdefense.StatusEffect;
import pt.up.fe.lpoo.towerdefense.Tile;
import pt.up.fe.lpoo.towerdefense.TowerDefenseGame;

import android.test.AndroidTestCase;

public class EnemyAndEffectsUnitTests extends AndroidTestCase{
	@Override
	public void setUp(){
	}
	
	@Override
	public void tearDown(){
		
		System.gc();
	}
	//tests the slime damaging system
	public void testSlimeDamage(){
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(40,40, Tile.MONSTER_ENTRANCE));
		Enemy slime = new Slime(path);
		int totalSlimeHealth = slime.getHealth();
		slime.damage(2,Enemy.DamageType.Piercing);
		assertEquals(slime.getHealth(),totalSlimeHealth-2/2);
		
		totalSlimeHealth = slime.getHealth();
		slime.damage(2,Enemy.DamageType.Magic);
		assertEquals(slime.getHealth(),totalSlimeHealth-2/1);
		
		totalSlimeHealth = slime.getHealth();
		slime.damage(2,Enemy.DamageType.Crushing);
		assertEquals(slime.getHealth(),totalSlimeHealth-2/1);
	}
	//tests the skull damaging system
	public void testSkullDamage(){
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(40,40, Tile.MONSTER_ENTRANCE));
		Enemy skull = new Skull(path);
		
		int totalSkullHealth = skull.getHealth();
		skull.damage(2,Enemy.DamageType.Piercing);
		assertEquals(skull.getHealth(),totalSkullHealth-2/1);
		
		totalSkullHealth = skull.getHealth();
		skull.damage(2,Enemy.DamageType.Magic);
		assertEquals(skull.getHealth(),totalSkullHealth-2/1);
		
		totalSkullHealth = skull.getHealth();
		skull.damage(2,Enemy.DamageType.Crushing);
		assertEquals(skull.getHealth(),totalSkullHealth-2/2);
		
	}
	//tests the golem damaging system
	public void testGolemDamage(){
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(40,40, Tile.MONSTER_ENTRANCE));
		Enemy golem = new Golem(path);
		
		int totalGolemHealth = golem.getHealth();
		golem.damage(2,Enemy.DamageType.Piercing);
		assertEquals(golem.getHealth(),totalGolemHealth-2/2);
		
		totalGolemHealth = golem.getHealth();
		golem.damage(2,Enemy.DamageType.Magic);
		assertEquals(golem.getHealth(),totalGolemHealth-2/2);
		
		totalGolemHealth = golem.getHealth();
		golem.damage(2,Enemy.DamageType.Crushing);
		assertEquals(golem.getHealth(),totalGolemHealth-2/1);
		
	}
	//tests if player receives the proper rewards for an enemy death
	public void testEnemyDeathMoneyAndScore(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(Tile.TILE_SIDE,0, Tile.PATH));
		Enemy slime = new Slime(path);
		
		int iniScore = LevelScreen.gameElems.curScore;
		int iniBalance = LevelScreen.gameElems.currentBalance;
		
		slime.damage(slime.getHealth()*2, Enemy.DamageType.Piercing);
		assertTrue(slime.isDead());
		
		assertEquals(LevelScreen.gameElems.currentBalance,iniBalance+10);
		assertEquals(LevelScreen.gameElems.curScore,iniScore+20);
	}
	//tests the enemy's movement along a pre-determined path
	public void testEnemyMovement(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();

		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(1,0, Tile.PATH));
		Enemy slime = new TestEnemy(path);
		
		int iniX = slime.getCenterX();
		int iniY = slime.getCenterY();
		
		slime.update(2.1f);
		
		assertEquals(slime.getCenterX(),iniX + TestEnemy.SLIME_SPEED);
		assertEquals(slime.getCenterY(),iniY);
	}
	//tests if enemies complete their path and "damage" the player
	public void testEnemyWin(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();

		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(1,0, Tile.PATH));
		Enemy slime = new TestEnemy(path);
		
		slime.update(1000);
		
		assertEquals(slime.getCenterX(),Tile.TILE_SIDE+Tile.TILE_SIDE/2);
		assertTrue(slime.toRemove);
		assertEquals(TowerDefenseGame.MAX_LIVES-1,LevelScreen.gameElems.livesLeft);
	}
	//tests status effects on enemies
	public void testStatusEffect(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();

		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(1,0, Tile.PATH));
		Enemy slime = new TestEnemy(path);
		slime.addStatusEffect(new StatusEffect(StatusEffect.EffectType.Frozen, 2));
		assertTrue(slime.hasStatusEffect(StatusEffect.EffectType.Frozen));
		slime.updateStatusEffects(2000);
		assertFalse(slime.hasStatusEffect(StatusEffect.EffectType.Frozen));
	}
	//tests damage caused by status effects on enemies
	public void testStatusEffectDamage(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();

		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(1,0, Tile.PATH));
		Enemy slime = new TestEnemy(path);
		slime.addStatusEffect(new StatusEffect(StatusEffect.EffectType.Poisoned, 2,4));
		assertTrue(slime.hasStatusEffect(StatusEffect.EffectType.Poisoned));
		int iniHealth = slime.getHealth();
		slime.updateStatusEffects(2000);
		assertEquals(iniHealth-(4*2)/TestEnemy.SLIME_MAGIC_ARMOR,slime.getHealth());
		assertFalse(slime.hasStatusEffect(StatusEffect.EffectType.Poisoned));
	}
	//tests status effects slowing down effects on enemies
	public void testStatusEffectSpeedChange(){
		LevelScreen.gameElems = new GameElements(0);
		ArrayList<Tile> path = new ArrayList<Tile>();

		path.add(new Tile(0,0, Tile.MONSTER_ENTRANCE));path.add(new Tile(1,0, Tile.PATH));
		Enemy slime = new TestEnemy(path);
		slime.addStatusEffect(new StatusEffect(StatusEffect.EffectType.Slowed, 5f,5.0));
		assertTrue(slime.hasStatusEffect(StatusEffect.EffectType.Slowed));
		
		slime.updateStatusEffects(2);
		assertEquals(5.0,slime.getReactionTimeModifier(),0.001);
	}
}
