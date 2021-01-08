package group11;

import robocode.*;

import java.awt.geom.*;
import java.util.*;

//import java.util.LinkedList;
//import java.awt.geom.Point2D;
//import java.awt.Color;
import java.io.IOException;

public class G11_Leader extends TeamRobot {
	
	int frame = 0;//for swinging head
	int teamMateCount = 2;//the number of living teammates
	
	// for antiGravMove
	Hashtable<String, EnemyInfo> targets;
	Hashtable<String,TeamInfo>team;
	
	EnemyInfo target;
	final double PI = Math.PI;
	int direction = 1;
	double firePower;
	double midpointstrength = 0;
	int midpointcount = 0;
	//boolean haveTarget = false;
	final int INF=100000100;
	
	/*nonomura test*/
	Point2D.Double p;
	/**/
	
	public void run() {
		// use hashtable
		targets = new Hashtable<String, EnemyInfo>();
		team = new Hashtable<String, TeamInfo>();
		//setAdjustGunForRobotTurn(true);
	//setAdjustRadarForGunTurn(true);
		//turnRadarRightRadians(2 * PI);
		target = new EnemyInfo();
		target.distance = 10000;
		//set color
		setColors();

		//doScanner();
		
		//information of leader
		LeaderInfo leaderInfo = new LeaderInfo(this,team);

		while(true) {
			/*
			if (!haveTarget) {
				target = getNextTarget();
				haveTarget=true;
			}
			*/
			antiGravMove();//antiGravMove(this);
			doScanner();
			doFirePower();
			doGun();
			//out.println("target is "+ target.name);//test
			if(teamMateCount == 0)fire(firePower);
			execute();
			
			//broadcast information of leader
			leaderInfo.update(this,team);
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
				TeamInfo teaminfo;
				if (team.containsKey(e.getName())) {
					teaminfo = (TeamInfo) team.get(e.getName());
				} else {
					teaminfo = new TeamInfo();
					teaminfo.update(e,this);
				    team.put(e.getName(), teaminfo);
				}
			return;
		}else {//case:enemy
			EnemyInfo en;
			if(targets.containsKey(e.getName())) {//if list doesn't have data of that robot
				targets.get(e.getName()).update(e,this);
				p = targets.get(e.getName()).prediction(this, 1);
				en = (EnemyInfo) targets.get(e.getName());
			}else {
				en = new EnemyInfo();
				en.update(e,this);
				targets.put(e.getName(),en);
			}
			double absbearing_rad = (getHeadingRadians() + e.getBearingRadians()) % (2 * PI);

			en.name = e.getName();
			double h = normaliseBearing(e.getHeadingRadians() - en.heading);
			h = h / (getTime() - en.ctime);
			en.changehead = h;
			en.x = getX() + Math.sin(absbearing_rad) * e.getDistance();
			en.y = getY() + Math.cos(absbearing_rad) * e.getDistance();
			if(en.name==target.name) {
				try{
					/*
					//this code should be changed. uncompleted!!!
					broadcastMessage(new EnemyInfo(en.x,en.y,en.speed,en.bearing));
					*/
						broadcastMessage(targets.get(en.name));
				}catch(IOException ex) {
					out.println("missed sending a message");
				}
			}
			en.bearing = e.getBearingRadians();
			en.heading = e.getHeadingRadians();
			en.ctime = getTime();
			en.speed = e.getVelocity();
			en.distance = e.getDistance();
			en.live = true;

			if ((en.distance < target.distance) || (target.live == false)) {
				target = en;
			}
			
			/*prediction test*/
			/*
			if(e.getName().equals(enemyName.get(0))){//found [0] robot
				try {
					broadcastMessage(enemyList.get(0));
				}catch(IOException g) {
					out.println("enemyinfo was ignored");
				}
			}
			*/
			/**/
			
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		/*if use one attack method , you should use this code.
		if(e.getName()==target.name) {
			haveTarget=false;
		}
		*/
		
		if(e.getName().equals("group11.G11_Sub1*") 
				|| e.getName().equals("group11.G11_Sub2*")) {//case:teammate
			teamMateCount -= 1;
			if(teamMateCount == 0) {
				setAdjustGunForRobotTurn(true);
				setAdjustRadarForGunTurn(true);
			}
			//out.println("teamMateCount:" + teamMateCount);//test
			
		}else {//case:enemy
			EnemyInfo en = (EnemyInfo)targets.get(e.getName());
			en.live = false;
			
			if(targets.containsKey(e.getName())) {//leader hasn't found the robot yet
				targets.remove(e.getName());
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
		if(teamMateCount != 0){
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
		}else {
			this.setTurnRadarRight(360);
		}
		
	}
	
	/*
	public Enemy getNextTarget() {
		Enemy en;
		en=new Enemy();
		en.distance=INF;
		Enumeration e = targets.elements();
		while(e.hasMoreElements()) {
			Enemy tmp=(Enemy)e.nextElement();
			if(tmp.live) {
				if(tmp.distance<en.distance) {
					en=tmp;
				}
			}
		}
		return en;
	}
	*/
	
	void antiGravMove() {
		double xforce = 0;
		double yforce = 0;
		double force;
		double ang;
		//Random rand = new Random();
		GravPoint p;
		EnemyInfo en;
		Enumeration<EnemyInfo> e = targets.elements();
		while (e.hasMoreElements()) {
			en = (EnemyInfo) e.nextElement();
			if (en.live) {
				p = new GravPoint(en.x, en.y, -3000);
				force = p.power / Math.pow(getRange(getX(), getY(), p.x, p.y), 2);
				ang = normaliseBearing(Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x));
				xforce += Math.sin(ang) * force;
				yforce += Math.cos(ang) * force;
			}
		}

		midpointcount++;
		if (midpointcount > 5) {
			midpointcount = 0;
			midpointstrength = (Math.random() * 2000) - 1000;
		}
		p = new GravPoint(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2, midpointstrength);
		force = p.power / Math.pow(getRange(getX(), getY(), p.x, p.y), 1.5);
		ang = normaliseBearing(Math.PI / 2 - Math.atan2(getY() - p.y, getX() - p.x));
		xforce += Math.sin(ang) * force;
		yforce += Math.cos(ang) * force;

		xforce += 5000 / Math.pow(getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
		xforce -= 5000 / Math.pow(getRange(getX(), getY(), 0, getY()), 3);
		yforce += 5000 / Math.pow(getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
		yforce -= 5000 / Math.pow(getRange(getX(), getY(), getX(), 0), 3);

		goTo(getX() - xforce, getY() - yforce);
		
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
	
	void doFirePower() {
		firePower = 400/target.distance;//selects a bullet power based on our distance away from the target
		if (firePower > 3) {
			firePower = 3;
		}
	}
	void doGun() {
		long time = getTime() + (int)Math.round((getRange(getX(),getY(),target.x,target.y)/(20-(3*firePower))));
		Point2D.Double p = target.guessPosition(time);

		double gunOffset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(p.y - getY(), p.x - getX()));
		setTurnGunLeftRadians(normaliseBearing(gunOffset));
	}

	double normaliseBearing(double ang) {
		if (ang > PI)
			ang -= 2 * PI;
		if (ang < -PI)
			ang += 2 * PI;
		return ang;
	}

	double normaliseHeading(double ang) {
		if (ang > 2 * PI)
			ang -= 2 * PI;
		if (ang < 0)
			ang += 2 * PI;
		return ang;
	}

	public double getRange(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double h = Math.sqrt(xo * xo + yo * yo);
		return h;
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
}


class GravPoint {
	public double x, y, power;

	public GravPoint(double pX, double pY, double pPower) {
		x = pX;
		y = pY;
		power = pPower;
	}
}

