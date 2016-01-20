package io.game_dump;

import java.io.Serializable;
import java.util.ArrayList;

public class Dump_Floor implements Serializable {
	
	public ArrayList<Dump_Floor_Tile> tiles;
	
	public Dump_Floor(){
		tiles=new ArrayList<Dump_Floor_Tile>();
	}

}
