package evoalgo.problem.ctrnn.trackerSim;

import evolalgo.problem.ctrnn.ITracker;
/**
 * Moves randomly, used as a baseline and debugging of simulation/animation
 * @author Andreas
 *
 */
public class randomTracker implements ITracker {

	@Override
	public int getMovement(boolean[] shadowSensors) {
		// Nobody move, or the bunny gets it!
		return (int) (Math.random()*8)-4;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

}
