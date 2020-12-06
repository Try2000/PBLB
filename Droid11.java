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
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)+80));
			} else {
				setTurnRight(normalRelativeAngleDegrees(getleaderbearing(leaderinfo)+100));
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
		if(leaderinfo != null && leaderinfo.isalive) {
			double bearing = 90 - Math.toDegrees(Math.atan2(leaderinfo.getY()-getY(),leaderinfo.getX()-getX()))- getHeading();
			return bearing;
		}
		return 0;
	}

	public void onDeath(DeathEvent e) {
		out.println("I died");//test
	}
}
