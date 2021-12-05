package edu.wm.cs.cs301.RumitPatel.generation;

import java.util.ArrayList;
import java.util.HashMap;

class MazeBuilderBoruvka extends MazeBuilder implements Runnable{
	/**create a private Hashamp of a hashmap of a hashmap named edgeWeights.
	 * This will be in the format of {x : {y: {CardinalDirection, Integer}}}.
	 * 
	 * Create a public setter named MazeBuilderBoruvka() and inside call super
	 * and set edgeWeights to a new HashMap.
	 * 
	 * The override for the generatePathways method will need helper functions.
	 * 
	 * The first of which is called PopulateEdgeWeights. This method does the following:
	 * Starts by filling the first x index in the edgeWeights method.
	 * Then it starts to loop over the x values starting at 1, and ending at width.
	 * It will set the x key in the hashMap.
	 * It will then iterate over the y values starting at 1 and ending at height.
	 * It will then call a method called addNonBorderWeight on all the cardinal
	 * directions for the x,y coordinates.
	 * 
	 * The addNonBorderWeight method takes (int x, int y, CardinalDirection, and an int edgeweight)
	 * as parameters. It will make sure that the wallboard object using x,y and the
	 * cardinal direction is not a border or part of a room. If it is not, it will access
	 * edgeWeights to put in the cardinal direction and its edgeweight.
	 * 
	 * The final public method is getEdgeWeights. This class is passed along a Wallboard object.
	 * It will get the x and y using the built in methods for WallBoard.
	 * It will then get the cardinal direction of the passed in wallboard.
	 * It will then access the edgeWeights dictionary to return the value for edgeWeight.
	 * Else if it a room or border, it will return infinity (the max integer).
	 * 
	 * Create a class called nodes so that we can store coordinates x,y in them. 
	 * This class will override the equals method and hashcode method.
	 * It will also have two get functions to return x and y coordinates.
	 * It will have a last function named getNeighbor that the neighbor of that
	 * node in a given Cardinal Direction. 
	 * 
	 * Override the generatePathways method from MazeBuilder: 
	 * first call populateEdgeWeights method.
	 * Then create a hash map named forest.
	 * 
	 * Next we want to populate the forest. by iterating over the width
	 * and height of the maze. We create a new node and make an array list of trees to
	 * which we add the nodes. Then we add the nodes and trees in the forest.
	 * For each entry E in the forest, we want to find the cheapest neighbor that's
	 * still in the forest. We want to remove the neighbor and add its value to E's.
	 * We can do this buy creating new Wallboard objects with different cardinal directions.
	 * We put them in a list then iterate over the list to find the minimum edge value.
	 * That wallBoard's index contains the wallboard with the cheapest edge and therefore
	 * neighbor.
	 * 
	 * We will then get rid of these neighbors.
	 * 
	 * 
	 * Then we start the while loop
	 */
	  private HashMap<Integer, HashMap<Integer, HashMap<CardinalDirection, Integer>>> edgeWeights;

	  public MazeBuilderBoruvka() {
	    super();
	    edgeWeights = new HashMap<>();
	  }
	  
	  /** Inner class that creates nodes for x,y coordinates.
	   * Nodes will be used for forests and trees.
	   * 
	   * @author rumitpatel
	   *
	   */
	  private class Node {
		  private final int x;
		  private final int y;
		  Node(int x, int y) {
		    this.x = x;
		    this.y = y;
		  }

		  int getX() { return x; }
		  int getY() { return y; }

		  @Override
          public boolean equals(Object that) {
              return ((Node)that).x == x && ((Node)that).y == y;
          }

          @Override
          public int hashCode() {
              return 1 + (x + y) * 31;
          }
          Node getNeighbor(CardinalDirection cd) {
        	  int[] dxy = cd.getDirection();
        	  return new Node(x + dxy[0], y + dxy[1]);
        	}
          CardinalDirection getDirection(Node that) {
        	  if (x == that.x) {
        	    if (y > that.y) return CardinalDirection.South;
        	    else return CardinalDirection.North;
        	  }
        	  else if (x > that.x) return CardinalDirection.West;
        	  else return CardinalDirection.East;
        	}
          @Override
          public String toString() {
            return "(" + x + "," + y + ")";
          }

		}
	  
