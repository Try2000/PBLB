package testTeam;

import java.io.Serializable;

import robocode.TeamRobot;

public class LeaderInfo implements Serializable {
	private static final long serialVersionUID = 5163422492696388736L;
	public LeaderInfo(TeamRobot leader11,double _xforce,double _yforce) {
		set(leader11);
		xforce = _xforce;
		yforce = _yforce;

	}

	private double x;
	private double y;
	private double xforce;
	private double yforce;
	public boolean isalive = true;

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
	public double getxforce() {
		return xforce;
	}
	public double getyforce() {
		return yforce;
	}

	public void update(TeamRobot leader11,double _xforce,double _yforce) {
		set(leader11);
	}
}
