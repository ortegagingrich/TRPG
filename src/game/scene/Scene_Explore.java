package game.scene;

import game.gui.*;
import game.gui.in_game.HUD_Main;
import game.map.Map;
import game.map.entity.Entity_Event;
import game.map.entity.Entity_Player;
import game.scripting.*;
import game.system.Input;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Scene_Explore extends Scene {
	
	public World world;
	public boolean movement_fixed;  //boolean indicating if player entities are not allowed to move on their own
	
	public HUD_Main hud;  //main hud
	
	
	public Scene_Explore(){ //method called the first time the game is loaded
		super();
		//construct hud
		hud=new HUD_Main();
		//construct world
		world=new World(this);
		newmap=Process_Main.quest.universe.get_map(Process_Main.quest.m);
		changemap();
		movement_fixed=false;
	}
	
	public Scene_Explore(Scene_Initiative s){
		super();
		//set hud to appropriate screen
		hud.switch_main();
		//resume interpreter processing
		Process_Main.quest.interpreter.resume_all();
		//set world
		world=s.world;
		movement_fixed=false;
	}
	
	//methods for changing maps
	private Map newmap;
	
	private void changemap(){
		world.display_map(Process_Main.quest.universe.get_map(Process_Main.quest.m));
		world.prepare_map();
		world.show_world();
		newmap=null;
	}
	
	public void transition_to_map(Map m){
		newmap=m;
	}
	
	//update method
	
	public void update(){
		
		super.update();
		
		//if need to transition to a new map, execute that transition
		if(newmap!=null){
			changemap();
		}
		
		//update camera
		update_camera();
		
		update_action();
		
		//update hud
		hud.update();
		
		//update world
		world.update();
	}
	
	private void update_camera(){
		//temporary: set camera fixed relative to player
		Process_Graphics.$graphics.camx=world.map.player().x;
		Process_Graphics.$graphics.camz=world.map.player().z-15;
		Process_Graphics.$graphics.camy=world.map.player().y+10;
	}
	
	private void update_action(){
		//if action not allowed by event processing, return
		if(!Process_Main.quest.interpreter.action_allowed()){
			return;
		}
		
		//for convenience get input first
		Input input=Process_Main.$input;
		
		//start initiative if space pressed, or an enemy has become aware
		if(input.trigger("Space")||world.map.enemy_aware_of_party()){
			start_initiative();
			return;
		}
		
		//check event interaction
		if(input.trigger("Mouse Left")){
			//find an event at the selector coordinates
			Entity_Event target=world.map.retrieve_entity_event(world.selector.x,world.selector.z);
			//if there is an event at the target location, try to interact with it
			if(target!=null){
				target.interact();
			}
		}
		
		//temporary: move player; also have the player face the correct direction,regardless of whether the move is successful
		String order="";
		if(input.pressed("W")){
			order="u";
			world.map.player().face_up();
		}else if(input.pressed("S")){
			order="d";
			world.map.player().face_down();
		}
		if(input.pressed("A")){
			order="l";
			world.map.player().face_left();
		}else if(input.pressed("D")){
			order="r";
			world.map.player().face_right();
		}
		for(Entity_Player p:world.map.players()){
			//if sufficiently close to main player, move
			if(p.taxi_distance_from(world.map.player())<3&&!p.moving()){
				p.move(order);
			}
		}
		
		//if right clicked, have character face towards the selector
		if(input.trigger("Mouse Right")){
			world.map.player().face_towards(world.selector.x,world.selector.z);
		}
		
		//check cycle characters;
		if(input.trigger("Period")){
			world.map.cycle_player();
		}
		if(input.trigger("Comma")){
			world.map.cycle_player_rev();
		}
		
		//if enter pressed, toggle character movement fixed
		if(input.trigger("Enter")){
			movement_fixed=!movement_fixed;
		}
		
	}
	
	private void start_initiative(){
		Process_Main.$scene=new Scene_Initiative(this);
	}

}