	  /** this overrides the generatePathways() method using Boruvka's algorithm which
	   * was implemented using nodes 
	   * 
	   */
	  @Override
	  public void generatePathways() {
	    populateEdgeWeights();
	    
	    HashMap<Node, ArrayList<Node>> forest = new HashMap<>();

		 // populate the forest
		 for (int i = 0; i < width; i++) {
		   for (int j = 0; j < height; j++) {
		     Node n = new Node(i, j); //vertex
		     ArrayList<Node> trees = new ArrayList<>();
		     trees.add(n);
		     forest.put(n, trees);
		   }
		 }
	
		 boolean completed = false;
		 while (!completed) {
		   // for each entry E in forest
		     // find cheapest neighbor that's still in forest
		     // remove remove the neighbor and add its values to E's values
				    // do stuff
			 HashMap<Node, ArrayList<Node>> workingForest = new HashMap<>();
			 for (HashMap.Entry<Node, ArrayList<Node>> entry : forest.entrySet()) {
				//Node entryNode = entry.getKey();
				 Node n = entry.getKey();
				Wallboard southEdge = new Wallboard(n.getX(),n.getY(), CardinalDirection.South);
				Wallboard westEdge =  new Wallboard(n.getX(),n.getY(), CardinalDirection.West);
				Wallboard eastEdge =  new Wallboard(n.getX(),n.getY(), CardinalDirection.East);
				Wallboard northEdge = new Wallboard(n.getX(),n.getY(), CardinalDirection.North);
				Wallboard[] edges = new Wallboard[] {southEdge, westEdge, eastEdge, northEdge};
	    			
				 int cheapestIndex = 0;
				 for (int z = 0; z < edges.length; z++) {
					System.out.println(getEdgeWeight(edges[z]));
					 if (z == 0) {
						 cheapestIndex = 0;
	    				}
					 else if (!floorplan.isPartOfBorder(edges[z])&&getEdgeWeight(edges[z]) < getEdgeWeight(edges[cheapestIndex])) {
	    						cheapestIndex = z;
	
	    					
	    					
	    				}
	    			}
				 ArrayList<Node> newTrees = entry.getValue();
				 System.out.println(n + "--"+forest.get(n.getNeighbor(edges[cheapestIndex].getDirection())));
				 newTrees.addAll(forest.get(n.getNeighbor(edges[cheapestIndex].getDirection())));
				 workingForest.put(n, newTrees);			
				 };
			 
			forest = workingForest; 
		   if (forest.size() == 1)
		     completed = true;
		 }

		 forest.forEach((lastNode, paths) -> {
			  for (int i = 0; i < paths.size() - 1; i++) {
			    Node n = paths.get(i);
			    floorplan.deleteWallboard(new Wallboard(n.getX(), n.getY(), n.getDirection(paths.get(i+1))));
			  }
			});


	  }

	  
	  
	  private void addNonBorderWeight(int x, int y, CardinalDirection cd, int edgeWeight) {
		  if (!floorplan.isPartOfBorder(new Wallboard(x, y, cd)) && !floorplan.isInRoom(x,y)) {
		    edgeWeights.get(x).get(y).put(cd, edgeWeight);
		  }
		}

	  private void populateEdgeWeights() {
	    edgeWeights.put(0, new HashMap<Integer, HashMap<CardinalDirection, Integer>>());

	    // start at 1 cause we don't want to give borders a edgeWeight
	    for (int x = 1; x < width; x++) {
	      edgeWeights.put(x, new HashMap<Integer, HashMap<CardinalDirection, Integer>>());
	      edgeWeights.get(x).put(0, new HashMap<CardinalDirection, Integer>());

	      // start at 1 cause we don't want to give borders a edgeWeight
	      for (int y = 1; y < height; y++) {
	        edgeWeights.get(x).put(y, new HashMap<CardinalDirection, Integer>());

	        // x - 1 east = x west
	        int randomX = random.nextInt();
	        addNonBorderWeight(x, y, CardinalDirection.West, randomX);
	       if(x-1>0) { addNonBorderWeight(x - 1, y, CardinalDirection.East, randomX);} 

	        // y - 1 south = y north
	        int randomY = random.nextInt();
	        addNonBorderWeight(x, y, CardinalDirection.North, randomY);
	        if (y-1>0) {addNonBorderWeight(x, y - 1, CardinalDirection.South, randomY);}
	      }
	    }
	  }
	  
	  public int getEdgeWeight(Wallboard w) {
	    int x = w.getX();
	    int y = w.getY();
	    CardinalDirection cd = w.getDirection();
	    if (edgeWeights.containsKey(x) && edgeWeights.get(x).containsKey(y) && edgeWeights.get(x).get(y).containsKey(cd)) {
	      return edgeWeights.get(x).get(y).get(cd);
	    }
	    return Integer.MAX_VALUE;
	  }
	}