package testTeam;

import static robocode.util.Utils.*;

import robocode.DeathEvent;
import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;
public class Droid11 extends TeamRobot implements Droid {
	public double distancetoleader;
	public boolean inWall, movingforward;
	private LeaderInfo leaderinfo;
	private EnemyInfo enemyinfo;
	final double PI = Math.PI;
	public void run() {
		out.println("MyFirstDroid ready.");

		if (getX() <= 50 || getY() <= 50 || getBattleFieldWidth() - getX() <= 50
				|| getBattleFieldHeight() - getY() <= 50) {
			inWall = true;
		} else {
			inWall = false;
		}

		setAhead(40000); // go ahead until you get commanded to do differently
		movingforward = true; // we called setAhead, so movingForward is true
		while (true) {
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
			execute();
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();

			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);//don't have
			setScanColor(c.scanColor);//don't have
			setBulletColor(c.bulletColor);
		}
		if (e.getMessage() instanceof LeaderInfo) {
			leaderinfo = (LeaderInfo) e.getMessage();
			out.println(getleaderbearing(leaderinfo));
			if (movingforward) {
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)));
			} else {
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)));
			}
		}
	}

	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}

	public void onHitRobot(HitRobotEvent e) {
		// If we're moving the other robot, reverse!
		if (e.isMyFault()) {
			reverseDirection();
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
		if(leaderinfo != null & leaderinfo.isalive) {
			double bearing = Math.toDegrees(Math.atan(getnormaldegreefromheading(getHeading())) - Math.atan2(leaderinfo.getY()-getY(),leaderinfo.getX()-getX()));
			return bearing;
		}
		return 0;
	}

	public double getnormaldegreefromheading(double theta) {
		double degree = 0;;
		if(theta < 90) {
			degree = 90 - theta;
		}else if(theta >=90 && theta < 180) {
			degree = 360 - (theta - 90);
		}else if(theta >= 180 && theta < 270) {
			degree = 270 - (theta- 180);
		}else if(theta >= 270 && theta < 360) {
			degree = 180 -(theta - 270);
		}
		return degree;
	}

	public void onDeath(DeathEvent e) {
		out.println("I died");//test
	}

	void goTo(double x, double y) {
		double dist = 20;
		double angle = Math.toDegrees(absbearing(getX(), getY(), x, y));
		double r = turnTo(angle);
		setAhead(dist * r);
	}

	int turnTo(double angle) {
		double ang;
		int dir;
		ang = normaliseBearing(getHeading() - angle);
		if (ang > 90) {
			ang -= 180;
			dir = -1;
		} else if (ang < -90) {
			ang += 180;
			dir = -1;
		} else {
			dir = 1;
		}
		setTurnLeft(ang);
		return dir;
	}

	double normaliseBearing(double ang) {
		if (ang > PI)
			ang -= 2 * PI;
		if (ang < -PI)
			ang += 2 * PI;
		return ang;
	}


	public double absbearing(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double h = getRange(x1, y1, x2, y2);
		if (xo > 0 && yo > 0) {
			return Math.asin(xo / h);
		}
		if (xo > 0 && yo < 0) {
			return Math.PI - Math.asin(xo / h);
		}
		if (xo < 0 && yo < 0) {
			return Math.PI + Math.asin(-xo / h);
		}
		if (xo < 0 && yo > 0) {
			return 2.0 * Math.PI - Math.asin(-xo / h);
		}
		return 0;
	}

	public double getRange(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double h = Math.sqrt(xo * xo + yo * yo);
		return h;
	}
}
