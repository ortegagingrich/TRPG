package io.game_dump;

import game.scripting.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Dump_Entity_Event extends Dump_Entity implements Serializable {
	public static final long serialVersionUID=1L;
	
	public ArrayList<Event> events;
	public String name;
	
	public Dump_Entity_Event(){
		super();
		events=new ArrayList<Event>();
	}
}
