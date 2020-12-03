package testTeam;

import robocode.*;
//for prediction
public class EnemyInfo implements java.io.Serializable {
	private static final long serialVersionUID = 7203604189743390550L;
	
	private String name;
	private double prex;
	private double prey;
	private double x;
	private double y;
	private double nextx;
	private double nexty;
	//nowx,y = getX,Y
	
	private double dx;
	private double dy;
	
	private double directionToEnemy;
	
	public EnemyInfo(ScannedRobotEvent e, TeamRobot robo) {
		name = e.getName();
		
		set(e, robo);
	}
	
	private void set(ScannedRobotEvent e, TeamRobot robo) {
		prex = x;
		prey = y;
		
		directionToEnemy = robo.getHeading() + e.getBearing();
		x = robo.getX() + e.getDistance() * Math.sin(Math.toRadians(directionToEnemy));
		y = robo.getY() + e.getDistance() * Math.cos(Math.toRadians(directionToEnemy));
	}
	
	public String getName() {
		return name;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	/*important method*/
	public void update(ScannedRobotEvent e, TeamRobot robo) {
		set(e, robo);
	}
	
}
