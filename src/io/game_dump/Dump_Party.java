package io.game_dump;

import java.io.Serializable;
import java.util.ArrayList;

public class Dump_Party implements Serializable {
	
	public ArrayList<Dump_Character> characters;
	public ArrayList<Dump_Character> current;
	
	public Dump_Party(){
		characters=new ArrayList<Dump_Character>();
		current=new ArrayList<Dump_Character>();
	}

}
