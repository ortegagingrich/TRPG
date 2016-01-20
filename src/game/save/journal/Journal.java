package game.save.journal;

import game.scripting.Switch;
import game.scripting.Variable;

import java.io.Serializable;
import java.util.ArrayList;

//journal; each save file has one; keeps track of quests (arcs)
public class Journal implements Serializable {
	public final static long serialVersionUID=1L;
	
	//story arc lists
	private ArrayList<Arc> main_arcs;  //full list of story arcs from the main quest
	private ArrayList<Arc> side_arcs;  //full list of story arcs from side quests
	
	public Journal(){
		//make array lists for arcs
		main_arcs=new ArrayList<Arc>();
		side_arcs=new ArrayList<Arc>();
	}
	
	//find switch/variable methods
	public Switch find_switch(String[] address){
		if(address[0]==null){
			return get_arc(address[1]).variable_handler.get_switch(Integer.parseInt(address[2]));
		}else{
			return get_arc(address[0]).get_segment(Integer.parseInt(address[1])).variable_handler.get_switch(Integer.parseInt(address[2]));
		}
	}
	
	public Variable find_variable(String[] address){
		if(address[0]==null){
			return get_arc(address[1]).variable_handler.get_variable(Integer.parseInt(address[2]));
		}else{
			return get_arc(address[0]).get_segment(Integer.parseInt(address[1])).variable_handler.get_variable(Integer.parseInt(address[2]));
		}
	}
	
	//add arc methods
	
	public Arc add_main_arc(){
		//first generate a new unique identifier
		String identifier="m"+Integer.toString(main_arcs.size());
		//make arc and add it to the appropriate list
		Arc a=new Arc(identifier);
		main_arcs.add(a);
		return a;
	}
	
	public Arc add_side_arc(){
		//first generate a new unique identifier
		String identifier="s"+Integer.toString(side_arcs.size());
		//make arc and add it to the appropriate list
		Arc a=new Arc(identifier);
		side_arcs.add(a);
		return a;
	}
	
	//retrieve arc methods
	public Arc get_arc(String identifier){
		for(Arc a:main_arcs){
			if(identifier.equals(a.identifier())){
				return a;
			}
		}
		for(Arc a:side_arcs){
			if(identifier.equals(a.identifier())){
				return a;
			}
		}
		return null;
	}
	
	public ArrayList<Arc> get_active_arcs(){ //returns list of active arcs; active main arc(s) are always first
		ArrayList<Arc> l=new ArrayList<Arc>();
		for(Arc a:main_arcs){
			if(a.active()){
				l.add(a);
			}
		}
		for(Arc a:side_arcs){
			if(a.active()){
				l.add(a);
			}
		}
		return l;
	}
	
	public ArrayList<Arc> get_finished_arcs(){ //returns list of finished arcs; main arc(s) are always first
		ArrayList<Arc> l=new ArrayList<Arc>();
		for(Arc a:main_arcs){
			if(a.finished()){
				l.add(a);
			}
		}
		for(Arc a:side_arcs){
			if(a.finished()){
				l.add(a);
			}
		}
		return l;
	}
	
	public ArrayList<Arc> get_main_arcs(){ //only return non-deprecated arcs
		ArrayList<Arc> l=new ArrayList<Arc>();
		for(Arc a:main_arcs){
			if(a.identifier()!=null){
				l.add(a);
			}
		}
		return l;
	}
	
	public ArrayList<Arc> get_side_arcs(){ //only return non-deprecated arcs
		ArrayList<Arc> l=new ArrayList<Arc>();
		for(Arc a:side_arcs){
			if(a.identifier()!=null){
				l.add(a);
			}
		}
		return l;
	}
	
}
