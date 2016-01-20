package game.map.entity;

import com.jme3.scene.*;
import com.jme3.light.*;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;

import game.scene.Scene_Explore;
import game.system.Process_Main;
import game.system.graphics.Graphics;
import io.game_dump.Dump_Entity;

import java.util.ArrayList;

//class of entity objects: items in the world which have a model, occupy a location and may possibly be interacted with; created as needed
public class Entity {
	
	public float x;
	public float y;
	public float z;
	public Spatial model;
	public Spatial hitbox;
	public ArrayList<Light> lights;
	
	public Entity(float xi,float yi,float zi){
		x=xi;
		y=yi;
		z=zi;
		lights=new ArrayList<Light>();
	}
	
	//change position method
	public void set_position(float xi,float yi,float zi){
		x=xi;
		y=yi;
		z=zi;
	}
	
	//whether or not event occupies tile; overriden for subclasses
	public boolean occupies_tile(){
		return false;
	}
	
	public void retrieve_model(){
		System.out.println("Problem retrieving model");
	}
	
	public void show_lights(){
		//hide existing lights
		hide_lights();
		//add lights
		for(Light l:lights){
			Graphics.graphics_manager.addRootLight(l);
		}
	}
	
	public void hide_lights(){
		//remove all lights from the root node
		for(Light l:lights){
			Graphics.graphics_manager.removeRootLight(l);
		}
	}
	
	public Vector3f coordinates(){
		return new Vector3f(x,y,z);
	}
	
	public int taxi_distance_from_player(){
		Entity_Player p=((Scene_Explore)Process_Main.$scene).world.map.player();
		return taxi_distance_from(p);
	}
	
	public int taxi_distance_from(Entity e){
		return taxi_distance_from(Math.round(e.x),Math.round(e.z));
	}
	
	public int taxi_distance_from(int xt,int zt){
		return Math.abs(xt-Math.round(x))+Math.abs(zt-Math.round(z));
	}
	
	public int taxi_distance_from(Vector2f v){
		return taxi_distance_from(Math.round(v.x),Math.round(v.y));
	}
	
	public void write(Dump_Entity d){
		d.x=x;
		d.y=y;
		d.z=z;
	}
	
	public void read(Dump_Entity d){
		x=d.x;
		y=d.y;
		z=d.z;
		//set model
		retrieve_model();
	}

}
