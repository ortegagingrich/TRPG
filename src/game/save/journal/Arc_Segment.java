package game.save.journal;

import game.scripting.Variable_Handler;

import java.io.Serializable;

//class of arc segments (short term objectives)
public class Arc_Segment implements Serializable {
	public final static long serialVersionUID=1L;
	
	public Variable_Handler variable_handler;
	
	private String parent_identifier;
	
	private int id;
	private String name;
	private String description;
	private boolean started;
	private boolean finished;
	
	public Arc_Segment(int i,String pid){
		id=i;
		parent_identifier=pid;
		name=Integer.toString(id)+"---";
		description="New Segment";
		variable_handler=new Variable_Handler(parent_identifier,Integer.toString(id));
		started=false;
		finished=false;
	}
	
	public int id(){
		return id;
	}
	
	public String parent_identifier(){
		return parent_identifier;
	}
	
	public String name(){
		return name;
	}
	
	public String description(){
		return description;
	}
	
	//construction methods
	
	public void deprecate(){
		id=-1;
	}
	
	public void rename(String n){
		name=n;
	}
	
	public void change_description(String d){
		description=d;
	}
	
	//start/finish operations:
	
	public boolean started(){
		return started;
	}
	
	public boolean active(){
		return started&&!finished;
	}
	
	public boolean finished(){
		return finished;
	}
	
	public void start(){
		started=true;
	}
	
	public void finish(){
		finished=true;
		//just in case
		started=true;
	}
	
}
