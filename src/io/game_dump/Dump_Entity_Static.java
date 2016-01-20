package io.game_dump;

import java.io.Serializable;

public class Dump_Entity_Static extends Dump_Entity implements Serializable {
	public static final long serialVersionUID=7083437496743475535L;
	
	public String name;
	public int height;
	public int blueprint_id;
	public String library_id;
	public float yrot;
	
	public Dump_Entity_Static(){
		super();
	}

}
