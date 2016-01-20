package game.scene;

import game.gui.edit.HUD_Edit;
import game.map.entity.Entity_Event;
import game.system.Clipboard;
import game.system.Input;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Scene_WorldEdit extends Scene {
	
	public World world;
	public HUD_Edit hud;
	
	private Entity_Event selected_event_entity;
	
	public Scene_WorldEdit(){
		super();
		world=new World(this);
		world.show_world();
		world.display_map(Process_Main.quest.universe.get_map(0));
		hud=new HUD_Edit("default",this);
	}
	
	public void update(){
		super.update();
		
		//update camera
		update_camera();
		
		//temporary: force hud refresh
		if(Process_Main.$input.trigger("R")){
			hud.refresh();
		}
		
		//update hud
		hud.update();
		
		if(update_action()){
			return;
		}
		
		//update world
		world.update();
		
	}
	
	//boolean indicates whether to skip rest of update
	private boolean update_action(){
		//for convenience get input first
		Input input=Process_Main.$input;
		
		//if E is pressed, save default file
		if(input.trigger("E")){
			Process_Main.save_default_quest();
		}
		
		//if J is pressed, open/close journal edit window
		if(input.trigger("J")){
			hud.journal_window.toggle();
		}
		
		//if M is pressed, switch to model mode
		if(input.trigger("M")){
			//save default quest
			Process_Main.save_default_quest();
			
			//end mode
			end_mode();
			//transition to model edit mode
			Process_Main.$scene=new Scene_Model();
			return true;
		}
		
		
		//if P is presses, save map and start quest
		if(input.trigger("P")){
			//save default quest
			Process_Main.save_default_quest();
			
			//end mode
			end_mode();
			//turn quest into game file
			Process_Main.quest.start();
			//transition to explore mode
			Process_Main.$scene=new Scene_Explore();
			return true;
		}
		
		//toggle grid
		if(input.trigger("G")){
			world.grid.toggle();
		}
		
		
		//actions which follow are skipped if HUD interaction occurs
		if(hud.skip||hud.frozen){
			hud.skip=false;
			return false;
		}
		
		//change selector height
		if(input.trigger("Add")){
			world.selector.y++;
		}
		
		if(input.trigger("Subtract")){
			world.selector.y--;
		}
		
		if(hud.mode.equals("floor")){
			
			if(input.trigger("Mouse Left")||input.trigger("Mouse Right")){
				world.selector.anchor();
			}
			
			if(input.released("Mouse Left")&&world.selector.anchored()){
				world.map.floor.add_tiles(world.selector.anchorx,world.selector.x,world.selector.anchorz,world.selector.z);
				world.selector.anchor_release();
			}
			
			if(input.released("Mouse Right")&&world.selector.anchored()){
				world.map.floor.remove_tiles(world.selector.anchorx,world.selector.x,world.selector.anchorz,world.selector.z);
				world.selector.anchor_release();
			}
		}else if(hud.mode.equals("static")){
			
			//if left click and model selected, place new static entity with that model
			if(input.trigger("Mouse Left")&&hud.selected_model()!=null){
				world.map.add_entity_static(world.selector.x,world.selector.y,world.selector.z,world.selector.facing(),hud.selected_model().id,hud.current_library_id());
			}
			//temporary: if right click, try and delete a static entity located at the selector location
			if(input.trigger("Mouse Right")){
				world.map.remove_entity_static(world.selector.x,world.selector.y,world.selector.z);
			}
			
			//if Comma/Period pressed, rotate the selector
			if(input.trigger("Comma")){
				world.selector.rotate_left();
			}
			if(input.trigger("Period")){
				world.selector.rotate_right();
			}
			
		}else if(hud.mode.equals("enemy")){
			
			if(input.trigger("Mouse Left")){
				world.map.add_entity_enemy("enemy",world.selector.x,world.selector.y,world.selector.z);
			}
			
			if(input.trigger("Mouse Right")){
				world.map.remove_entity_enemy(world.selector.x,world.selector.z);
			}
		}else if(hud.mode.equals("start")){
			
			if(input.trigger("Mouse Left")){
				Process_Main.quest.universe.set_start(world.map.id(),world.selector.x,world.selector.y,world.selector.z);
			}
		}else if(hud.mode.equals("event")){
			//if left click, snap secondary selector to primary
			if(input.pressed("Mouse Left")){
				world.secondary_selector.snap_to_primary();
				if(selected_event_entity!=null){
					selected_event_entity.set_position(world.selector.x,world.selector.y,world.selector.z);
				}
				//set selected event entity
				if(input.trigger("Mouse Left")){
					selected_event_entity=world.map.retrieve_entity_event(world.secondary_selector.x,world.secondary_selector.z);
				}
			}else{
				//de-select event entity
				selected_event_entity=null;
			}
			//move secondary selector with right click too
			if(input.pressed("Mouse Right")){
				world.secondary_selector.snap_to_primary();
			}
			
			//handle right click menu
			//if left click, hide the window
			if(input.trigger("Mouse Left")){
				hud.event_right_click_window.hide();
			}
			//if right click, show the window for the selected event
			if(input.trigger("Mouse Right")){
				selected_event_entity=world.map.retrieve_entity_event(world.secondary_selector.x,world.secondary_selector.z);
				hud.event_right_click_window.show(selected_event_entity);
			}
			
			if(input.double_trigger("Mouse Left")&&!input.pressed("Shift")){
				hud.event_window.show(world.map.retrieve_entity_event(world.secondary_selector.x,world.secondary_selector.z));
			}
			if(input.trigger("C")&&input.pressed("Control")){
				//copy selected event (if there is one)
				Entity_Event e=world.map.retrieve_entity_event(world.secondary_selector.x,world.secondary_selector.z);
				if(e!=null){
					Clipboard.save_entity_event(e);
				}
			}
			if(input.trigger("V")&&input.pressed("Control")){
				//retrieve new entity
				Entity_Event e=Clipboard.get_entity_event();
				if(e!=null){
					//move the new entity to the secondary selector
					e.set_position(world.secondary_selector.x,world.secondary_selector.y,world.secondary_selector.z);
					//add entity to the map
					world.map.add_entity_event(e);
				}
			}
			if(input.trigger("Delete")){
				world.map.remove_entity_event(world.secondary_selector.x,world.secondary_selector.z);
			}
		}
		return false;
	}
	
	private static final float camera_move_unit=0.2f;
	private void update_camera(){
		if(Process_Main.$input.pressed("W")){
			Process_Graphics.$graphics.camz+=camera_move_unit;
			hud.hide_right_click_menus();
		}
		if(Process_Main.$input.pressed("S")){
			Process_Graphics.$graphics.camz-=camera_move_unit;
			hud.hide_right_click_menus();
		}
		if(Process_Main.$input.pressed("A")){
			Process_Graphics.$graphics.camx+=camera_move_unit;
			hud.hide_right_click_menus();
		}
		if(Process_Main.$input.pressed("D")){
			Process_Graphics.$graphics.camx-=camera_move_unit;
			hud.hide_right_click_menus();
		}
	}
	
	
	private void end_mode(){
		world.hide_world();
		hud.dispose_panel();
	}

}
