package game.scene;

import game.system.Process_Graphics;
import game.system.graphics.Graphics;

import asset.model.*;

import com.jme3.math.Ray;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Geometry;
import com.jme3.math.Plane;
import com.jme3.math.ColorRGBA;

//object that indicates current location of mouse; created with world on boot up
public class Overlay_Selector {
	
	public int x;
	public int y;
	public int z;
	public int anchorx;
	public int anchory;
	public int anchorz;
	public Node world_node;
	public Node selector_node;
	public boolean visible;
	public boolean locked=false;
	
	private boolean wall_mode; //indicates if selector is in wall mode
	private int wall_height;  //wall height
	private Node line; //model for wall mode
	protected Node box;
	private Node anchor_box;//box at anchor
	protected Node model_node;//helper model node (e.g. for placing static entity)
	private boolean anchor_visible;
	protected float oldx;
	protected float oldy;
	protected float oldz;
	private int facing; //the selector's orientation
	
	public Overlay_Selector(Node w){
		world_node=w;
		x=0;
		y=0;
		z=0;
		anchorx=0;
		anchory=0;
		anchorz=0;
		oldx=x;
		oldy=y;
		oldz=z;
		anchor_visible=false;
		wall_mode=false;
		wall_height=2;
		
		selector_node=new Node();
		box=new Node();
		line=new Node();
		anchor_box=new Node();
		model_node=new Node();
		visible=true;
		facing=0;
		Graphics.graphics_manager.attachChild(world_node,selector_node);
		Graphics.graphics_manager.attachChild(selector_node,box);
		Graphics.graphics_manager.attachChild(selector_node,model_node);
		draw_main_box();
		
		update();
	}
	
	public void refresh(){
		//redraw main box/line
		Graphics.graphics_manager.detachAllChildren(box);
		Graphics.graphics_manager.detachAllChildren(line);
		draw_main_box();
		update();
	}
	
	public void update(){
		
		if(!locked){
			Ray mouseray=Process_Graphics.$graphics.get_mouse_ray();
			Plane p=new Plane(new Vector3f(0,1,0),0);
			Vector3f loc=new Vector3f();
			mouseray.intersectsWherePlane(p,loc);
			
			if(wall_mode){
				x=Math.round(loc.x);
				z=Math.round(loc.z);
			}else{
				x=Math.round(loc.x-0.5f);
				z=Math.round(loc.z-0.5f);
			}
		}
		
		
		//move box if there was a change
		Graphics.graphics_manager.move(box,x-oldx,y-oldy,z-oldz);
		Graphics.graphics_manager.move(line,x-oldx,y-oldy,z-oldz);
		Graphics.graphics_manager.move(model_node,x-oldx,y-oldy,z-oldz);
		oldx=x;
		oldy=y;
		oldz=z;
		
		//draw anchor box, if visible
		if(anchor_visible){
			update_anchor();
		}
	}
	
	public void show(){
		if(!visible){
			Graphics.graphics_manager.attachChild(world_node,selector_node);
		}
		visible=true;
	}
	
	public void hide(){
		if(visible){
			Graphics.graphics_manager.detachChild(world_node,selector_node);
		}
		visible=false;
	}
	
	
	//show model methods
	public void show_model(Model m){
		//first, hide old model
		hide_model();
		//show new model
		Spatial s=m.get_spatial();
		Graphics.graphics_manager.attachChild(model_node,s);
		//reset orientation
		rotate_to(0);
	}
	
	public void hide_model(){
		Graphics.graphics_manager.detachAllChildren(model_node);
		//reset orientation
		rotate_to(0);
	}
	
	//orientation methods
	public int facing(){
		return facing;
	}
	
	public void rotate_left(){
		int f=facing;
		f+=90;
		if(f==360){
			f=0;
		}
		rotate_to(f);
	}
	
	public void rotate_right(){
		int f=facing;
		f-=90;
		if(f<0){
			f=270;
		}
		rotate_to(f);
	}
	
	public void rotate_to(int f){
		int oldfacing=facing;
		facing=f;
		int diff=facing-oldfacing;
		//rotate model node
		Graphics.graphics_manager.rotate(model_node,0,(float)Math.toRadians(diff),0);
	}
	
	public void set_mode_wall(){
		wall_mode=true;
		Graphics.graphics_manager.detachChild(selector_node,box);
		Graphics.graphics_manager.attachChild(selector_node,line);
	}
	
	public int wall_height(){
		return wall_height;
	}
	
	public void set_wall_height(int h){
		wall_height=h;
		refresh();
	}
	
	public void set_mode_tile(){
		wall_mode=false;
		Graphics.graphics_manager.attachChild(selector_node,box);
		Graphics.graphics_manager.detachChild(selector_node,line);
	}
	
	public void toggle_mode(){
		if(wall_mode){
			set_mode_tile();
		}else{
			set_mode_wall();
		}
	}
	
	
	public void anchor(){
		anchorx=x;
		anchory=y;
		anchorz=z;
		anchor_visible=true;
		Graphics.graphics_manager.attachChild(selector_node,anchor_box);
	}
	
	public void anchor_release(){
		anchor_visible=false;
		Graphics.graphics_manager.detachChild(selector_node,anchor_box);
	}
	
	public boolean anchored(){
		return anchor_visible;
	}
	
	
	public void draw_main_box(){
		Process_Graphics.$graphics.draw_box_wireframe(box,0,y,0,1,1,1,ColorRGBA.Green);
		//Process_Graphics.$graphics.draw_box_wireframe(line,0,y,0,0,wall_height,0,ColorRGBA.LightGray);
		oldx=0;
		oldy=y;
		oldz=0;
	}
	
	protected void update_anchor(){
		Graphics.graphics_manager.detachAllChildren(anchor_box);
		int lx,hx,lz,hz;
		lx=Math.min(x,anchorx);
		hx=Math.max(x,anchorx);
		lz=Math.min(z,anchorz);
		hz=Math.max(z,anchorz);
		if(wall_mode){
			//wall mode
			int xdiff=hx-lx;
			int zdiff=hz-lz;
			if(zdiff>xdiff){
				Process_Graphics.$graphics.draw_box_wireframe(anchor_box,anchorx,y,lz,0,wall_height,hz-lz,ColorRGBA.White);
			}else{
				Process_Graphics.$graphics.draw_box_wireframe(anchor_box,lx,y,anchorz,hx-lx,wall_height,0,ColorRGBA.White);
			}
		}else{
			//tile mode
			Process_Graphics.$graphics.draw_box_wireframe(anchor_box,lx,y,lz,hx-lx+1,1,hz-lz+1,ColorRGBA.White);
		}
		
	}

}
