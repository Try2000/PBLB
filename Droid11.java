package testTeam;
​
import robocode.*;

//matsu import
import java.awt.Color;
import java.awt.geom.*;
import java.util.*;
//

import java.util.Arrays;
import java.util.LinkedList;

//import java.awt.Color;
import java.io.IOException;
import TargetPoint;
public class Droid11 extends TeamRobot implements Droid {
	TargetPoint point;
	public void run() {

​

		while (true) {
			ahead(50);
			back(50);
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
		if (e.getMessage() instanceof TargetPoint) {
			point = (TargetPoint) e.getMessage();
			double angle = Math.toDegrees(absbearing(getX(), getY(), point.x, point.y));
			turnGunRight(angle);
			dofire();
		}

	}

	public void doFire() {
		fire(2);
	}


​}
