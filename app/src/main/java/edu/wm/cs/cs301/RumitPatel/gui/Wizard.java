package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.generation.CardinalDirection;
import edu.wm.cs.cs301.RumitPatel.generation.Maze;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Direction;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Turn;
/**
 * CRC: drives the robot towards the exit . It uses a maze object to cheat. 
 * It interacts with the reliablesensor and reliableRobot class.
 * @author: Rumit Patel
 */
public class Wizard implements RobotDriver{

	
	
	private ReliableRobot robot;
	private Maze maze;
	private int delay = 0;
	private boolean paused = false;
	
	
	@Override
	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method 
	 * provides it with this necessary information.
	 * @param r robot to operate
	 */
	public void setRobot(Robot r) {
		
		robot = (ReliableRobot) r;
		
	}

	@Override
	public void setMaze(Maze maze) {

		this.maze = maze;
		
	}
	
	//turns toward the direction of the neighbor closer to exit.
	public void rotateDirections(CardinalDirection cd) {
		
		CardinalDirection currentDirection = robot.getCurrentDirection();
		//switch on currentDirection of robot
		switch (currentDirection) {
		
		case North:
			//switch on direction we want robot facing
			switch (cd) {
			
			case North:
				break;
				
			case South:
				robot.rotate(Turn.AROUND);
				break;
				
			case East:
				robot.rotate(Turn.LEFT);
				break;
				
			case West:
				robot.rotate(Turn.RIGHT);
				break;
			}
			break;
		case South:
			switch (cd) {
			
			case North:
				robot.rotate(Turn.AROUND);
				break;
				
			case South:
				break;
				
			case East:
				robot.rotate(Turn.RIGHT);
				break;
				
			case West:
				robot.rotate(Turn.LEFT);
				break;
			}
			break;
		case West:
			switch (cd) {
			
			case North:
				robot.rotate(Turn.LEFT);
				break;
				
			case South:
				robot.rotate(Turn.RIGHT);
				break;
				
			case East:
				robot.rotate(Turn.AROUND);
				break;
				
			case West:
				break;
			}
			break;
			
		case East:
			switch (cd) {
			
			case North:
				System.out.println("north");
				robot.rotate(Turn.RIGHT);
				break;
				
			case South:
				System.out.println("south");
				robot.rotate(Turn.LEFT);
				break;
				
			case East:
				break;
				
			case West:
				System.out.println("west");
				robot.rotate(Turn.AROUND);
				break;
			
			
			}
			break;
		}
		
	}
	
	//so Robot can get the direction of the neighbor.
	public int[] getNeighborCloserToExit(int x, int y) {
		assert maze.isValidPosition(x,y) : "Invalid position";
		// corner case, (x,y) is exit position
		if (maze.getFloorplan().isExitPosition(x, y))
			return null;
		// find best candidate
		int dnext = maze.getDistanceToExit(x, y) ;
		int[] result = new int[3] ; //changed to size three so we can return direction of neighbor too
		int[] dir;
		for (CardinalDirection cd: CardinalDirection.values()) {
			if (maze.hasWall(x, y, cd)) 
				continue; // there is a wallboard in the way
			// no wallboard, let's check the distance
			dir = cd.getDirection();
			int dn = maze.getDistanceToExit(x+dir[0], y+dir[1]);
			if (dn < dnext) {
				// update neighbor position with min distance
				result[0] = x+dir[0] ;
				result[1] = y+dir[1] ;
				
				//result[2] will be int representation of direction of neighbor
				switch (cd) {
				
				case North:
					result[2] = 1;
					break;
					
				case South:
					result[2] = 2;
					break;
					
				case East:
					result[2] = 3;
					break;
					
				case West:
					result[2] = 4;
					break;
				
				}
				
				dnext = dn ;
			}	
		}
		// expectation: we found a neighbor that is closer
		assert(maze.getDistanceToExit(x, y) > dnext) : 
			"cannot identify direction towards solution: stuck at: " + x + ", "+ y ;
		// since assert statements need not be executed, check it 
		// to avoid giving back wrong result
		return (maze.getDistanceToExit(x, y) > dnext) ? result : null;
	}
	@Override
	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive2Exit() throws Exception {

		while (!robot.isAtExit()) {
			if (paused) {
				synchronized (this) {
					try {
						this.wait();
					}
					catch (InterruptedException ignore) {

					}
				}
			}
			drive1Step2Exit();
			delayStep();

			//if we traveled more than area of maze, we are looping.
			if (getPathLength() > 2 * (maze.getWidth() * maze.getHeight())) {
				return false;
			}
		}

		//once at exit, check each direction to see where exit is and move through it.
		if (rotateBeforeExit(Direction.FORWARD, null) ||
				rotateBeforeExit(Direction.LEFT, Turn.LEFT) ||
				rotateBeforeExit(Direction.RIGHT, Turn.RIGHT) ||
				rotateBeforeExit(Direction.BACKWARD, Turn.AROUND)) {

			robot.move(1);
			return true;
		}
		return false;
			
		}


	@Override
	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	public boolean drive1Step2Exit() throws Exception {
		//same as Drive2Exit except without while loop

		int[] startPosition = robot.getCurrentPosition();

		if (robot.hasStopped()) {
			throw new Exception("Robot has stopped due to a problem.");
		}

		if (!robot.isAtExit()) {
			int[] nextPosition = this.getNeighborCloserToExit(startPosition[0], startPosition[1]);
			if (nextPosition != null) {

				switch (nextPosition[2]) {
					case 1:
						this.rotateDirections(CardinalDirection.North);
						break;
					case 2:
						this.rotateDirections(CardinalDirection.South);
						break;
					case 3:
						this.rotateDirections(CardinalDirection.East);
						break;
					default:
						this.rotateDirections(CardinalDirection.West);
				}
			}
			else {
				return false;
			}

			robot.move(1);
		}
		return true;
	}

	/**
	 * Helper method used in drive2exit to rotate appropriately before moving through
	 * the exit
	 *
	 * @param direction the direction for the robot to sense
	 * @param turn the direction to turn, if applicable
	 * @return boolean, whether the robot is at the exit and rotated successfully
	 */
	private boolean rotateBeforeExit(Direction direction, Turn turn) {
		if (robot.hasSensor(direction) && robot.canSeeThroughTheExitIntoEternity(direction)) {
			if (turn != null) {
				robot.rotate(turn);
			}
			return true;
		}
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 3500 - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return robot.getOdometerReading();
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
	}

	private void delayStep() {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			}
			catch (InterruptedException ignore) {}
		}
	}

	@Override
	public void togglePaused() {
		if (paused) {
			paused = false;
			synchronized (this) {
				this.notify();
			}
		}
		else {
			paused = true;
		}
	}
}
