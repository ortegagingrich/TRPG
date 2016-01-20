package game.scripting;

import java.util.ArrayList;

//handles execution of scripts/thread management
public class Interpreter {
	
	//each running script will have its own thread set to 60 fps; interpreter handles starting/stopping/resuming
	
	//Note:
	//Action Holds: freeze player control; parallel event processes still continue
	//Hold For: actually stops event processes (except for the process calling it) until they are resumed.
	private ArrayList<Event_Process> processes; //arraylist of event threads
	private ArrayList<Object> action_holds; //arraylist of objects placing a hold upon action; if none present, action is allowed
	private boolean active;
	
	public Interpreter(){
		action_holds=new ArrayList<Object>();
		processes=new ArrayList<Event_Process>();
		active=false;
	}
	
	//checks to see if interpreter is active
	public boolean active(){
		return active;
	}
	
	//activate interpreter for map events
	public void activate(){
		active=true;
	}
	
	//add process
	public void add_process(Event e){
		//make new process for the event
		Event_Process p=new Event_Process(e);
		//add process
		processes.add(p);
		//start thread
		new Thread(p).start();
	}
	
	public void finish_process(Event_Process p){
		processes.remove(p);
	}
	
	//checks to see if actions are suppressed
	public boolean action_allowed(){
		return action_holds.size()==0;
	}
	
	public void hold_action(Object o){
		action_holds.add(o);
	}
	
	public void release_action(Object o){
		action_holds.remove(o);
	}
	
	//resume/pause methods
	
	public void pause_for(Event_Process hold){
		//pause all event processes, except for the one initiating the hold
		for(Event_Process p:processes){
			if(p!=hold){
				p.pause();
			}
		}
	}
	
	public void pause_all(){
		//set inactive
		active=false;
		//pause all event processes
		for(Event_Process p:processes){
			p.pause();
		}
	}
	
	public void resume_all(){
		//set active
		active=true;
		//resume all event processes
		for(Event_Process p:processes){
			p.resume();
		}
	}
	
	public void stop_all(){
		//stop all event processes
		for(Event_Process p:processes){
			p.stop();
		}
	}
	
	public void stop_except(Event_Process e){
		for(Event_Process p:processes){
			if(p!=e){
				p.stop();
			}
		}
	}
	
}
