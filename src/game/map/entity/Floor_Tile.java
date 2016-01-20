package game.map.entity;

import game.system.Process_Graphics;
import io.game_dump.Dump_Floor_Tile;

import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import jme3tools.optimize.GeometryBatchFactory;

//created a priori with map
public class Floor_Tile extends Entity {
	
	public Floor_Tile(int xi,int zi,int yi){
		super(Math.round(xi-0.5f),yi,Math.round(zi-0.5f));
		retrieve_model();
	}
	
	
	public void retrieve_model(){
		//temporary
		model=new Node();
		//make floor
		Geometry g=Process_Graphics.$graphics.draw_box((Node)model,x+0.5f,y,z+0.5f,0.5f,0.0001f,0.5f);
		model=GeometryBatchFactory.optimize((Node)model);
	}
	
	public void write(Dump_Floor_Tile d){
		super.write(d);
	}
	
	public void read(Dump_Floor_Tile d){
		super.read(d);
	}
	
	

}
