package game.scripting;

import game.*;
import game.system.Process_Main;

//class of event execution processes; run as separate threads; one for each event
public class Event_Process implements Runnable {
	
	private Event event;
	
	public Event_Process(Event e){
		event=e;
	}
	
	public Event get_event(){
		return event;
	}

	@Override
	public void run() {
		//execute the corresponding event script
		event.script.execute(this);
		//remove self from interpreter list
		Process_Main.quest.interpreter.finish_process(this);
		//set event to not running
		event.stoprunning();
	}
	
	//pause/resume methods:
	public void pause(){
		event.script.pause();
	}
	
	public void resume(){
		event.script.resume();
	}
	
	public void stop(){
		event.script.stop();
	}
}
