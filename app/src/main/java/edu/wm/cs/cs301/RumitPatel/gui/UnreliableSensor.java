package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.generation.CardinalDirection;

/**
 * CRC Card:
 * 		Responsibilities: This class is the sensor for the Wallfollower algorithm, it extends
 * 					      the reliable sensor class so that it can handle the sensor failing 
 * 						  and consequently repairing itself. It tells the distance to an obstacle
 * 						  in a given direction.
 * 		Collaborators: extends ReliableSensor, used by Wallfollower, and UnreliabelRobot
 * @author Rumit Patel
 *
 */

public class UnreliableSensor extends ReliableSensor implements Runnable, DistanceSensor{
	
	private int timeToRepair;
	private int timeBetweenFailures;
	private boolean isOperational = true;
	public Thread failures;
	
	
	public UnreliableSensor() {
		super();
	}
	
	public void setOperational(boolean operational) {
		
		isOperational = operational;
		
	}
	
	public boolean getOperational() {
		return isOperational;
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		
		if (isOperational) {
			return super.distanceToObstacle(currentPosition, currentDirection, powersupply);
		} else {
			throw new Exception("SensorFailure");
		}
		
	}
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		
		// Only setup and start the thread the first call
		if (failures == null) {
			failures = new Thread(this);
			failures.setDaemon(true);
			timeToRepair = meanTimeToRepair;
			timeBetweenFailures = meanTimeBetweenFailures;
			setOperational(true);
			failures.start();
		}
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		
		isOperational = true;
		failures.interrupt();
		
	}
	
    @Override
    public void run() {
        boolean running = true;
        while (running) {
            //repair
            setOperational(false);
            try {
                Thread.sleep(timeToRepair);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                running = false;
            }

            //working fine
            setOperational(true);
            try {
                Thread.sleep(timeBetweenFailures);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                running = false;
            }
            
        }
    }

}
