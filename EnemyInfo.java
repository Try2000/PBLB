package group11;

import java.awt.geom.Point2D;

import robocode.*;
//for prediction
public class EnemyInfo implements java.io.Serializable {
	private static final long serialVersionUID = 7203604189743390550L;
	public String name;
	public double bearing,distance;
	public long ctime;
	public boolean live;
	
	//past data
	private long pretime;
	private double preheading;
	
	//current data
	public double x;
	public double y;
	private long time;
	public double heading;
	public double speed;
	private double whenhit;
	
	//for prediction
	private double headingChangeRate;
	public double changehead;
	private long passedtime;
	
	private double nextx;
	private double nexty;
	
	private double directionToEnemy;//to locate the enemy
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
	
	public Point2D.Double guessPosition(long when) {
		double diff = when - ctime;
		double newY = y + Math.cos(heading) * speed * diff;
		double newX = x + Math.sin(heading) * speed * diff;

		return new Point2D.Double(newX, newY);
	}
	
}
