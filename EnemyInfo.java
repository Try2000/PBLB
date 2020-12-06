package group11;

import java.awt.geom.Point2D;

import robocode.*;
//for prediction
public class EnemyInfo implements java.io.Serializable {
	private static final long serialVersionUID = 7203604189743390550L;
	
	//private String name;//unnecessary
	
	//past data
	private long pretime;
	private double preheading;
	
	//current data
	private double x;
	private double y;
	private long time;
	private double heading;
	private double speed;
	private double whenhit;
	
	//for prediction
	private double headingChangeRate;
	private double changehead;
	private long passedtime;
	
	private double nextx;
	private double nexty;
	
	private double directionToEnemy;//to locate the enemy
	
	public EnemyInfo(ScannedRobotEvent e, TeamRobot robo) {
		//name = e.getName();
		
		set(e, robo);
	}
	
	private void set(ScannedRobotEvent e, TeamRobot robo) {
		pretime = time;
		preheading = heading;
		
		directionToEnemy = robo.getHeading() + e.getBearing();
		x = robo.getX() + e.getDistance() * Math.sin(Math.toRadians(directionToEnemy));
		y = robo.getY() + e.getDistance() * Math.cos(Math.toRadians(directionToEnemy));
		time = robo.getTime();
		heading = e.getHeading();
		speed = e.getVelocity();
	}
	
	/*for test*/
	/*
	public String getName() {
		return name;
	}
	*/
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	/**/
		
	/*important method*/
	public void update(ScannedRobotEvent e, TeamRobot robo) {
		set(e, robo);
	}
	
	public Point2D.Double prediction(TeamRobot robo, double power){
		double distance = Point2D.distance(x, y, robo.getX(), robo.getY());
		double bulletspeed = 20 - 3*power;
		
		whenhit = distance / bulletspeed;
		
		passedtime = time - pretime;
		changehead = heading - preheading;
		headingChangeRate = changehead / passedtime;
		
		if(Math.abs(headingChangeRate) > 0.00001) {//circular
			double radius = speed / headingChangeRate;
			double nextheading = heading + headingChangeRate * whenhit;
			
			nextx = x + Math.cos(heading)*radius - Math.cos(nextheading)*radius;
			nexty = y - Math.sin(heading)*radius + Math.sin(nextheading)*radius;
		}else{//linear
			nextx = x + Math.sin(heading) * speed * whenhit;
			nexty = y + Math.cos(heading) * speed * whenhit;
		}
		
		return new Point2D.Double(nextx, nexty);
	}
	
}
