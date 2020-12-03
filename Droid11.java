package testTeam;

import robocode.*;
import java.io.IOException;

public class Droid11 extends TeamRobot implements Droid{
	public void run() {
		out.println("MyFirstDroid ready.");
		while (true) {
			ahead(100);//test
			back(100);//test
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
			LeaderInfo l = (LeaderInfo) e.getMessage();
			
			out.println("LeaderX:" + l.getX() + ", LeaderY:" + l.getY());//test
		}
	}
	
	public void onDeath(DeathEvent e) {
		out.println("I died");//test
	}
}
