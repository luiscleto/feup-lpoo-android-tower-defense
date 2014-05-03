package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

/**
 * This class contains the information of every path used collectively by enemies in the game
 *  as well as methods to create new paths
 *  
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class PathCluster {
	private ArrayList<PathInfo> paths;

	/**
	 * Default constructor for the PathCluster class. Initializes the paths array
	 */
	public PathCluster(){
		paths = new ArrayList<PathInfo>();
	}

	/**
	 * Adds a new path to the path bank
	 * @param path path to be added to the path bank
	 * @param xOrig starting x position of the path
	 * @param yOrig starting y position of the path
	 * @param xDest final x position of the path
	 * @param yDest final y position of the path
	 * @param enemyType enemy type for which the path was calculated (different enemies have different optimal paths)
	 */
	public void addPath(ArrayList<Tile> path, int xOrig, int yOrig, int xDest, int yDest, int enemyType){
		paths.add(new PathInfo(path,xOrig,yOrig,xDest,yDest, enemyType));
	}

	/**
	 * Searches for a path between the two given points for the given enemy type
	 * @param xOrig starting x position for the path
	 * @param yOrig starting y position for the path
	 * @param xDest final x position for the path
	 * @param yDest final y position for the path
	 * @param enemyType enemy type for which the path was calculated
	 * @return returns the path if it exists, null otherwise
	 */
	public ArrayList<Tile> getPath(int xOrig, int yOrig, int xDest, int yDest, int enemyType){
		for(int i=0; i < paths.size(); i++){
			PathInfo cur = paths.get(i);
			if(xDest == cur.xDest && yDest == cur.yDest && xOrig == cur.xOrig && yOrig == cur.yOrig
					&& enemyType == cur.enemyType)
				return cur.path;
		}
		return null;
	}
	/**
	 * Updates all the paths that may have been affected by a recent alteration to a Tile's status
	 *  (like building/remove a defense object)
	 * @param changedTile the tile that suffered the change
	 * @param tileGrid the map of all the tiles
	 */
	public void recalculatePathsWithTile(Tile changedTile, ArrayList<ArrayList<Tile>> tileGrid){
		for(int i=0; i < paths.size(); i++){
			if(pathContains(paths.get(i).path, changedTile) || !changedTile.hasDefenseObject()){
				Float[] weights;
				switch(paths.get(i).enemyType){
				case Slime.enemyType:
					weights = Slime.tileWeights;
					break;
				case Golem.enemyType:
					weights = Golem.tileWeights;
					break;
				case Skull.enemyType:
					weights = Skull.tileWeights;
					break;
				default:
					weights = Slime.tileWeights;
					break;
				}
				paths.get(i).recalculate(weights, tileGrid);
			}
		}
	}

	private boolean pathContains(ArrayList<Tile> path, Tile changedTile) {
		for(int i=0; i < path.size(); i++){
			if(path.get(i).equals(changedTile))
				return true;
		}
		return false;
	}

	/**
	 * Class for storing the information for a certain path
	 * @author Joao Marinheiro
	 * @author Luis Cleto
	 */
	private class PathInfo{
		private ArrayList<Tile> path;
		private int xOrig, yOrig, xDest, yDest;
		private int enemyType;
		/**
		 * Constructor for the PathInfo object
		 * @param path array of tiles that make up the path
		 * @param xOrig starting x position of the path
		 * @param yOrig starting y position of the path
		 * @param xDest final x position of the path
		 * @param yDest final y position of the path
		 * @param enemyType type of enemy the path was calculated for
		 */
		PathInfo(ArrayList<Tile> path, int xOrig, int yOrig, int xDest, int yDest, int enemyType){
			this.path = path;
			this.xOrig = xOrig;
			this.yOrig = yOrig;
			this.xDest = xDest;
			this.yDest = yDest;
			this.enemyType = enemyType;
		}
		/**
		 * Recalculates the path between the starting and end point of the current path
		 * @param weights tile weights to be used for the new calculation
		 * @param tileGrid grid with all of the map's tiles
		 */
		public void recalculate(Float[] weights, ArrayList<ArrayList<Tile>> tileGrid) {
			ArrayList<Tile> newPath = Enemy.getPathToPoint(path.get(0).getGridX(), path.get(0).getGridY(), 
					path.get(path.size()-1).getGridX(), path.get(path.size()-1).getGridY(), tileGrid, weights);
			int i=0;
			for(;i < newPath.size(); i++){
				if(i < path.size())
					path.set(i, newPath.get(i));
				else
					path.add(newPath.get(i));
			}
			while(i < path.size()){
				path.remove(path.size()-1);
			}
		}
	}
}
