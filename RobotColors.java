package testTeam;

import java.awt.Color;

public class RobotColors implements java.io.Serializable {
	private static final long serialVersionUID = 7781932971629945286L;
	
	public Color bodyColor;
	public Color gunColor;
	public Color radarColor;
	public Color scanColor;
	public Color bulletColor;
	
	RobotColors(){
		bodyColor = Color.gray;
		//gunColor = Color.magenta;
		gunColor = Color.cyan;
		radarColor = Color.gray;
		scanColor = Color.gray;
		bulletColor = Color.white;
	}
}
