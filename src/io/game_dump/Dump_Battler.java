package io.game_dump;

import java.io.Serializable;

public class Dump_Battler implements Serializable {
	
	public String type;
	public int hp_max;
	public int hp;
	public int def_n;
	public int acc_n;
	public int dex_n;
	public int mov_n;
	
	public Dump_Battler(){
		
	}

}
