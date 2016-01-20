package io.game_dump;

import java.io.Serializable;

public class Dump_Entity_Enemy extends Dump_Entity implements Serializable {
	
	public Dump_Enemy battler;
	
	public Dump_Entity_Enemy(){
		super();
		battler=new Dump_Enemy();
	}

}
