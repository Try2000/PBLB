import robocode.*;


public class droid extends Robot{

	int others;
	public void onDeath(DeathEvent e){
		if(others == 0) return;

		
		public void broadcastMessage("I'm dead!");

	}
	public void onRobotDeath(RobotDeathEvent event){
		public void run(){
			setBodyColor(Color.black);
			setGunColor(Color.black);
			setBulletColor(Color.cyan);
			
			moveAmount = Math.max(getBattleFieldWidth(),
						getBattleFieldHeight());

		
			peak = false;

			turnLeft(getHeading() % 90);
			ahead(moveAmount);

			peak = true;
			turnGunRight(90);
			turnRight(90);
		

			while(true){
				peak = true;
				ahead(moveAmount);
				peak = false;
				turnRight(90);


			}

		}

	public void onHitRobot(HitRobotEvent e){
		
		turnGunRight(e.getBearing()-90);
		fire(2);

		if(e.getBearing() > -90 && e.getBearing() < 90) {

			back(100);

		}else{
			ahead(100);
		
		}
		
	}	




}
















}
