package game.save.journal;

import game.scripting.Variable_Handler;

import java.io.Serializable;
import java.util.ArrayList;

//class of story arcs used in journal (both main and side quests); Note that these are referred to as "quests" in-game
public class Arc implements Serializable {
	public final static long serialVersionUID=1L;
	
	public Variable_Handler variable_handler;
	
	private String identifier;
	private String name;
	private String description;
	private boolean started;
	private boolean finished;
	
	private ArrayList<Arc_Segment> segments;
	private int active_segment_id;
	
	public Arc(String i){
		identifier=i;
		name=identifier+"---";
		description="New Arc";
		variable_handler=new Variable_Handler(null,identifier);
		started=false;
		finished=false;
		//make empty list of segments
		segments=new ArrayList<Arc_Segment>();
		active_segment_id=-2;
	}
	
	public String identifier(){
		return identifier;
	}
	
	public String name(){
		return name;
	}
	
	public String description(){
		return description;
	}
	
	//construction methods:
	
	public void deprecate(){
		identifier=null;
	}
	
	public void rename(String n){
		name=n;
	}
	
	public void change_description(String d){
		description=d;
	}
	
	public void add_segment(){
		Arc_Segment s=new Arc_Segment(segments.size(),identifier);
		segments.add(s);
	}
	
	//runtime methods:
	
	public boolean started(){
		return started;
	}
	
	public boolean active(){
		return started&&!finished;
	}
	
	public boolean finished(){
		return finished;
	}
	
	public ArrayList<Arc_Segment> get_segments(){  //note: only returns non-deprecated segments
		ArrayList<Arc_Segment> l=new ArrayList<Arc_Segment>();
		for(Arc_Segment s:segments){
			if(s.id()!=-1){
				l.add(s);
			}
		}
		return l;
	}
	
	public Arc_Segment get_segment(int i){
		if(i<0){
			return null;
		}
		for(Arc_Segment s:segments){
			if(s.id()==i){
				return s;
			}
		}
		return null;
	}
	
	public Arc_Segment get_active_segment(){
		if(active_segment_id<0){
			return null;
		}
		for(Arc_Segment s:segments){
			if(s.id()==active_segment_id){
				return s;
			}
		}
		return null;
	}
	
	public void start(){
		started=true;
		active_segment_id=-1;
		advance();
	}
	
	public void advance(){
		//if there is an active segment, finish it it
		if(get_active_segment()!=null){
			get_active_segment().finish();
		}
		//advance to next available segment, or stop if all segments are completed
		active_segment_id++;
		while(get_active_segment()==null){
			active_segment_id++;
			if(active_segment_id>segments.size()){
				finish();
				return;
			}
		}
		//start next segment
		get_active_segment().start();
	}
	
	private void finish(){
		finished=true;
		//just in case:
		started=true;
	}

}
