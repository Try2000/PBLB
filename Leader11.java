package testTeam;

import robocode.*;

import java.util.Arrays;
import java.util.LinkedList;

//import java.awt.Color;
import java.io.IOException;

public class Leader11 extends TeamRobot {
	LinkedList<EnemyInfo> enemyInfo = new LinkedList<EnemyInfo>();
	
	public void run() {
		//set color
		RobotColors c = new RobotColors();
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);
		try{
			broadcastMessage(c);
		}catch(IOException ignored) {
			out.println("RobotColors class was ignored");
		}
		
		int frame = 0;
		while(true) {
			if((0 <= frame && frame <= 2) || (9 <= frame && frame <= 11)) {
				this.setTurnRadarRight(45);
				this.setTurnGunRight(20);
				this.execute();
			}else if(3 <= frame && frame <= 8) {
				this.setTurnRadarLeft(45);
				this.setTurnGunLeft(20);
				this.execute();
			}
			frame = (frame + 1) % 12;
			
			out.println(Arrays.toString(this.getTeammates()));//test
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(e.getName().equals("testTeam.Droid11* (2)") || e.getName().equals("testTeam.Droid11* (3)")) {//not good
			setAhead(100);//test
			out.println("teammate");//test
			return;
		}else {
			setTurnLeft(60);//test
			out.println("enemy");//test
		}
	}
}
