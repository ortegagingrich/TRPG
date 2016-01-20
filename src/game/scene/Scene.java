package game.scene;

import game.system.Process_Main;

public class Scene {
	
	public boolean finished;
	public World world;
	
	public int loopcount;
	
	public Scene(){
		loopcount=0;
	}
	
	//frames allowed to skip
	public int allowed_skip(){
		return 10;
	}
	
	public void update(){
		//end game if escape pressed
		if(Process_Main.$input.trigger("Escape")){
			finished=true;
		}
	}

}
