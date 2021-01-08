package group11;

import java.awt.geom.Point2D;

import robocode.*;

import static robocode.util.Utils.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Hashtable;

public class G11_Sub1 extends TeamRobot implements Droid{
	boolean leaderAlive = true;
	
	/*for Walls*/
	boolean wallsInitialized = false;
	/*nonomura test*/
	Point2D.Double p;
	double theta;
	double turn;
	double firepower;
	//int rotate = 10000;
	//int go = 10000;
	/**/
	
	public double distancetoleader;
	public boolean inWall, movingforward;
	private LeaderInfo leaderinfo;
	private float near = 100;
	private ArrayList<TeamInfo> team;
	//private EnemyInfo enemyinfo;
	final double PI = Math.PI;
	
	public void run() {
		/*temporary*/
		/*
		if(this.getX() < 250 || this.getX() > this.getBattleFieldWidth() - 250 || this.getY() < 250 || this.getY() > this.getBattleFieldHeight() - 250) {
			double firstRotate = Math.toDegrees(Math.atan2(this.getBattleFieldWidth()/2 - this.getX(), this.getBattleFieldHeight()/2 - this.getY()));
			double toCenter = firstRotate - this.getHeading();
			if(toCenter > 180) {
				toCenter -= 360;
			}else if(toCenter < -180) {
				toCenter += 360;
			}
			this.turnRight(toCenter);
			this.ahead(250);
		}
		*/
		/*
		*/
		if (getX() <= 50 || getY() <= 50 || getBattleFieldWidth() - getX() <= 50
				|| getBattleFieldHeight() - getY() <= 50) {
			inWall = true;
		} else {
			inWall = false;
		}
		setMaxVelocity(6);
		setAhead(40000); // go ahead until you get commanded to do differently
		movingforward = true; // we called setAhead, so movingForward is true
		
		while (true) {
			if(leaderAlive) {
				/*temporary*/
				/*
				setAhead(go);
				setTurnRight(rotate);
				*/
				/**/
				
				if (getX() > 50 && getY() > 50 && getBattleFieldWidth() - getX() > 50
						&& getBattleFieldHeight() - getY() > 50 && inWall == true) {
					inWall = false;
				}
				if (getX() <= 50 || getY() <= 50 || getBattleFieldWidth() - getX() <= 50
						|| getBattleFieldHeight() - getY() <= 50) {
					if (!inWall) {
						reverseDirection();
						inWall = true;
					}
				}
				
				/*prediction test*/
				if(firepower > 0) {
					turn = theta - this.getGunHeading();
					if(turn >= 180) {
						turn -= 360;
					}else if(turn <= -180) {
						turn += 360;
					}
					//out.println("turn:" + turn);//test
					setTurnGunRight(turn);
					fire(firepower);
					//firepower = 0;
				}
				/**/
				
				execute();
			}else {//if leader is dead -> become Walls
				
				if(wallsInitialized == false) {//first move
					// turnLeft to face a wall.
					// getHeading() % 90 means the remainder of
					// getHeading() divided by 90.
					turnLeft(getHeading() % 90);
					
					wallsInitialized = true;
				}
				
				if(this.getHeading() == 0) {
					if(this.getY() + 100 >= this.getBattleFieldHeight()) {
						nearWall();
					}
				}else if(this.getHeading() == 180) {
					if(this.getY() - 100  <= 0) {
						nearWall();
					}
				}else if(this.getHeading() == 90) {
					if(this.getX() + 100 >= this.getBattleFieldWidth()) {
						nearWall();
					}
				}else if(this.getHeading() == 270) {
					if(this.getX() - 100 <= 0) {
						nearWall();
					}
				}
				setAhead(800);
				execute();
				
			}
			
		}
	}
	private void nearWall() {
		stop(true);
		this.turnLeft(90);
	}
	
