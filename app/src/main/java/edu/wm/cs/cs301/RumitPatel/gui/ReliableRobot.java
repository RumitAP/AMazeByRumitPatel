package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.generation.CardinalDirection;
import edu.wm.cs.cs301.RumitPatel.generation.Maze;
import edu.wm.cs.cs301.RumitPatel.gui.Constants.UserInput;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Direction;


/**
 * CRC: this is the robot that will traverse the maze. It uses the maze object
 * To know where to move. It has access to Floorplan so it can cheat.
 * Interacts with the ReliableSensor class to get distances to obstacle
 * And the wizard which drives it.
 * @author rumitpatel
 *
 */

public class ReliableRobot implements Robot {

	private final int ENERGY_FOR_STEP = 6;
	private final int ENERGY_FOR_TURN = 3;
	private final int ENERGY_FOR_JUMP = 40;

	private StatePlaying control;
	private float batteryLevel = 3500;
	private Maze mazeContainer;
	private int odometer = 0;
	private boolean stopped;
	protected DistanceSensor senseLeft;
	protected DistanceSensor senseRight;
	protected DistanceSensor senseForward;
	protected DistanceSensor senseBackward;

	
	
	@Override
	/**
	 * Provides the robot with a reference to the controller to cooperate with.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * @throws IllegalArgumentException if controller is null, 
	 * or if controller is not in playing state, 
	 * or if controller does not have a maze
	 */
	public void setController(StatePlaying controller) {
		if (controller == null) {
			throw new IllegalArgumentException();
		}
		control = controller;
		//maze object
		mazeContainer = control.getMazeConfiguration();
	}
	
	


	@Override
	/**
	 * Adds a distance sensor to the robot such that it measures in the given direction.
	 * This method is used when a robot is initially configured to get ready for operation.
	 * The point of view is that one mounts a sensor on the robot such that the robot
	 * can measure distances to obstacles or walls in that particular direction.
	 * For example, if one mounts a sensor in the forward direction, the robot can tell
	 * with the distance to a wall for its current forward direction, more technically,
	 * a method call distanceToObstacle(FORWARD) will return a corresponding distance.
	 * So a robot with a left and forward sensor will internally have 2 DistanceSensor
	 * objects at its disposal to calculate distances, one for the forward, one for the
	 * left direction.
	 * A robot can have at most four sensors in total, and at most one for any direction.
	 * If a robot already has a sensor for the given mounted direction, adding another
	 * sensor will replace/overwrite the current one for that direction with the new one.
	 * @param sensor is the distance sensor to be added
	 * @param mountedDirection is the direction that it points to relative to the robot's forward direction
	 */
	
