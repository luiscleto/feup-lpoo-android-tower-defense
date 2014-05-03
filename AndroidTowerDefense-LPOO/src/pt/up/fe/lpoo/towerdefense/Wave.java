package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class contains the information regarding the content of enemy waves, organizing them in enemy stacks.
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class Wave {
	private ArrayList<LinkedList<Enemy>> enemyStacks;
	private ArrayList<Integer> stackDeploymentTimes;
	private ArrayList<Integer> unitDelayTimePerStack;
	private ArrayList<Integer> stackElapsedTimes;
	private ArrayList<Boolean> stackStarted;
	private long elapsedTime = 0;
	/** indicates if the wave has been started yet or not */
	public boolean started = false;
	/** indicates if the wave is finished yet or not */
	public boolean finished = false;


	/**
	 * Default constructor for the Wave class. Initializes all variables.
	 */
	public Wave(){
		enemyStacks = new ArrayList<LinkedList<Enemy>>();
		stackDeploymentTimes = new ArrayList<Integer>();
		unitDelayTimePerStack = new ArrayList<Integer>();
		stackElapsedTimes = new ArrayList<Integer>();
		stackStarted = new ArrayList<Boolean>();
	}
	/**
	 * Adds a new stack of enemies to the wave with the given deployment time and delay between each enemy
	 * 
	 * @param enemyStack stack of enemies to be added to the wave
	 * @param deploymentTime time when the stack is supposed to start being deployed after wave start (miliseconds)
	 * @param unitDelay time interval between deploying enemies of the stack (miliseconds)
	 */
	public void addEnemyStack(LinkedList<Enemy> enemyStack, int deploymentTime, int unitDelay){
		enemyStacks.add(enemyStack);
		stackDeploymentTimes.add(deploymentTime);
		unitDelayTimePerStack.add(unitDelay);
		stackElapsedTimes.add(0);
		stackStarted.add(false);
	}
	
	/**
	 * If the wave has started, updates all the stacks of enemies, adding the deployed enemies to the game elements
	 * @param updateTime elapsed time since last update
	 */
	public void update(float updateTime){
		if(started){
			int elapsedMilis = (int)(updateTime*10f);
			elapsedTime += elapsedMilis;
			
			for(int i=0; i < enemyStacks.size(); i++){
				if(elapsedTime >= stackDeploymentTimes.get(i)){
					stackElapsedTimes.set(i, stackElapsedTimes.get(i) + elapsedMilis);
					while((stackElapsedTimes.get(i) >= unitDelayTimePerStack.get(i) || !stackStarted.get(i)) 
							&& !enemyStacks.get(i).isEmpty()){
						if(stackStarted.get(i))
							stackElapsedTimes.set(i, stackElapsedTimes.get(i) - unitDelayTimePerStack.get(i));
						else
							stackStarted.set(i, true);
						LevelScreen.gameElems.addEnemy(enemyStacks.get(i).remove());
					}
				}

			}

			finished = true;
			for(int i=0; i < enemyStacks.size(); i++){
				if(enemyStacks.get(i).size() > 0)
					finished = false;
			}
		}

	}

}
