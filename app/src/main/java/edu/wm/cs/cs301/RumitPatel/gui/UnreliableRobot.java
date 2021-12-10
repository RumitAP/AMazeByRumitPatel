package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.gui.Robot.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * CRC Card: 
 * 		Responsibilities: This class extends ReliableRobot so that it can implement sensor
 * 					      failures and repairs.This is the robot that will traverse the maze
 * 						  in the Wallfollower class. 
 * 		Collaborators: ReliableRobot, used by Wallfollower, Unreliable Sensor
 * @author Rumit Patel
 *
 */
public class UnreliableRobot extends ReliableRobot implements Robot{
	
	private final List<Direction> unreliableDirections;
	private volatile boolean delayRepairInitialization;

	public UnreliableRobot() {
		super();
		unreliableDirections = new ArrayList<>();
	}
	
	
	/**
	 * helper method that tells us how many times to turn based on the direction we want to sense
	 * and the current sensor Direction
	 * @param senseDir direction we want to sense
	 * @param currentSensorDirection - direction of working sensor
	 * @return number of times to rotate right to be able to sense in that direction
	 * 
	 * need to choose one direction to rotate in to simply these switch statements - 
	 * arbitrary decision
	 */
	private int turnsToDirection(Direction senseDir, Direction currentSensorDirection) {
		switch (currentSensorDirection) {
		
		case BACKWARD:
			switch (senseDir) {
			
			case FORWARD:
				return 0;
			
			case LEFT:
				return 1;
				
			case BACKWARD:
				return 2;
				
			case RIGHT:
				return 3;
			}
			
		case RIGHT:
			switch(senseDir) {
			
			case FORWARD:
				return 3;
				
			case LEFT:
				return 0;
				
			case BACKWARD:
				return 1;
				
			case RIGHT:
				return 2;
			
			}
			
		case LEFT:
			switch(senseDir) {
			
			case FORWARD:
				return 1;
				
			case LEFT:
				return 2;
				
			case RIGHT:
				return 0;
				
			case BACKWARD:
				return 3;
			
			}
			
		case FORWARD:
			switch(senseDir) {
			
			case FORWARD:
				return 2;
				
			case LEFT:
				return 3;
				
			case RIGHT:
				return 1;
				
			case BACKWARD:
				return 0;

			}
		
		}
		return 0;
	}
	
	
	public Direction hasWorkingSensor() {
		//use working sensor
		for (Direction direction : Direction.values()) {
			if (super.distanceToObstacle(direction) != Integer.MAX_VALUE) {
				return direction;
			}
		}
		
		//wait until the sensor is operational again if first method did not work.
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//keep doing this until we can get a sensor to work.
		return hasWorkingSensor();
	}
	
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		int distance = super.distanceToObstacle(direction);

		if (distance == Integer.MAX_VALUE) {
			//get direction of working sensor
			Direction dir = hasWorkingSensor();
		
			//number of rotations to sense in that working sensor direction
			switch(turnsToDirection(direction, dir)) {
				//cases are based on left rotations.
				case 1:
					return useWorkingSensor(dir, Turn.RIGHT, Turn.LEFT);
				case 2:
					return useWorkingSensor(dir, Turn.AROUND, Turn.AROUND);
				case 3:
					return useWorkingSensor(dir, Turn.LEFT, Turn.RIGHT);
				default:
					return distance;
			}
		}

		// if sensor works, return the distance.
		return distance;
		
	}
	
	/**
	 * Helper method in distanceToObstacle to rotate to the appropriate, working sensor
	 * before sensing, then back after
	 *
	 * @param direction the target direction for sensing
	 * @param beforeSense the direction to rotate before sensing to be facing correctly
	 * @param afterSense the opposite of beforeSense, to rotate back to face forward
	 * @return the distance to obstacle, according to the working sensor
	 */
	private int useWorkingSensor(Direction direction, Turn beforeSense, Turn afterSense) {
		super.rotate(beforeSense);
		int distance = super.distanceToObstacle(direction);
		super.rotate(afterSense);
		return distance;
	}
	
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		if (delayRepairInitialization) {
			try {
				Thread.sleep(1300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		unreliableDirections.add(direction);
		delayRepairInitialization = true;

		DistanceSensor sensor = getSensorFromDirection(direction);
		if (sensor != null) {
			sensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
	}
	
	/**
	 * Pushes off the operation to the UnreliableSensor for specified sensor.
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		DistanceSensor sensor = getSensorFromDirection(direction);
		if (sensor != null && unreliableDirections.contains(direction)) {
			sensor.stopFailureAndRepairProcess();
			unreliableDirections.remove(direction);
		}
	}

	private DistanceSensor getSensorFromDirection(Direction direction) {
		switch (direction) {
			case FORWARD:
				return senseForward;
			case BACKWARD:
				return senseBackward;
			case LEFT:
				return senseLeft;
			case RIGHT:
				return senseRight;
			default:
				return null;
		}
	}

	public List<Direction> getUnreliableDirections() {
		return unreliableDirections;
	}
}