	//Sensors added when configuring the Robot
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		//switch based on the direction you wanted to place the sensor
		switch (mountedDirection) {
		case LEFT:
			senseLeft = sensor;
			senseLeft.setMaze(mazeContainer);
			senseLeft.setSensorDirection(mountedDirection);
			break;
			
		case RIGHT:
			senseRight = sensor;
			senseRight.setMaze(mazeContainer);
			senseRight.setSensorDirection(mountedDirection);
			break;
			
		case FORWARD:
			senseForward = sensor;
			senseForward.setMaze(mazeContainer);
			senseForward.setSensorDirection(mountedDirection);
			break;
			
		case BACKWARD:
			senseBackward = sensor;
			senseBackward.setMaze(mazeContainer);
			senseBackward.setSensorDirection(mountedDirection);
			break;
		}
		
	}		
	
	//Helper method to check if the robot has a sensor in a given direction.
	//Wizard and ReliableRobot use this and this is why it must be public.
	public boolean hasSensor( Direction direction) {
			switch (direction) {
			
			case LEFT:
				return senseLeft != null;
			
			case RIGHT:
				return senseRight != null;
				
			case FORWARD:
				return senseForward != null;
				
			case BACKWARD:
				return senseBackward != null;
			}
		return false;
	}
	

	@Override
	public int[] getCurrentPosition() throws Exception {
		int[] pos = control.getCurrentPosition();
		if (pos[0] > mazeContainer.getWidth() || pos[1] > mazeContainer.getHeight() 
				|| pos[0] < 0 || pos[1] < 0) {
			
			throw new Exception("Outside Maze");
			
		}
		
		return control.getCurrentPosition();
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		return control.getCurrentDirection();
	}

	@Override
	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for sensor distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @return current battery level, {@code level > 0} if operational. 
	 */
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return batteryLevel;
	}

	@Override
	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @throws IllegalArgumentException if level is negative 
	 */
	public void setBatteryLevel(float level) {
		if (level <= 0) {
			throw new IllegalArgumentException();
		} else {
			batteryLevel = level;
		}
		
	}

	@Override
	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	public float getEnergyForFullRotation() {
		// 90 degree turn is 3 energy so this is four times that (12).
		return ENERGY_FOR_TURN * 4;
	}

	@Override
	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step and that for moving a distance of n steps 
	 * takes n times the energy for a single step.
	 * @return energy for a single step forward
	 */
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		return ENERGY_FOR_STEP;
	}

	@Override
	/** 
	 * Gets the distance traveled by the robot.
	 * The robot has an odometer that calculates the distance the robot has moved.
	 * Whenever the robot moves forward, the distance 
	 * that it moves is added to the odometer counter.
	 * The odometer reading gives the path length if its setting is 0 at the start of the game.
	 * The counter can be reset to 0 with resetOdomoter().
	 * @return the distance traveled measured in single-cell steps forward
	 */
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	@Override
	public void resetOdometer() {
		stopped = false;
		odometer = 0;
		
	}
	
	/**
	 * Helper method to perform the rotation, if possible
	 *
	 * @param key the UserInput key to press
	 * @param numberOfRotations the number of times to press key
	 */
	private void performRotate(UserInput key, int numberOfRotations) {
		if (batteryLevel >= ENERGY_FOR_TURN * numberOfRotations) {
			//turn it and decrement the battery.
			for (int i = 0; i < numberOfRotations; i++) {
				control.keyDown(key, 0);
			}
			batteryLevel = batteryLevel - (ENERGY_FOR_TURN * numberOfRotations);

		} else {
			batteryLevel = 0;
		}

		if (batteryLevel <= 0) {
			stopped = true;
		}
	}

	@Override
	/**
	 * Turn robot on the spot for amount of degrees. 
	 * If robot runs out of energy, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * @param turn is the direction to turn and relative to current forward direction. 
	 */
	public void rotate(Turn turn) {
//make sure the robot has not stopped due to a crash or no battery.
		if (!hasStopped()) {
			switch (turn) {
			case LEFT:
				performRotate(UserInput.LEFT, 1);
				break;
			case RIGHT:
				performRotate(UserInput.RIGHT, 1);
				break;
			case AROUND:
				performRotate(UserInput.RIGHT, 2);
				break;
			}
			
		}
		
	}

	@Override
	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * @param distance is the number of cells to move in the robot's current forward direction 
	 * @throws IllegalArgumentException if distance not positive
	 */
	public void move(int distance) {
		
		if (distance < 0) {
			throw new IllegalArgumentException();
		}
		
		int[] currentPosition;
		
		while (distance > 0) {
			
			try {
				currentPosition = getCurrentPosition();
			} catch (Exception e) {

				e.printStackTrace();
				break;
			}
			// if there is a wall in the direction the robot is facing.
			if (mazeContainer.hasWall(currentPosition[0], currentPosition[1], getCurrentDirection())) {
				
				stopped = true;
				break;
			}
			
			if (batteryLevel >= ENERGY_FOR_STEP) {
				//move the robot forward and increase odometer and decrease battery.
				control.keyDown(UserInput.UP ,0);
				batteryLevel = batteryLevel - ENERGY_FOR_STEP;
				odometer = odometer + 1;
				
			} else {
				batteryLevel = 0;
				stopped = true;
				break;
				
			}
			
			distance--;
			
		}
		
	}
	
	/**
	 * Helper method to actually perform the jump after checking whether
	 * the landing position is valid
	 *
	 * @param position the current position of the robot
	 * @param plusX an optional X value added before checking position validity
	 * @param plusY an optional Y value added before checking position validity
	 */
	private void performJump(int[] position, int plusX, int plusY) {
		if (mazeContainer.isValidPosition(position[0] + plusX, position[1] + plusY)) {
			control.keyDown(UserInput.JUMP, 0);
			batteryLevel = batteryLevel - ENERGY_FOR_JUMP;
			odometer++;
		}
	}

	@Override
	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level.
	 * If the robot tries to jump over an exterior wall and
	 * would land outside of the maze that way,  
	 * it remains at its current location and direction,
	 * hasStopped() == true as this is not supposed to happen.
	 */
	public void jump() {
		
		try {
			int[] currentPosition = getCurrentPosition();

			if (!hasStopped() && batteryLevel >= ENERGY_FOR_JUMP) {
				switch (getCurrentDirection()) {
					case North:
						performJump(currentPosition, 0, -1);
						break;
					case South:
						performJump(currentPosition, 0, 1);
						break;
					case East:
						performJump(currentPosition, 1, 0);
						break;
					case West:
						performJump(currentPosition, -1, 0);
						break;
				}
			} else {
				batteryLevel = 0;
			}

			if (batteryLevel <= 0) {
				stopped = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		


	@Override
	public boolean isAtExit() {
		try {
			int[] currentPosition = getCurrentPosition();
			int[] exit = mazeContainer.getExitPosition();

			return exit[0] == currentPosition[0] && exit[1] == currentPosition[1];
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isInsideRoom() {
		try {
			
			int[] currentPosition = this.getCurrentPosition();
			return mazeContainer.isInRoom(currentPosition[0], currentPosition[1]);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	/**
	 * Tells if the robot has stopped for reasons like lack of energy, 
	 * hitting an obstacle, etc.
	 * Once a robot is has stopped, it does not rotate or 
	 * move anymore.
	 * @return true if the robot has stopped, false otherwise
	 */
	public boolean hasStopped() {
		return (stopped || batteryLevel <= 0);
	}

	@Override
	/**
	 * Tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 * The direction is relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step forward before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * The robot uses its internal DistanceSensor objects for this and
	 * delegates the computation to the DistanceSensor which need
	 * to be installed by calling the addDistanceSensor() when configuring
	 * the robot.
	 * @param direction specifies the direction of interest
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		if (hasSensor(direction)) {
			float[] bl = new float[1];
			bl[0] = batteryLevel;
			int dist = 0;

			try {
				switch (direction) {
					case LEFT:
						//pass calculation to the appropriate sensor object
						dist = senseLeft.distanceToObstacle(this.getCurrentPosition(), this.getCurrentDirection(), bl);
						break;
					case RIGHT:
						dist = senseRight.distanceToObstacle(this.getCurrentPosition(), this.getCurrentDirection(), bl);
						break;
					case FORWARD:
						dist = senseForward.distanceToObstacle(this.getCurrentPosition(), this.getCurrentDirection(), bl);
						break;
					case BACKWARD:
						dist = senseBackward.distanceToObstacle(this.getCurrentPosition(), this.getCurrentDirection(), bl);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			batteryLevel = bl[0]; //energy to scan is 1
			return dist;
					
		} else {
			throw new UnsupportedOperationException();
		}
}

	@Override
	/**
	 * Tells if a sensor can identify the exit in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 * It is a convenience method is based on the distanceToObstacle() method and transforms
	 * its result into a boolean indicator.
	 * @param direction is the direction of the sensor
	 * @return true if the exit of the maze is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		
		return (distanceToObstacle(direction) == Integer.MAX_VALUE); 

	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
		
	}

}
