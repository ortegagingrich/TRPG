package game.scene;

import asset.model.*;
import game.save.Quest;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Scene_Title extends Scene {
	
	public Scene_Title(){
		super();
	}
	
	public void update(){
		super.update();
		
		//make quest file
		Process_Main.quest=new Quest("game_data");
		//try to load model libraries
		Process_Graphics.load_model_libraries();
		//try to load sprite set
		System.out.println("before spriteset");
		Process_Graphics.load_spriteset();
		System.out.println("after spriteset");
		//load default quest file
		Process_Main.load_default_quest();
		
		//animation test debug
		boolean animtest=false;
		//animtest=true;  //comment this line out to disable animation test mode
		if(animtest){
			Process_Main.$scene=new Scene_AnimationTest();
			return;
		}
		//temporary: go immediately to world editor mode (only mode so far)
		Process_Main.$scene=new Scene_WorldEdit();
	}

}
