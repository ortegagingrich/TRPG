package game.map;

import game.map.entity.Floor_Tile;
import game.system.graphics.Graphics;
import io.game_dump.Dump_Floor;
import io.game_dump.Dump_Floor_Tile;

import java.util.ArrayList;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;

//floor class for maps; created a priori with map
public class Floor {
	
	public ArrayList<Floor_Tile> tiles;
	public Node floor_node;
	
	private boolean visible;
	
	public Floor(){
		tiles=new ArrayList<Floor_Tile>();
		floor_node=new Node();
		visible=false;
	}
	
	// show/hide methods (tiles will not be visible at runtime)
	public boolean visible(){
		return visible;
	}
	
	public void show(){
		for(Floor_Tile t:tiles){
			Graphics.graphics_manager.attachChild(floor_node,t.model);
		}
		visible=true;
	}
	
	public void hide(){
		Graphics.graphics_manager.detachAllChildren(floor_node);
		visible=false;
	}
	
	//add/remove tile methods
	
	public void add_tile(int x,int z,int height){
		Floor_Tile newtile=new Floor_Tile(x,z,height);
		//floor_node.attachChild(newtile.model);
		if(visible){
			Graphics.graphics_manager.attachChild(floor_node,newtile.model);
		}
		tiles.add(newtile);
	}
	
	public void add_tile(Floor_Tile tile){
		tiles.add(tile);
		if(visible){
			Graphics.graphics_manager.attachChild(floor_node,tile.model);
		}
	}
	
	public void add_tiles(int x1,int x2,int z1,int z2){
		int lx,hx,lz,hz;
		lx=Math.min(x1,x2);
		hx=Math.max(x1,x2);
		lz=Math.min(z1,z2);
		hz=Math.max(z1,z2);
		if(hz<lz){
			return;
		}
		for(int x=lx;x<=hx;x++){
			for(int z=lz;z<=hz;z++){
				if(!tile_exist(x,z)){
					add_tile(x,z,0);
				}
			}
		}
	}
	
	public boolean tile_exist(int x,int z){
		boolean result=false;
		for(Floor_Tile tile:tiles){
			if(tile.x==x&&tile.z==z){
				result=true;
			}
		}
		return result;
	}
	
	public void remove_tile(int x,int z){
		Floor_Tile toremove=null;
		for(Floor_Tile tile:tiles){
			if(tile.x==x&&tile.z==z){
				toremove=tile;
			}
		}
		if(toremove==null){
			return;
		}
		tiles.remove(toremove);
		//floor_node.detachChild(toremove.model);
		Graphics.graphics_manager.detachChild(floor_node,toremove.model);
	}
	
	public void remove_tiles(int x1,int x2,int z1,int z2){
		int lx,hx,lz,hz;
		lx=Math.min(x1,x2);
		hx=Math.max(x1,x2);
		lz=Math.min(z1,z2);
		hz=Math.max(z1,z2);
		for(int x=lx;x<=hx;x++){
			for(int z=lz;z<=hz;z++){
				remove_tile(x,z);
			}
		}
	}
	
	public void write(Dump_Floor d){
		for(Floor_Tile tile:tiles){
			Dump_Floor_Tile dt=new Dump_Floor_Tile();
			tile.write(dt);
			d.tiles.add(dt);
		}
	}
	
	public void read(Dump_Floor d){
		int count=0;
		for(Dump_Floor_Tile dt:d.tiles){
			Floor_Tile tile=new Floor_Tile(0,0,0);
			tile.read(dt);
			add_tile(tile);
			count++;
		}
	}
	
}
