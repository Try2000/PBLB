package group11;

import robocode.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;
//import java.awt.Color;
import java.io.IOException;

public class G11_Leader extends TeamRobot {
	//informations
	LinkedList<String> enemyName = new LinkedList<String>();
	LinkedList<EnemyInfo> enemyList = new LinkedList<EnemyInfo>();
	
	int frame = 0;//for swinging head
	int teammates = 2;//the number of living teammates
	
	/*nonomura test*/
	Point2D.Double p;
	/**/
	
	public void run() {
		setColors();
		
		
		//information of leader
		LeaderInfo leaderInfo = new LeaderInfo(this);
		
		while(true) {
			doScanner();
			
			//broadcast information of leader
			leaderInfo.update(this);
			try {
				broadcastMessage(leaderInfo);
			}catch(IOException ignored) {
				out.println("LeaderInfo class was ignored");
			}
			
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(e.getName().equals("group11.G11_Sub1*") 
				|| e.getName().equals("group11.G11_Sub2*")) {//case:teammate
	
			setAhead(100);//test
			
			return;
		}else {//case:enemy
			setTurnLeft(60);//test
			
			int index = enemyName.indexOf(e.getName());
			if(-1 == index) {//if list doesn't have data of that robot
				enemyName.add(e.getName());//add to list
				enemyList.add(new EnemyInfo(e, this));//add to list
				
			}else {
				enemyList.get(index).update(e, this);
				
				/*prediction test*/
				p = enemyList.get(index).prediction(this, 1);
				out.println("nextX:" + p.getX() + ", nextY:" + p.getY());
				/**/
			}
			
			/*prediction test*/
			if(e.getName().equals(enemyName.get(0))){//found [0] robot
				try {
					broadcastMessage(enemyList.get(0));
				}catch(IOException g) {
					out.println("enemyinfo was ignored");
				}
			}
			/**/
			
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals("group11.G11_Sub1*") 
				|| e.getName().equals("group11.G11_Sub2*")) {//case:teammate
			teammates--;
			
			out.println("teammate died");//test
		}else {//case:enemy
			int index = enemyName.indexOf(e.getName());
			if(-1 == index) {//leader hasn't found the robot yet
				//do no action
			}else {//remove the data of dead enemy
				
				enemyName.remove(index);
				enemyList.remove(index);
				
			}
		}
	}
	
	public void setColors() {
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
	}
	
	public void doScanner() {
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
	}
}
