package game.scene;

import asset.model.*;
import game.gui.edit.HUD_Model;
import game.system.Process_Graphics;
import game.system.Process_Main;

//scene class for model build/edit mode
public class Scene_Model extends Scene {
	
	public World_Model world;
	public HUD_Model hud;
	
	public Scene_Model(){
		super();
		world=new World_Model(this);
		world.show_world();
		hud=new HUD_Model(this);
	}
	
	//not allowed to skip frames
	@Override
	public int allowed_skip(){
		return 0;
	}
	
	//update method
	public void update(){
		super.update();
		
		//update camera
		update_camera();
		
		//update hud
		hud.update();
		
		//update action
		if(update_action()){
			return;
		}
		
		//update world
		world.update();
	}
	
	//camera update method
	private void update_camera(){
		//temporary; just set camera to focus on origin
		Process_Graphics.$graphics.camx=0;
		Process_Graphics.$graphics.camz=-15;
		Process_Graphics.$graphics.camy=10;
	}
	
	//boolean indicates whether or not to skip the rest of the update (e.g. switching to a different scene type)
	private boolean update_action(){
		
		//if left click, pick the select the component the mouse is over
		if(Process_Main.$input.trigger("Mouse Left")){
			world.select_mouse_component();
		}
		
		//temporary; if right click, de-select current component
		if(Process_Main.$input.trigger("Mouse Right")){
			hud.deselect_component();
			//refresh model
			world.refresh_model();
		}
		
		//temporary: if N is pressed, make a test component block
		if(Process_Main.$input.trigger("N")){
			//make new component
			Component c=new Component_Box(0.5f,0.5f,0.5f,1,1,1);
			world.model.add_component(c);
			//refresh hud
			hud.refresh();
			//refresh model
			world.refresh_model();
		}
		//temporary: if Q is pressed, make a test component quad
		if(Process_Main.$input.trigger("Q")){
			//make new component
			Component c=new Component_Quad(0,0,0,1,1);
			world.model.add_component(c);
			//refresh hud
			hud.refresh();
			//refresh model
			world.refresh_model();
		}
		//temporary: if L is pressed, make a test point light
		if(Process_Main.$input.trigger("L")){
			//make new component
			Component c=new Component_Light(0,0,0,0.8f,5);
			world.model.add_component(c);
			//refresh hud
			hud.refresh();
			//refresh model
			world.refresh_model();
		}
		
		//if M is pressed, save model libraries and return to world edit mode (note: current model is not saved automatically)
		if(Process_Main.$input.trigger("M")){
			Process_Graphics.save_model_libraries();
			//end mode
			end_mode();
			//transition to world edit mode
			Process_Main.$scene=new Scene_WorldEdit();
			return true;
		}
		
		
		return false;
	}
	
	//end mode
	private void end_mode(){
		world.hide_world();
		//dispose HUD
		hud.dispose_panel();
	}

}
