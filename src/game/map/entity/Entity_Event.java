package game.map.entity;

import game.map.Map;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;
import game.system.graphics.Graphics;
import io.File;
import io.game_dump.Dump_Entity_Event;

import java.util.ArrayList;
import com.jme3.scene.*;


//class of event entities; basically a dynamic entity with an arraylist of events attached
public class Entity_Event extends Entity_Dynamic {
	
	public ArrayList<Event> events;  //arraylist
	
	private String name;
	
	public Entity_Event(float xi,float yi,float zi,Map m){
		super(xi,yi,zi,m,0.5f);
		//make event arraylist and first event
		events=new ArrayList<Event>();
		events.add(new Event());
		//set default fields
		name="EVENT";
		//retrieve model
		retrieve_model();
	}
	
	//pre-configured events
	
	public static Entity_Event make_teleport_event(float xi,float yi,float zi,Map m){
		//make new event entity
		Entity_Event e=new Entity_Event(xi,yi,zi,m);
		//set default conditions
		Event ev=e.events.get(0);
		ev.autostart_conditions().clear();
		ev.autostart_conditions().add(new game.scripting.trigger.Condition_Proximity(1));
		ev.interact_conditions().add(new game.scripting.trigger.Condition());
		//add command
		Command_Teleport_Player ct=new Command_Teleport_Player(1);
		ev.script.add_command(ct);
		//return event entity
		return e;
	}
	
	//check to see if tile is occupied
	@Override
	public boolean occupies_tile(){
		//temporary; always false
		return false;
	}
	
	//interaction methods:
	public void interact(){
		//find the first event (if any) with conditions met, execute its script and return
		for(Event e:events){
			if(e.interact_conditions_met(this)){
				e.execute();
				return;
			}
		}
	}
	
	//name methods
	@Override
	public String name(){
		return name;
	}
	
	//rename
	public void rename(String s){
		name=s;
	}
	
	//update method(s) (will eventually allow for automatic/parallel script execution by checking conditions every frame)
	@Override
	public void update(){
		//superclass method
		super.update();
		
		//only proceed if interpreter is active
		if(!Process_Main.quest.interpreter.active()){
			return;
		}
		
		//add processing for auto-start
		for(Event e:events){
			//if event is not running and conditions are met, execute
			if(!e.running()){
				if(e.autostart_conditions_met(this)){
					e.execute();
				}
			}
		}
	}
	
	//override update animation method
	@Override
	protected void update_animation(){
		//do nothing for now; later, check to see if event is visible and execute superclass method if so
		
	}
	
	//retrieve model method
	@Override
	public void retrieve_model(){
		//super.retrieve_model();
		//generic model
		//if no previous model, make a new node
		if(model==null){
			model=new Node();
		}else{
			Graphics.graphics_manager.detachAllChildren((Node)model);
		}
		Process_Graphics.$graphics.draw_box((Node)model,x+0.5f,y+0.5f,z+0.5f,0.5f,0.5f,0.5f);
		//set hitbox (none, by default)
		hitbox=new Node();
		//to prevent issues for later
		face="down";
	}
	
	//read/write methods
	public void write(Dump_Entity_Event d){
		super.write(d);
		d.name=name;
		d.events=events;
	}
	
	public void read(Dump_Entity_Event d){
		name=d.name;
		events=d.events;
		super.read(d);
	}
	
	//copy/clipboard methods
	
	public Entity_Event make_copy(){
		Entity_Event e=new Entity_Event(x,y,z,map);
		e.rename(name);
		e.events.clear();
		for(Event ev:events){
			e.events.add((Event)File.copy_serializable(ev));
		}
		return e;
	}
}
