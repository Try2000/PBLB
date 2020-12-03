package testTeam;

import java.io.Serializable;
import robocode.*;

public class LeaderInfo implements Serializable {
	private static final long serialVersionUID = 5163422492696388736L;
	
	public LeaderInfo(TeamRobot leader11) {
		set(leader11);
	}
	
	private double x;
	private double y;
	
	private void set(TeamRobot leader11) {
		x = leader11.getX();
		y = leader11.getY();
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void update(TeamRobot leader11) {
		set(leader11);
	}
}
