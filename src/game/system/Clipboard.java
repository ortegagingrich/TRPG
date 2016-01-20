package game.system;

import game.map.entity.Entity_Event;

//clipboard for saving objects; note that all fields are static and contain the original
public class Clipboard {
	
	//copied event entities
	private static Entity_Event entity_event;
	
	public static void save_entity_event(Entity_Event e){
		entity_event=e;
	}
	
	public static Entity_Event get_entity_event(){
		if(entity_event==null){
			return null;
		}else{
			return entity_event.make_copy();
		}
	}
	
}
