package testTeam;

import robocode.*;

public class EnemyInfo implements java.io.Serializable {
	private static final long serialVersionUID = 7203604189743390550L;

	private double x;
	private double y;
	private double speed;
	private double direction;

	private double dx;
	private double dy;

	public EnemyInfo(double nx,double ny,double nspeed,double ndirection) {
		x=nx;
		y=ny;
		speed=nspeed;
		direction=ndirection;
	}
}