	private void nearTeam(){
		team = (ArrayList<TeamInfo>)leaderinfo.getteam().values();
		team.forEach(teaminfo ->{
			double distancex = teaminfo.getX() - getX();
			double distancey = teaminfo.getY() - getY();
			if(Math.abs(distancex) <= near && Math.abs(distancey) <= near){
				reverseDirection();
				stop(true);
			}
		});
	}
	private void nearLeader(){
		double distancex = leaderinfo.getX() - getX();
		double distancey = leaderinfo.getY() - getY();
		if(Math.abs(distancex) <= near && Math.abs(distancey) <= near){
			stop(true);
			reverseDirection();
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);//unnecessary
			setScanColor(c.scanColor);//unnecessary
			setBulletColor(c.bulletColor);
		}else if(e.getMessage() instanceof LeaderInfo) {
			//LeaderInfo l = (LeaderInfo) e.getMessage();//will be used
			
			//out.println("LeaderX:" + l.getX() + ", LeaderY:" + l.getY());//test
			
			leaderinfo = (LeaderInfo) e.getMessage();
			//out.println(getleaderbearing(leaderinfo));//test
			if (movingforward) {
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)+80));
			} else {
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)+100));
			}
			nearTeam();
			nearLeader();
		}else if(e.getMessage() instanceof EnemyInfo) {
			EnemyInfo enemy = (EnemyInfo) e.getMessage();
			/*prediction test*/
			firepower = 1;
			p = enemy.prediction(this, firepower);
			double thisnextX;
			double thisnextY;
			double heading = this.getHeading();
			double headingChangeRate = 10 - 0.75 * this.getVelocity();
			double radius = this.getVelocity() / headingChangeRate;
			double nextheading = heading + headingChangeRate * 3;//after 3 Tick
			thisnextX = this.getX() + Math.cos(heading)*radius - Math.cos(nextheading)*radius;
			thisnextY = this.getY() - Math.sin(heading)*radius + Math.sin(nextheading)*radius;
			theta = Math.toDegrees(Math.atan2(p.getX() - thisnextX, p.getY() - thisnextY));
			
			//out.println("nextX:" + p.getX() + ", nextY:" + p.getY());//test
			//out.println("theta:" + theta);//test
			/**/
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals("group11.G11_Leader*")) {//when leader died
			leaderAlive = false;
		}else {//test
			firepower = 0;//test
		}
	}
	
	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		if(leaderAlive) {
			/*temporary*/
			//go *= -1;
			/**/
			
			// If we're moving the other robot, reverse!
			if (e.isMyFault()) {
				reverseDirection();
			}
			return;
		}
		
		//if leader isn't alive
		
		// If he's in front of us, set back up a bit.
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(100);
		} // else he's in back of us, so set ahead a bit.
		else {
			ahead(100);
		}
	}
	
	public void onHitWall(HitWallEvent e) {
		if(leaderAlive) {
			/*
			double firstRotate = Math.toDegrees(Math.atan2(this.getBattleFieldWidth()/2 - this.getX(), this.getBattleFieldHeight()/2 - this.getY()));
			double toCenter = firstRotate - this.getHeading();
			if(toCenter > 180) {
				toCenter -= 360;
			}else if(toCenter < -180) {
				toCenter += 360;
			}
			this.turnRight(toCenter);
			this.ahead(250);
			*/
			reverseDirection();
		}else {
			if(Math.abs(e.getBearing()) < 90) {
				back(100);
			}else {
				ahead(100);
			}
		}
	}
	
	public void reverseDirection() {
		if (movingforward) {
			setBack(40000);
			movingforward = false;
		} else {
			setAhead(40000);
			movingforward = true;
		}
	}

	public double getleaderbearing(LeaderInfo leaderinfo) {
		if(leaderinfo != null && leaderinfo.isalive) {
			double bearing = 90 - Math.toDegrees(Math.atan2(leaderinfo.getY()-getY(),leaderinfo.getX()-getX()))- getHeading();
			return bearing;
		}
		return 0;
	}
}
