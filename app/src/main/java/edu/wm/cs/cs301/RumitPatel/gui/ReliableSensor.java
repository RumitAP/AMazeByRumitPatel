package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.generation.CardinalDirection;
import edu.wm.cs.cs301.RumitPatel.generation.Maze;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Direction;

//@autor Rumit Patel
/**This class will interact with any RobotAlgorithm including wallflower
So that it can calculate the distance to an obstacle. Here, it interacts
With reliable robot.
*/
public class ReliableSensor implements DistanceSensor {
	
	private Maze maze;
	private int sensorAngle;
	private final int[] move = new int[2];
	private final int ENERGY_FOR_SENSING = 1;

	

	/**
	 * Tells the distance to an obstacle (a wallboard) that the sensor
	 * measures. The sensor is assumed to be mounted in a particular
	 * direction relative to the forward direction of the robot.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step in this direction before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * 
	 * This method requires that the sensor has been given a reference
	 * to the current maze and a mountedDirection by calling 
	 * the corresponding set methods with a parameterized constructor.
	 * 
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @param powersupply is an array of length 1, whose content is modified 
	 * to account for the power consumption for sensing
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise.
	 * @throws Exception with message 
	 * SensorFailure if the sensor is currently not operational
	 * PowerFailure if the power supply is insufficient for the operation
	 * @throws IllegalArgumentException if any parameter is null
	 * or if currentPosition is outside of legal range
	 * ({@code currentPosition[0] < 0 || currentPosition[0] >= width})
	 * ({@code currentPosition[1] < 0 || currentPosition[1] >= height}) 
	 * @throws IndexOutOfBoundsException if the powersupply is out of range
	 * ({@code powersupply < 0}) 
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		//checks if paramters are valid
		if (currentPosition == null || currentPosition[0] < 0 || currentPosition[0] >= maze.getWidth() || 
				currentPosition[1] < 0 || currentPosition[1] >= maze.getHeight() || 
				currentDirection == null || powersupply == null ) {
			
			throw new IllegalArgumentException();

			
		//	checks if we overspent battery
		} else if (powersupply[0] < 0) {
			
			throw new IndexOutOfBoundsException();
			
			//checks if we overspent battery
		}else if (powersupply[0] < getEnergyConsumptionForSensing()) {
			
			throw new Exception("PowerFailure");
			
		} 
				
		int numberOfSteps = 0;
		int x = currentPosition[0];
		int y = currentPosition[1];
		
		//gets CardinalDirection of the sensor in relation to robot
		CardinalDirection checkDirection = CheckDirection(currentDirection, sensorAngle);
		
		while (numberOfSteps < Integer.MAX_VALUE && !maze.hasWall(x, y, checkDirection)) {
			//move to neighbor
			x = x + move[0];
			y = y + move[1];
			numberOfSteps++;

			//if out of bounds, we won
			if ((checkDirection == CardinalDirection.East || checkDirection == CardinalDirection.West) &&
				numberOfSteps > maze.getWidth()
			) {
				numberOfSteps = Integer.MAX_VALUE;
			}
			
			//if out of bounds we won
			if ((checkDirection == CardinalDirection.North || checkDirection == CardinalDirection.South) &&
				numberOfSteps > maze.getHeight()
			) {
				numberOfSteps = Integer.MAX_VALUE;
			}
			
			//checking to make sure it's in bounds
			if (x < 0 || y < 0 || y >= maze.getHeight() || x >= maze.getWidth()) {
				numberOfSteps = Integer.MAX_VALUE;
			}
		}
		powersupply[0] = powersupply[0] - ENERGY_FOR_SENSING;
		return numberOfSteps;
	}
	
	//helper method for getDistanceToObstacle that gets direction of sensor
	private CardinalDirection CheckDirection(CardinalDirection cd, int sensorAngle) {
		
		switch (cd) {
		//switched north & south
		case North:
			switch (sensorAngle) {
			
			case 270:
				move[0] = 1;
				move[1] = 0;
				return CardinalDirection.East;
			
			case 90:
				move[0] = -1;
				move[1] = 0;
				return CardinalDirection.West;
				
			case 0:
				move[0] = 0;
				move[1] = -1;
				return CardinalDirection.North;
				
			case 180:
				move[0] = 0; 
				move[1] = 1;
				return CardinalDirection.South;
			}
			
		case South:
			switch (sensorAngle) {
			case 270:
				move[0] = -1; 
				move[1] = 0;
				return CardinalDirection.West;
			
			case 90:
				move[0] = 1; 
				move[1] = 0;
				return CardinalDirection.East;
				
			case 0:
				move[0] = 0; 
				move[1] = 1;
				return CardinalDirection.South;
				
			case 180:
				move[0] = 0; 
				move[1] = -1;
				return CardinalDirection.North;
			}
			
		case East:
			switch (sensorAngle) {
			case 270:
				move[0] = 0; 
				move[1] = 1;
				return CardinalDirection.South;
				
			case 90:
				move[0] = 0; 
				move[1] = -1;
				return CardinalDirection.North;
				
			case 0:
				move[0] = 1; 
				move[1] = 0;
				return CardinalDirection.East;
			
			case 180:
				move[0] = -1; 
				move[1] = 0;
				return CardinalDirection.West;
			}
			
		case West:
			switch (sensorAngle) {
			case 270:
				move[0] = 0; 
				move[1] = -1;
				return CardinalDirection.North;
			
			case 90:
				move[0] = 0; 
				move[1] = 1;
				return CardinalDirection.South;
				
			case 0:
				move[0] = -1; 
				move[1] = 0;
				return CardinalDirection.West;
				
			case 180:
				move[0] = 1; 
				move[1] = 0;
				return CardinalDirection.East;
			}
		}
		return cd;
	}

	@Override
	/**
	 * Provides the maze information that is necessary to make
	 * a DistanceSensor able to calculate distances.
	 * @param maze the maze for this game
	 * @throws IllegalArgumentException if parameter is null
	 * or if it does not contain a floor plan
	 */
	public void setMaze(Maze maze) {
		if (maze == null || maze.getFloorplan() == null) {
			throw new IllegalArgumentException();
		} else {
			this.maze = maze;
		}
	}

	@Override
	/**
	 * Provides the angle, the relative direction at which this 
	 * sensor is mounted on the robot.
	 * If the direction is left, then the sensor is pointing
	 * towards the left hand side of the robot at a 90 degree
	 * angle from the forward direction. 
	 * @param mountedDirection is the sensor's relative direction
	 * @throws IllegalArgumentException if parameter is null
	 */
	public void setSensorDirection(Direction mountedDirection) {
		if (mountedDirection == null) {
			throw new IllegalArgumentException();
		}
		
		switch (mountedDirection) {
			case LEFT:
				sensorAngle = 270;
				break;
				
			case RIGHT:
				sensorAngle = 90;
				break;
				
			case FORWARD:
				sensorAngle = 0;
				break;
				
			case BACKWARD:
				sensorAngle = 180;
				break;
		}

	}
	


	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return ENERGY_FOR_SENSING;
	}
	
	public int getSensorAngle() {
		return sensorAngle;
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}


}
