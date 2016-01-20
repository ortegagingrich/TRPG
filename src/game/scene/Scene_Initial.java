package game.scene;

import game.system.Process_Main;

public class Scene_Initial extends Scene {
	
	public Scene_Initial(){
		super();
		loopcount=0;
	}
	
	public void update(){
		super.update();
		//loop twice (to make sure processes are started)
		if(loopcount>2){
			loopcount=0;
			Process_Main.$scene=new Scene_Title();
		}
		loopcount++;
	}

}
