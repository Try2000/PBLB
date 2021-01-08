package group11;

import java.io.Serializable;
import robocode.*;
import java.awt.geom.*;
import java.util.*;
public class LeaderInfo implements Serializable {
	private static final long serialVersionUID = 5163422492696388736L;
	
	
	private Hashtable<String,TeamInfo> team;
	private double x;
	private double y;
	
	/*nakata*/
	public boolean isalive = true;
	/**/
	public LeaderInfo(TeamRobot leader11,Hashtable<String,TeamInfo> _team) {
		set(leader11,_team);
	}
	private void set(TeamRobot leader11,Hashtable<String,TeamInfo> _team) {
		x = leader11.getX();
		y = leader11.getY();
		team = _team;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public Hashtable<String,TeamInfo> getteam(){
		return team;
	}
	
	public void update(TeamRobot leader11,Hashtable<String,TeamInfo> _team) {
		set(leader11,_team);
		team = _team;
	}
}
