package io.game_dump;

import game.save.journal.Journal;
import game.scripting.*;

import java.io.Serializable;

public class Dump_Quest implements Serializable {
	public final static long serialVersionUID=-2783204234120297906L;
	
	public String label;
	public Dump_Party party;
	public Dump_Universe universe;
	public Journal journal;
	public int m;
	public int x;
	public int y;
	public int z;
	
	public Dump_Quest(){
		party=new Dump_Party();
		universe=new Dump_Universe();
	}

}
