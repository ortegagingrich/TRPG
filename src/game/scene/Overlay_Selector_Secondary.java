package game.scene;

import game.system.Process_Graphics;
import game.system.graphics.Graphics;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.*;

public class Overlay_Selector_Secondary extends Overlay_Selector {
	
	private Overlay_Selector primary_selector;
	public boolean follow_primary_selector=true;
	
	public Overlay_Selector_Secondary(Node w,Overlay_Selector p){
		super(w);
		primary_selector=p;
	}
	
	@Override
	public void update(){
		if(follow_primary_selector){
			snap_to_primary();
		}
		
		//move box if there was a change
		Graphics.graphics_manager.move(box,x-oldx,y-oldy,z-oldz);
		Graphics.graphics_manager.move(model_node,x-oldx,y-oldy,z-oldz);
		oldx=x;
		oldy=y;
		oldz=z;
	}
	
	public void snap_to_primary(){
		x=primary_selector.x;
		y=primary_selector.y;
		z=primary_selector.z;
	}
	
	@Override
	public void draw_main_box(){
		Process_Graphics.$graphics.draw_box_wireframe(box,0,y,0,1,1,1,ColorRGBA.LightGray);
		//Process_Graphics.$graphics.draw_box_wireframe(line,0,y,0,0,wall_height,0,ColorRGBA.LightGray);
		oldx=0;
		oldy=y;
		oldz=0;
	}
	
}
