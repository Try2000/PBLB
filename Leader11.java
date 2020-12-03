package testTeam;

import robocode.*;

import java.util.Arrays;
import java.util.LinkedList;

//import java.awt.Color;
import java.io.IOException;

public class Leader11 extends TeamRobot {
	//informations
	LinkedList<String> enemyName = new LinkedList<String>();
	LinkedList<EnemyInfo> enemyList = new LinkedList<EnemyInfo>();
	
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
		
		//information of leader
		LeaderInfo leaderInfo = new LeaderInfo(this);
		
		//for swinging head
		int frame = 0;
		while(true) {
			//swing head for search
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
			
			//broadcast information of leader
			leaderInfo.update(this);
			try {
				broadcastMessage(leaderInfo);
			}catch(IOException ignored) {
				out.println("LeaderInfo class was ignored");
			}
			
			out.println("LeaderX:" + this.getX() + ", LeaderY:" + this.getY());//test
			out.println(Arrays.toString(this.getTeammates()));//test
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(e.getName().equals("testTeam.Droid11* (2)") 
				|| e.getName().equals("testTeam.Droid11* (3)")) {//case:teammate //not good code
			
			setAhead(100);//test
			out.println("teammate");//test
			
			return;
		}else {//case:enemy
			setTurnLeft(60);//test
			out.println("enemy");//test
			
			int index = enemyName.indexOf(e.getName());
			if(-1 == index) {//if list doesn't have data of that robot
				enemyName.add(e.getName());//add to list
				enemyList.add(new EnemyInfo(e, this));//add to list
				
				out.println(enemyName.get(enemyName.indexOf(e.getName())));//test
			}else {
				enemyList.get(index).update(e, this);
				
				//broadcast to Droid
				
				out.println("name:" + enemyName.get(index) + " x:" + enemyList.get(index).getX()
						+ ", y:" + enemyList.get(index).getY());//test
			}
			
		}
	}
	
	//remove the data of dead enemy
	
}
