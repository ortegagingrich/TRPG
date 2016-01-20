package io.game_dump;

import game.map.Terrain;

import java.io.Serializable;
import java.util.ArrayList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Dump_Map implements Serializable {
	
	public static final long serialVersionUID=6974755476053535160L; //never change this!
	
	public int id;
	public String name;
	public Dump_Floor floor;
	public ArrayList<Dump_Entity_Static> statics;
	public ArrayList<Dump_Entity_Enemy> enemies;
	public ArrayList<Dump_Entity_Event> events;
	public Dump_Entity_Static starting;
	//terrain data
	public Terrain terrain;
	//light data
	public ColorRGBA ambient_color=ColorRGBA.Red;
	public float ambient_intensity=0.8f;
	public Vector3f directional1_vector=new Vector3f(-1,-1,1);
	public ColorRGBA directional1_color=ColorRGBA.White;
	public float directional1_intensity=0.1f;
	public Vector3f directional2_vector=new Vector3f(1,0,1);
	public ColorRGBA directional2_color=ColorRGBA.Orange;
	public float directional2_intensity=0.2f;
	
	public Dump_Map(){
		statics=new ArrayList<Dump_Entity_Static>();
		enemies=new ArrayList<Dump_Entity_Enemy>();
		events=new ArrayList<Dump_Entity_Event>();
		floor=new Dump_Floor();
	}

}
