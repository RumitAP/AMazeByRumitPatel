package edu.wm.cs.cs301.RumitPatel.gui;

import edu.wm.cs.cs301.RumitPatel.generation.Maze;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Direction;
import edu.wm.cs.cs301.RumitPatel.gui.Robot.Turn;

/**
 * CRC Card:
 * 		Responsibilities: Drives the robot roward the exit by following the wall on its
 * 						  left side
 * 		Collaborators: UnreliableRobot, UnreliableSensor, Wizard, Reliable Sensor(as
 * 		it is extended by UnreliableSensor), and ReliableRobot (as it is extended by
 * 		UnreliableRebot)
 * 
 * 
 * @author Rumit Patel
 *
 */

public class Wallfollower implements RobotDriver{
	private final int INITIAL_BATTERY_LEVEL = 3500;
	private Robot robot;
	private int delay = 0;
	private boolean paused = false;
	

	@Override
	public void setRobot(Robot r) {
		robot = r;
		
	}

	@Override
	public void setMaze(Maze maze) {}
	

	
	@Override
	public boolean drive1Step2Exit() throws Exception {
		if (!robot.isAtExit()) {
			//fails if the robot has stopped
			if (robot.hasStopped()) {
				return false;
			}
			
			//go left if we can, so we are on the wall
			if (robot.distanceToObstacle(Direction.LEFT) != 0) {
				robot.rotate(Turn.LEFT);
				robot.move(1);
				return true;
			  //if against the wall already	
			}
			else if (robot.distanceToObstacle(Direction.FORWARD) == 0) {
				robot.rotate(Turn.RIGHT);
			}
			else {
				//move to wall
				robot.move(1);
				return true;
			}

		}
		return true;
	}
	
	@Override
	public boolean drive2Exit() throws Exception {
		while (!robot.isAtExit()) {
			if (robot.hasStopped()) {
				throw new Exception("Robot has stopped due to a problem.");
			}

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
		}

		//if at exit, then turn to teh direction of the exit and move through it.
		while (!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
			robot.rotate(Robot.Turn.LEFT);
		}
		robot.rotate(Robot.Turn.LEFT);
		robot.move(1);


		return true;
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
	public float getEnergyConsumption() {
		return INITIAL_BATTERY_LEVEL - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return robot.getOdometerReading();
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
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