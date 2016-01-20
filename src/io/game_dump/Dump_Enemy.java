package io.game_dump;

import java.io.Serializable;

public class Dump_Enemy extends Dump_Battler implements Serializable {
	
	public String type;
	public boolean defeated;
	
	public Dump_Enemy(){
		super();
	}

}
