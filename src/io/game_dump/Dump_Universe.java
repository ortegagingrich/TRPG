package io.game_dump;

import java.io.Serializable;
import java.util.ArrayList;

public class Dump_Universe implements Serializable {
	
	public ArrayList<Dump_Map> maps;
	public int startm;
	public int startx;
	public int starty;
	public int startz;
	
	public Dump_Universe(){
		maps=new ArrayList<Dump_Map>();
	}

}
