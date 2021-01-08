package group11;
import robocode.*;
/**
 * MyClass - a class by (your name here)
 */
public class TeamInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1;
	private double x;
	private double y;
	private double directionToEnemy;
	public void update(ScannedRobotEvent e, TeamRobot robo) {
		set(e, robo);
	}
	private void set(ScannedRobotEvent e, TeamRobot robo) {
		directionToEnemy = robo.getHeading() + e.getBearing();
		x = robo.getX() + e.getDistance() * Math.sin(Math.toRadians(directionToEnemy));
		y = robo.getY() + e.getDistance() * Math.cos(Math.toRadians(directionToEnemy));
	}
	
	private void setxy(double _x,double _y){
		x = _x;
		y = _y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
}
