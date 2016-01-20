package game.scripting;

import java.io.Serializable;
import java.util.ArrayList;
import game.*;
import game.map.entity.Entity_Event;
import game.scripting.trigger.*;
import game.system.Process_Main;

//class of events; contain scripts and conditions; attached to an event entity
public class Event implements Serializable {
	public static final long serialVersionUID=1L;
	
	public String name="Event";
	
	public Command_Block script;

	private ArrayList<Condition> existence_conditions;
	private ArrayList<Condition> interact_conditions;
	private ArrayList<Condition> autostart_conditions;
	
	private boolean running;
	
	public Event(){
		//make command block
		script=new Command_Block(0);
		//make condition arraylists
		existence_conditions=new ArrayList<Condition>();
		interact_conditions=new ArrayList<Condition>();
		autostart_conditions=new ArrayList<Condition>();
		//by default, there is an impossible auto-start condition
		autostart_conditions.add(new Condition());
		//not running initially
		running=false;
		
		/*//debug; remove immediately
		script.add_command();
		Command_Message c=new Command_Message(1);
		c.set_text("Hello World!  Jacob Ortega-Gingrich has just managed to transcend the limitations of this platform to create something extraordinary.  If all goes as planned, this game shall finally be completed.");
		script.get_commands().set(0,c);
		script.add_command();
		Command_Message c1=new Command_Message(1);
		c1.set_text("Isn't this message system great?");
		script.get_commands().set(1,c1);
		script.add_command();
		Command_Message c2=new Command_Message(1);
		c2.set_text("You currently have @money credits");
		script.get_commands().set(2,c2);*/
	}
	
	//check to see if event conditions are met
	public boolean existence_conditions_met(Entity_Event parent){
		for(Condition c:existence_conditions){
			if(!c.satisfied(this,parent)){
				return false;
			}
		}
		return true;
	}
	
	public boolean interact_conditions_met(Entity_Event parent){
		//first, check existence conditions
		if(!existence_conditions_met(parent)){
			return false;
		}
		//check interact conditions
		for(Condition c:interact_conditions){
			if(!c.satisfied(this,parent)){
				return false;
			}
		}
		return true;
	}
	
	public boolean autostart_conditions_met(Entity_Event parent){
		//first, check existence conditions
		if(!existence_conditions_met(parent)){
			return false;
		}
		//check auto-start conditions
		for(Condition c:autostart_conditions){
			if(!c.satisfied(this,parent)){
				return false;
			}
		}
		return true;
	}
	
	//return conditions
	public ArrayList<Condition> existence_conditions(){
		return existence_conditions;
	}
	
	public ArrayList<Condition> interact_conditions(){
		return interact_conditions;
	}
	
	public ArrayList<Condition> autostart_conditions(){
		return autostart_conditions;
	}
	
	//checks to see if event is running
	public boolean running(){
		return running;
	}
	
	//sets the event to not running
	public void stoprunning(){
		running=false;
	}
	
	//execute event
	public void execute(){
		running=true;
		//start interpreter process
		Process_Main.quest.interpreter.add_process(this);
	}
}
