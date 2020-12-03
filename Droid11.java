package testTeam;
​
import robocode.util.Utils.*;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.util.LinkedList;
public class Droid11 extends TeamRobot implements Droid {
	public double distancetoleader;
	public boolean inWall, movingforward;
	public LeaderInfo leaderinfo;
	public LinkedList<EnemyInfo> enemylist;
	public void run() {
		out.println("MyFirstDroid ready.");
​
		if (getX() <= 50 || getY() <= 50 || getBattleFieldWidth() - getX() <= 50
				|| getBattleFieldHeight() - getY() <= 50) {
			inWall = true;
		} else {
			inWall = false;
		}
​
		setAhead(40000); // go ahead until you get commanded to do differently
		setTurnRadarRight(360); // scan until you find your first enemy
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
			if (movingforward) {
				setTurnRight(normalRelativeAngleDegrees(80));
			} else {
				setTurnRight(normalRelativeAngleDegrees(100));
			}
			execute();
		}
	}
​
	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
​
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);//don't have
			setScanColor(c.scanColor);//don't have
			setBulletColor(c.bulletColor);
		}
		if (e.getMessage() instanceof LeaderInfo) {
			leaderinfo = (LeaderInfo) e.getMessage();
		}
		if (e.getMessage() instanceof EnemyInfo) {
			enemylist.add((EnemyInfo) e.getMessage());
		}
		if (e.getMessage() == LeaderInfo.Operator.ListReset) {
			enemylist.removeAll(enemylist);
		}
	}
​
	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}
​
	public void onHitRobot(HitRobotEvent e) {
		// If we're moving the other robot, reverse!
		if (e.isMyFault()) {
			reverseDirection();
		}
	}
​
	public void reverseDirection() {
		if (movingforward) {
			setBack(40000);
			movingforward = false;
		} else {
			setAhead(40000);
			movingforward = true;
		}
	}
​
	public double getleaderbearing(LeaderInfo leaderinfo) {
		if(leaderinfo != null & leaderinfo.isalive) {
			double bearing = Math.toDegrees(Math.atan2(getX() - leaderinfo.LeaderX, getY() - leaderinfo.LeaderY));
			return bearing;
		}
		return 0;
	}
​
	public void onDeath(DeathEvent e) {
		out.println("I died");//test
	}
}