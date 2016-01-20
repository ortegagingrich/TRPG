package game.save;

import game.map.Universe;
import game.save.journal.Journal;
import game.scripting.*;
import io.game_dump.Dump_Quest;

//game file class; contains all data related to quest as well as updated maps
public class Quest {
	
	public String label;
	
	public Universe universe;
	public Party party;
	public Journal journal;
	public Interpreter interpreter;
	
	//party location coordinates; used for starting saves and for map transitions
	public int m;
	public int x;
	public int y;
	public int z;
	
	public Quest(String l){
		label=l;
		universe=new Universe();
		party=new Party();
		journal=new Journal();
		interpreter=new Interpreter();
		m=0;
		x=0;
		y=0;
		z=0;
	}
	
	//this method modifies the quest object to turn it from a template into an actual game file; this will be an important method
	public void start(){
		//set party location
		m=universe.startm;
		x=universe.startx;
		y=universe.starty;
		z=universe.startz;
		//start party
		party.start();
		//activate interpreter
		interpreter.activate();
	}
	
	
	public void write(Dump_Quest d){
		d.label=label;
		party.write(d.party);
		universe.write(d.universe);
		d.journal=journal;
		d.m=m;
		d.x=x;
		d.y=y;
		d.z=z;
	}
	
	public void read(Dump_Quest d){
		label=d.label;
		party.read(d.party);
		universe.read(d.universe);
		journal=d.journal;
		m=d.m;
		x=d.x;
		y=d.y;
		z=d.z;
	}

}
