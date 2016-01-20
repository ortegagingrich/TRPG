package game.gui.edit;

import asset.model.*;
import game.scene.Scene_Model;
import game.system.Process_Graphics;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Controller_Model implements ScreenController {
	
	private HUD_Model hud;
	private Scene_Model scene;  //for easy retrieval
	
	public Controller_Model(HUD_Model h,Scene_Model s){
		hud=h;
		scene=s;
	}

	@Override
	public void bind(Nifty arg0, Screen arg1) {

	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {

	}
	
	//button interaction methods
	
	public void make_new_model(){
		scene.world.make_new_model();
		//de-select current component
		hud.select_component(new Component());
	}
	
	public void save_current_model(){
		//first check to see if current model has a valid id
		if(scene.world.model.id<0){ //if not, save as new model
			scene.world.library.add_model(scene.world.model);
		}else{ //if so, then overwrite old model
			scene.world.library.save_model(scene.world.model,scene.world.model.id);
		}
		//save model libraries
		Process_Graphics.save_model_libraries();
	}
	
	public void open_load_model_window(){
		//call hud's method for this
		hud.open_window_load_model();
	}
	
	//component selection methods
	private int selected_component_id(){
		//retrieve selected map ID
		ListBox l=hud.panel.getScreen("start").findNiftyControl("list components",ListBox.class);
		int id;
		if(l.getSelection().size()!=0){//if a map is selected
			id=(Integer)l.getSelectedIndices().get(0);
		}else{
			id=-1;
		}
		return id;
	}
	
	public void select_component(){
		//first get the correct component
		int id=selected_component_id();
		if(id<0){
			return;
		}
		hud.select_component(scene.world.model.components().get(id));
		//refresh model
		scene.world.refresh_model();
	}
	
	//add new component methods
	public void add_component_quad(){
		//make new component
		Component c=new Component_Quad(0,0,0,1,1);
		scene.world.model.add_component(c);
		//refresh hud
		hud.refresh();
		//refresh model
		scene.world.refresh_model();
	}
	
	public void add_component_box(){
		//make new component
		Component c=new Component_Box(0.5f,0.5f,0.5f,1,1,1);
		scene.world.model.add_component(c);
		//refresh hud
		hud.refresh();
		//refresh model
		scene.world.refresh_model();
	}
	
	public void add_component_cylinder(){
		//make new component
		Component c=new Component_Cylinder(0.5f,0.5f,0.5f,0.5f,1);
		scene.world.model.add_component(c);
		//refresh hud
		hud.refresh();
		//refresh model
		scene.world.refresh_model();
	}
	
	public void add_component_sphere(){
		//make new component
		Component c=new Component_Sphere(0.5f,0.5f,0.5f,0.5f);
		scene.world.model.add_component(c);
		//refresh hud
		hud.refresh();
		//refresh model
		scene.world.refresh_model();
	}
	
	public void add_component_light(){
		//make new component
		Component c=new Component_Light(0,0,0,0.8f,5);
		scene.world.model.add_component(c);
		//refresh hud
		hud.refresh();
		//refresh model
		scene.world.refresh_model();
	}
	
	//texture methods
	public void open_change_texture_window(){
		//call the hud's method
		hud.open_window_change_texture();
	}
	
	//component manipulation panel methods
	
	public void delete_selected_component(){
		Component c=hud.current_component();
		scene.world.model.remove_component(c);
		hud.select_component(new Component());
		scene.world.refresh_model();
		hud.refresh();
	}
	
	public void restore_component_entries(){
		//just refresh the hud
		hud.refresh();
	}
	
	public void commit_changes(){
		Component c=hud.current_component();  //just for convenience
		
		//get entry strings for parsing
		String xfl=hud.panel.getScreen("start").findNiftyControl("component x entry",TextField.class).getText();
		String yfl=hud.panel.getScreen("start").findNiftyControl("component y entry",TextField.class).getText();
		String zfl=hud.panel.getScreen("start").findNiftyControl("component z entry",TextField.class).getText();
		String sxfl=hud.panel.getScreen("start").findNiftyControl("component sx entry",TextField.class).getText();
		String syfl=hud.panel.getScreen("start").findNiftyControl("component sy entry",TextField.class).getText();
		String szfl=hud.panel.getScreen("start").findNiftyControl("component sz entry",TextField.class).getText();
		String rxfl=hud.panel.getScreen("start").findNiftyControl("component rx entry",TextField.class).getText();
		String ryfl=hud.panel.getScreen("start").findNiftyControl("component ry entry",TextField.class).getText();
		String rzfl=hud.panel.getScreen("start").findNiftyControl("component rz entry",TextField.class).getText();
		String ufl=hud.panel.getScreen("start").findNiftyControl("component u entry",TextField.class).getText();
		String vfl=hud.panel.getScreen("start").findNiftyControl("component v entry",TextField.class).getText();
		
		//set default new float values (old ones)
		float x=c.x();
		float y=c.y();
		float z=c.z();
		float sx=c.size_x();
		float sy=c.size_y();
		float sz=c.size_z();
		float rx=c.rot_x();
		float ry=c.rot_y();
		float rz=c.rot_z();
		float u=c.texture_u();
		float v=c.texture_v();
		
		//try and parse each entry (if fails, the default value will remain)
		try{x=Float.parseFloat(xfl);}catch(Exception ex){}
		try{y=Float.parseFloat(yfl);}catch(Exception ex){}
		try{z=Float.parseFloat(zfl);}catch(Exception ex){}
		try{sx=Float.parseFloat(sxfl);}catch(Exception ex){}
		try{sy=Float.parseFloat(syfl);}catch(Exception ex){}
		try{sz=Float.parseFloat(szfl);}catch(Exception ex){}
		try{rx=Float.parseFloat(rxfl);}catch(Exception ex){}
		try{ry=Float.parseFloat(ryfl);}catch(Exception ex){}
		try{rz=Float.parseFloat(rzfl);}catch(Exception ex){}
		try{u=Float.parseFloat(ufl);}catch(Exception ex){}
		try{v=Float.parseFloat(vfl);}catch(Exception ex){}
			
		//commit changes
		c.set(x,y,z,sx,sy,sz,rx,ry,rz);
		c.set_uv(u,v);
		//refresh the model
		scene.world.refresh_model();
		//refresh the hud
		hud.refresh();
	}
	
	//load models window methods
	
	public void cancel_load_model(){
		//close window with hud's method
		hud.close_window_load_model();
	}
	
	public void load_selected_model(){
		//first, get the index of the selected model
		ListBox l=hud.panel.getScreen("start").findNiftyControl("list model load",ListBox.class);
		if(l.getSelectedIndices().size()>0){
			int index=(Integer)l.getSelectedIndices().get(0);
			//call hud's method for loading this index
			Model m=hud.model_to_load(index);
			//load model
			scene.world.display_model(m);
			//de-select component
			hud.select_component(new Component());
		}
		
		//close window
		hud.close_window_load_model();
	}
	
	//select texture window methods
	public void show_texture_library(){
		//first get the name of the correct library
		ListBox l=hud.panel.getScreen("start").findNiftyControl("texture library select",ListBox.class);
		if(l.getSelection().size()>0){
			hud.show_texture_library((String)l.getSelection().get(0));
		}else{
			//refresh the hud to prevent button sticking
			hud.refresh();
		}
	}
	
	public void change_texture(){
		//first, set the new texture and refresh the model
		ListBox l=hud.panel.getScreen("start").findNiftyControl("texture select",ListBox.class);
		if(l.getSelection().size()>0){
			hud.current_component().set_texture((String)l.getSelection().get(0));
			scene.world.refresh_model();
		}
		//close texture change window
		hud.close_window_change_texture();
	}
	
	public void cancel_change_texture(){
		//call hud's method to close the window
		hud.close_window_change_texture();
	}

}
