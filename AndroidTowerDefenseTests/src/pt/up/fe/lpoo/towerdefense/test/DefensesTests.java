package pt.up.fe.lpoo.towerdefense.test;

import java.util.ArrayList;

import pt.up.fe.lpoo.towerdefense.DefenseObject;
import pt.up.fe.lpoo.towerdefense.Enemy;
import pt.up.fe.lpoo.towerdefense.GameElements;
import pt.up.fe.lpoo.towerdefense.LevelScreen;
import pt.up.fe.lpoo.towerdefense.Tile;
import android.test.AndroidTestCase;

public class DefensesTests  extends AndroidTestCase{
	@Override
	public void setUp(){
	}
	
	@Override
	public void tearDown(){
		
		System.gc();
	}
	
	//creates tiles and attempts to create a defense and add it to a tile
	public void testDefenseCreation(){
		LevelScreen.gameElems = new GameElements(0);
		Tile tile1 = new Tile(0,0, Tile.MONSTER_ENTRANCE); Tile tile2 = new Tile(1,0, Tile.PATH);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(tile1);path.add(tile2);
		
		DefenseObject def = new TestDefense(0,0);
		tile1.buildDefense(def);
		assertTrue(tile1.hasDefenseObject());
		assertEquals(tile1.getDefenseObject(), def);
	}
	
	//attempts to destroy a defense (by damaging it using the tile destroyDefense function)
	public void testDefenseDestruction(){
		LevelScreen.gameElems = new GameElements(0);
		Tile tile1 = new Tile(0,0, Tile.MONSTER_ENTRANCE); Tile tile2 = new Tile(1,0, Tile.PATH);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(tile1);path.add(tile2);
		
		DefenseObject def = new TestDefense(0,0);
		tile1.buildDefense(def);
		assertTrue(tile1.hasDefenseObject());
		assertEquals(tile1.getDefenseObject(), def);
		
		tile1.destroyDefense();
		assertTrue(def.toRemove);
	}
	//tests if a defense attacks an enemy in range
	public void testDefenseObjectAttack(){
		LevelScreen.gameElems = new GameElements(0);
		Tile tile1 = new Tile(0,0, Tile.MONSTER_ENTRANCE); Tile tile2 = new Tile(1,0, Tile.PATH);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(tile1);path.add(tile2);
		
		Enemy slime = new TestEnemy(path);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>(); enemies.add(slime);
		TestDefense def = new TestDefense(0,0);
		tile2.buildDefense(def);

		def.update(TestDefense.RELOAD_TIME*101.0f, enemies);
		assertTrue(def.attacked);
	}
	//tests if a defense does not attack an enemy out of its radius
	public void testEnemyOutOfRadius(){
		LevelScreen.gameElems = new GameElements(0);
		Tile tile1 = new Tile(0,0, Tile.MONSTER_ENTRANCE); Tile tile2 = new Tile(1,0, Tile.PATH);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(tile1);path.add(tile2);
		
		Enemy slime = new TestEnemy(path);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>(); enemies.add(slime);
		TestDefense def = new TestDefense(1000,1000);
		tile2.buildDefense(def);

		def.update(TestDefense.RELOAD_TIME*101.0f, enemies);
		assertFalse(def.attacked);
	}
	//tests upgrading a defense to its max level and if its not upgradable anymore after
	public void testUpgrade(){
		LevelScreen.gameElems = new GameElements(0);
		TestDefense def = new TestDefense(1000,1000);
		while(def.getLevel() < TestDefense.TURRET_MAX_LEVEL){
			LevelScreen.gameElems.currentBalance = def.getUpgradeCost();
			assertTrue(def.isUpgradable());
			def.upgrade();
		}
		assertFalse(def.isUpgradable());
		assertEquals(def.getLevel(),TestDefense.TURRET_MAX_LEVEL);
	}
	//tests damaging the tower
	public void testDamage(){
		TestDefense def = new TestDefense(1000,1000);
		def.receiveDamage(20);
		assertEquals(TestDefense.TURRET_HP-20,def.getHitpoints());
	}
}
