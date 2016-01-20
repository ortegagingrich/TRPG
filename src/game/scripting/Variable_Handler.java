package game.scripting;

import java.io.Serializable;
import java.util.ArrayList;

//class of variable handlers (contains switches and variables); may be attached to an arc, quest, character, map, etc.
public class Variable_Handler implements Serializable {
	public final static long serialVersionUID=1L;
	
	private String[] address;
	
	private ArrayList<Switch> switches;
	private ArrayList<Variable> variables;
	
	public Variable_Handler(String par_type,String par_id){
		address=new String[2];
		address[0]=par_type;
		address[1]=par_id;
		switches=new ArrayList<Switch>();
		variables=new ArrayList<Variable>();
	}
	
	public String[] address(){
		return address;
	}
	
	//variable creation methods
	public Switch make_switch(String name){
		Switch s=new Switch(switches.size(),name,address);
		switches.add(s);
		return s;
	}
	
	public Switch make_switch(String name,boolean val){
		Switch s=new Switch(switches.size(),name,address,val);
		switches.add(s);
		return s;
	}
	
	public Variable make_variable(String name){
		Variable v=new Variable(variables.size(),name,address);
		variables.add(v);
		return v;
	}
	
	public Variable make_variable(String name,float val){
		Variable v=new Variable(variables.size(),name,address,val);
		variables.add(v);
		return v;
	}
	
	//variable return methods
	public Switch get_switch(int id){
		for(Switch s:switches){
			if(s.id()==id){
				return s;
			}
		}
		return null;
	}
	
	public Variable get_variable(int id){
		for(Variable v:variables){
			if(v.id()==id){
				return v;
			}
		}
		return null;
	}
	
	public ArrayList<Switch> get_switches(){
		return switches;
	}
	
	public ArrayList<Variable> get_variables(){
		return variables;
	}
	
}
