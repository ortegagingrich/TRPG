package game.gui.edit;

import game.map.Map;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Main;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import asset.model.*;

import com.jme3.math.*;

public class Controller_Edit implements ScreenController {
	
	private HUD_Edit hud;
	private Scene_WorldEdit scene;
	
	
	public Controller_Edit(HUD_Edit h,Scene_WorldEdit s){
		scene=s;
		hud=h;
	}

	@Override
	public void bind(Nifty n, Screen s) {
		
	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {

	}
	
	//button methods:
	
	//Edit HUD:
	//change mode buttons:
	
	public void set_mode_start(){
		set_mode("start");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.show();
	}
	
	public void set_mode_floor(){
		set_mode("floor");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.show();
	}
	
	public void set_mode_enemy(){
		set_mode("enemy");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.show();
	}
	
	public void set_mode_event(){
		set_mode("event");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.show();
		scene.world.secondary_selector.follow_primary_selector=false;
		scene.world.map.floor.hide();
	}
	
	public void set_mode_static(){
		set_mode("static");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.hide();
	}
	
	public void set_mode_light(){
		set_mode("light");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.hide();
	}
	
	public void set_mode_terrain(){
		set_mode("terrain");
		scene.world.selector.set_mode_tile();
		scene.world.secondary_selector.hide();
		scene.world.secondary_selector.follow_primary_selector=true;
		scene.world.map.floor.show();
	}
	
	public void set_mode(String s){
		hud.mode=s;
		hud.refresh();
	}
	
	//Map panel
	
	//retrieve selected map id
	private int selected_map_id(){
		//retrieve selected map ID
		ListBox l=hud.panel.getScreen("start").findNiftyControl("maps",ListBox.class);
		int id;
		if(l.getSelection().size()!=0){//if a map is selected
			id=Integer.parseInt(((String)l.getSelection().get(0)).substring(1,4));
		}else{
			id=-1;
		}
		return id;
	}
	
	public void open_map_rename(){
		//retrieve selected map ID
		int id=selected_map_id();
		Map m=Process_Main.quest.universe.get_map(id);
		if(m!=null){//if can't find map, do nothing (e.g. none selected)
			hud.open_map_rename_window(Process_Main.quest.universe.get_map(id));
		}
	}
	
	public void close_map_rename(){
		hud.close_map_rename_window();
	}
	
	public void load_map(){
		//retrieve selected map ID
		int id=selected_map_id();
		Map m=Process_Main.quest.universe.get_map(id);
		//if map found, display it
		if(m!=null){
			scene.world.display_map(m);
		}
	}
	
	public void new_map(){
		Process_Main.quest.universe.add_map();
		hud.refresh();
	}
	
	public void confirm_delete_map(){
		int id=selected_map_id();
		Map m=Process_Main.quest.universe.get_map(id);
		if(m!=null&&scene.world.map!=m){
			hud.open_map_delete_confirm_window(m);
		}
	}
	
	public void delete_map(){
		hud.delete_map();
	}
	
	public void cancel_delete_map(){
		hud.close_map_delete_window();
	}
	
	//Static Entities panel
	
	public void select_model(){
		//first get the selected model
		ListBox l=hud.panel.getScreen("start").findNiftyControl("list models",ListBox.class);
		if(l.getSelectedIndices().size()>0){
			int id=(Integer)l.getSelectedIndices().get(0);
			Model m=hud.current_library().models_condensed().get(id);
			//select the model
			hud.select_model(m);
			//show model with selector
			scene.world.selector.show_model(m);
		}
	}
	
	public void deselect_model(){
		hud.select_model(null);
		//hide selector model
		scene.world.selector.hide_model();
	}
	
	//Lights panel
	
	public void confirm_light_changes(){
		//just for convenience
		Map m=scene.world.map;
		
		//get entry strings for parsing
		String arfl=hud.panel.getScreen("start").findNiftyControl("ambient r",TextField.class).getText();
		String agfl=hud.panel.getScreen("start").findNiftyControl("ambient g",TextField.class).getText();
		String abfl=hud.panel.getScreen("start").findNiftyControl("ambient b",TextField.class).getText();
		String aafl=hud.panel.getScreen("start").findNiftyControl("ambient a",TextField.class).getText();
		String aifl=hud.panel.getScreen("start").findNiftyControl("ambient i",TextField.class).getText();
		String d1rfl=hud.panel.getScreen("start").findNiftyControl("dir1 r",TextField.class).getText();
		String d1gfl=hud.panel.getScreen("start").findNiftyControl("dir1 g",TextField.class).getText();
		String d1bfl=hud.panel.getScreen("start").findNiftyControl("dir1 b",TextField.class).getText();
		String d1afl=hud.panel.getScreen("start").findNiftyControl("dir1 a",TextField.class).getText();
		String d1ifl=hud.panel.getScreen("start").findNiftyControl("dir1 i",TextField.class).getText();
		String d2rfl=hud.panel.getScreen("start").findNiftyControl("dir2 r",TextField.class).getText();
		String d2gfl=hud.panel.getScreen("start").findNiftyControl("dir2 g",TextField.class).getText();
		String d2bfl=hud.panel.getScreen("start").findNiftyControl("dir2 b",TextField.class).getText();
		String d2afl=hud.panel.getScreen("start").findNiftyControl("dir2 a",TextField.class).getText();
		String d2ifl=hud.panel.getScreen("start").findNiftyControl("dir2 i",TextField.class).getText();
		String d1xfl=hud.panel.getScreen("start").findNiftyControl("dir1 x",TextField.class).getText();
		String d1yfl=hud.panel.getScreen("start").findNiftyControl("dir1 y",TextField.class).getText();
		String d1zfl=hud.panel.getScreen("start").findNiftyControl("dir1 z",TextField.class).getText();
		String d2xfl=hud.panel.getScreen("start").findNiftyControl("dir2 x",TextField.class).getText();
		String d2yfl=hud.panel.getScreen("start").findNiftyControl("dir2 y",TextField.class).getText();
		String d2zfl=hud.panel.getScreen("start").findNiftyControl("dir2 z",TextField.class).getText();
		
		//set default values (old ones)
		float ar=m.ambient_color.getRed();
		float ag=m.ambient_color.getGreen();
		float ab=m.ambient_color.getBlue();
		float aa=m.ambient_color.getAlpha();
		float ai=m.ambient_intensity;
		float d1r=m.directional1_color.getRed();
		float d1g=m.directional1_color.getGreen();
		float d1b=m.directional1_color.getBlue();
		float d1a=m.directional1_color.getAlpha();
		float d1i=m.directional1_intensity;
		float d2r=m.directional2_color.getRed();
		float d2g=m.directional2_color.getGreen();
		float d2b=m.directional2_color.getBlue();
		float d2a=m.directional2_color.getAlpha();
		float d2i=m.directional2_intensity;
		float d1x=m.directional1_vector.x;
		float d1y=m.directional1_vector.y;
		float d1z=m.directional1_vector.z;
		float d2x=m.directional2_vector.x;
		float d2y=m.directional2_vector.y;
		float d2z=m.directional2_vector.z;
		
		//try and parse each entry (if fails, the default value will remain)
		try{ar=Float.parseFloat(arfl);}catch(Exception e){}
		try{ag=Float.parseFloat(agfl);}catch(Exception e){}
		try{ab=Float.parseFloat(abfl);}catch(Exception e){}
		try{aa=Float.parseFloat(aafl);}catch(Exception e){}
		try{ai=Float.parseFloat(aifl);}catch(Exception e){}
		try{d1r=Float.parseFloat(d1rfl);}catch(Exception e){}
		try{d1g=Float.parseFloat(d1gfl);}catch(Exception e){}
		try{d1b=Float.parseFloat(d1bfl);}catch(Exception e){}
		try{d1a=Float.parseFloat(d1afl);}catch(Exception e){}
		try{d1i=Float.parseFloat(d1ifl);}catch(Exception e){}
		try{d2r=Float.parseFloat(d2rfl);}catch(Exception e){}
		try{d2g=Float.parseFloat(d2gfl);}catch(Exception e){}
		try{d2b=Float.parseFloat(d2bfl);}catch(Exception e){}
		try{d2a=Float.parseFloat(d2afl);}catch(Exception e){}
		try{d2i=Float.parseFloat(d2ifl);}catch(Exception e){}
		try{d1x=Float.parseFloat(d1xfl);}catch(Exception e){}
		try{d1y=Float.parseFloat(d1yfl);}catch(Exception e){}
		try{d1z=Float.parseFloat(d1zfl);}catch(Exception e){}
		try{d2x=Float.parseFloat(d2xfl);}catch(Exception e){}
		try{d2y=Float.parseFloat(d2yfl);}catch(Exception e){}
		try{d2z=Float.parseFloat(d2zfl);}catch(Exception e){}
		
		//commit changes
		m.ambient_color=new ColorRGBA(ar,ag,ab,aa);
		m.ambient_intensity=ai;
		m.directional1_color=new ColorRGBA(d1r,d1g,d1b,d1a);
		m.directional1_intensity=d1i;
		m.directional1_vector=new Vector3f(d1x,d1y,d1z);
		m.directional2_color=new ColorRGBA(d2r,d2g,d2b,d2a);
		m.directional2_intensity=d2i;
		m.directional2_vector=new Vector3f(d2x,d2y,d2z);
		//refresh scene lights
		m.refresh_lights();
		//refresh the hud
		hud.refresh();
	}
	
	public void cancel_light_changes(){
		//just refresh the hud
		hud.refresh();
	}
	
	//Terrain Panel
	
	public void toggle_terrain_visible(){
		//toggle visibility
		scene.world.map.terrain.visible=!scene.world.map.terrain.visible;
		//refresh the terrain
		scene.world.map.refresh_terrain();
		//refresh the hud
		hud.refresh();
	}
	
	public void change_heightmap(){
		try{
			//first get new heightmap name
			String h=hud.panel.getScreen("start").findNiftyControl("heightmap name",TextField.class).getText();
			//set texture and refresh terrain
			scene.world.map.terrain.heightmap_name=h;
			scene.world.map.refresh_terrain();
			//refresh the hud
			hud.refresh();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	public void change_texture_r(){
		change_texture("r");
	}
	
	public void change_texture_g(){
		change_texture("g");
	}
	
	public void change_texture_b(){
		change_texture("b");
	}
	
	private void change_texture(String t){
		//open hud window
		hud.open_terrain_texture_window(t);
	}
	
	public void change_terrain_height(){
		//first get the string to parse
		String sheight=hud.panel.getScreen("start").findNiftyControl("terrain height",TextField.class).getText();
		//set default value
		float height=scene.world.map.terrain.base_height;
		//try and parse string
		try{height=Float.parseFloat(sheight);}catch(Exception e){}
		//set new height and refresh terrain and hud
		scene.world.map.terrain.base_height=height;
		scene.world.map.refresh_terrain();
		hud.refresh();
	}
	
	//terrain texture window
	
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
	
	public void accept_texture_change(){
		//first, set the new texture and refresh the model
		ListBox l=hud.panel.getScreen("start").findNiftyControl("texture select",ListBox.class);
		if(l.getSelection().size()>0){
			hud.set_terrain_texture((String)l.getSelection().get(0));
			scene.world.map.refresh_terrain();
		}
		//close texture change window
		hud.close_terrain_texture_window();
	}
	
	public void cancel_texture_change(){
		//just close the hud window
		hud.close_terrain_texture_window();
	}

}