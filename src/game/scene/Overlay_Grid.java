package game.scene;


import game.system.Process_Graphics;
import game.system.graphics.Graphics;

import com.jme3.scene.Node;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

//basic grid (not visible during gameplay); created with world upon boot up
public class Overlay_Grid {
	
	private int x_width;
	private int z_width;
	private int x_origin;
	private int z_origin;
	private Node world_node;
	private Node grid_node;
	private boolean hidden;
	private ColorRGBA color;
	
	
	public Overlay_Grid(int xo,int zo,int xw,int zw,Node par){
		world_node=par;
		grid_node=new Node();
		//world_node.attachChild(grid_node);
		Graphics.graphics_manager.attachChild(world_node,grid_node);
		hidden=false;
		color=ColorRGBA.Blue;
		set_grid(xw,zw,xo,zo);
	}
	
	public void set_grid(int xw,int zw,int xo,int zo){
		x_width=xw;
		z_width=zw;
		x_origin=xo;
		z_origin=zo;
		refresh_grid();
	}
	
	public void hide(){
		if(!hidden){
			//world_node.detachChild(grid_node);
			Graphics.graphics_manager.detachChild(world_node,grid_node);
		}
		hidden=true;
	}
	
	public void show(){
		if(hidden){
			//world_node.attachChild(grid_node);
			Graphics.graphics_manager.attachChild(world_node,grid_node);
		}
		hidden=false;
	}
	
	public void toggle(){
		if(hidden){
			show();
		}else{
			hide();
		}
	}
	
	public void refresh_grid(){
		//first detach all lines
		//grid_node.detachAllChildren();
		Graphics.graphics_manager.detachAllChildren(grid_node);
		//first make horizontal lines
		for(int x=x_origin;x<=x_origin+x_width;x++){
			Vector3f start=new Vector3f(x,0.05f,z_origin);
			Vector3f end=new Vector3f(x,0.05f,z_origin+z_width);
			Process_Graphics.$graphics.draw_line(grid_node,start,end,color);
		}
		//next make vertical lines
		for(int z=z_origin;z<=z_origin+z_width;z++){
			Vector3f start=new Vector3f(x_origin,0.05f,z);
			Vector3f end=new Vector3f(x_origin+x_width,0.05f,z);
			Process_Graphics.$graphics.draw_line(grid_node,start,end,color);
		}
	}

}
