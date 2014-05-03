package pt.up.fe.lpoo.towerdefense.test;

import java.util.ArrayList;

import pt.up.fe.lpoo.towerdefense.Enemy;
import pt.up.fe.lpoo.towerdefense.LevelScreen;
import pt.up.fe.lpoo.towerdefense.Tile;
import android.test.AndroidTestCase;

public class ProjectileTests  extends AndroidTestCase{
	@Override
	public void setUp(){
	}
	
	@Override
	public void tearDown(){
		
		System.gc();
	}
	//tests creating a projectile and verifying its movement direction
	public void testProjectileCreation(){
		LevelScreen.MAP_HEIGHT = 1000;
		LevelScreen.MAP_WIDTH = 1000;
		TestProjectile testProjectile = new TestProjectile(0, 0, 100, 0, 10, 20);
		assertEquals(testProjectile.getYSpeed(),0);
		assertEquals(testProjectile.getXSpeed(),10);
	}
	//tests if the projectiles move as they should
	public void testProjectileMovement(){
		LevelScreen.MAP_HEIGHT = 1000;
		LevelScreen.MAP_WIDTH = 1000;
		TestProjectile testProjectile = new TestProjectile(0, 0, 100, 0, 10, 20);
		assertEquals(testProjectile.getYSpeed(),0);
		assertEquals(testProjectile.getXSpeed(),10);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		testProjectile.update(2.1f, enemies);
		assertEquals(testProjectile.getCenterX(), 10);
		assertEquals(testProjectile.getCenterY(), 0);
	}
	//tests if the projectiles collide against enemies
	public void testProjectileCollision(){
		LevelScreen.MAP_HEIGHT = 1000;
		LevelScreen.MAP_WIDTH = 1000;
		Tile tile1 = new Tile(0,0, Tile.MONSTER_ENTRANCE); Tile tile2 = new Tile(1,0, Tile.PATH);
		ArrayList<Tile> path = new ArrayList<Tile>();
		path.add(tile1);path.add(tile2);
		TestProjectile testProjectile = new TestProjectile(1, 40, 100, 0, 10, 20);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		TestEnemy enemy = new TestEnemy(path);
		enemies.add(enemy);
		testProjectile.update(2.1f, enemies);
		assertTrue(testProjectile.toRemove);
		assertTrue(enemy.wasAttacked);
	}


}
