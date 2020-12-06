package testTeam;

import java.awt.geom.Point2D;

import robocode.*;

public class Droid11 extends TeamRobot implements Droid{
	boolean leaderAlive = true;
	
	/*for Walls*/
	boolean wallsInitialized = false;
	
	/*nonomura test*/
	Point2D.Double p;
	double theta;
	double turn;
	double firepower;
	int rotate = 10000;
	int go = 10000;
	/**/
	
	public void run() {
		/*temporary*/
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
		/**/
		
		while (true) {
			if(leaderAlive) {
				/*temporary*/
				setAhead(go);
				setTurnRight(rotate);
				/**/
				
				/*prediction test*/
				if(firepower > 0) {
					turn = theta - this.getGunHeading();
					if(turn >= 180) {
						turn -= 360;
					}else if(turn <= -180) {
						turn += 360;
					}
					out.println("turn:" + turn);
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
			
			out.println("nextX:" + p.getX() + ", nextY:" + p.getY());
			out.println("theta:" + theta);
			/**/
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals("testTeam.Leader11*")) {//when leader died
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
			go *= -1;
			/**/
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
	
	/*temporary*/
	public void onHitWall(HitWallEvent e) {
		if(leaderAlive) {
			double firstRotate = Math.toDegrees(Math.atan2(this.getBattleFieldWidth()/2 - this.getX(), this.getBattleFieldHeight()/2 - this.getY()));
			double toCenter = firstRotate - this.getHeading();
			if(toCenter > 180) {
				toCenter -= 360;
			}else if(toCenter < -180) {
				toCenter += 360;
			}
			this.turnRight(toCenter);
			this.ahead(250);
		}else {
			if(Math.abs(e.getBearing()) < 90) {
				back(100);
			}else {
				ahead(100);
			}
		}
	}
	/**/
}
