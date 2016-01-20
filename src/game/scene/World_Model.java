package game.scene;

import asset.model.*;

import com.jme3.scene.*;
import com.jme3.light.*;
import com.jme3.math.*;
import com.jme3.collision.*;

import game.system.Process_Graphics;
import game.system.Process_Main;
import game.system.graphics.Graphics;

import java.util.ArrayList;

//equivalent of world object, but for object editing mode
public class World_Model {
	
	public Node world_node;
	public Overlay_Grid grid;
	public Model model;
	public Model_Library library;
	public Spatial model_spatial;
	
	private PointLight origin_light;
	private AmbientLight light;
	
	private Scene_Model scene; //for easy reference
	
	public World_Model(Scene_Model s){
		scene=s;
		world_node=new Node();
		grid=new Overlay_Grid(0,0,5,5,world_node);
		//make ambient light
		light=new AmbientLight();
		light.setColor(ColorRGBA.White.mult(1.5f));
		origin_light=new PointLight();
		origin_light.setPosition(new Vector3f(0,1,0));
		origin_light.setColor(ColorRGBA.White.mult(1f));
		Graphics.graphics_manager.addLight(world_node,light);
		Graphics.graphics_manager.addLight(world_node,origin_light);
		//by default, open test library
		library=Process_Graphics.get_library("test");
		//make empty default model
		make_new_model();
	}
	
	//update method
	public void update(){
		//update overlays, if needed
	}
	
	//display/hide model methods
	public void display_model(Model m){
		model=m;
		refresh_model();
	}
	
	public void make_new_model(){
		model=new Model("");
		refresh_model();
	}
	
	public void refresh_model(){
		//detach any previous spatials
		Graphics.graphics_manager.detachChild(world_node,model_spatial);
		//get new spatial
		if(scene.hud!=null){
			model_spatial=model.get_spatial(scene.hud.current_component());
		}else{
			model_spatial=model.get_spatial();
		}
		//attach lights to model spatial
		for(Light l:model.get_light_list()){
			model_spatial.addLight(l);
		}
		//attach new model spatial to node
		Graphics.graphics_manager.attachChild(world_node,model_spatial);
	}
	
	public void show_world(){
		Graphics.graphics_manager.attachChild(Process_Main.rootNode,world_node);
	}
	
	public void hide_world(){
		Graphics.graphics_manager.detachChild(Process_Main.rootNode,world_node);
	}
	
	//select component method
	public void select_mouse_component(){
		//first get the mouse ray
		Ray mouseray=Process_Graphics.$graphics.get_mouse_ray();
		//make empty list of components which intersect
		ArrayList<Component> intersect=new ArrayList<Component>();
		ArrayList<Geometry> hitboxes=new ArrayList<Geometry>();
		CollisionResults results=new CollisionResults();
		//try colliding the ray with each component
		for(Component component:model.components()){
			Geometry hbox=component.get_hitbox();
			CollisionResults c=new CollisionResults();
			hbox.collideWith(mouseray,c);
			if(c.size()>0){
				intersect.add(component);
				hitboxes.add(hbox);
				results.addCollision(c.getClosestCollision());
			}
		}
		//if there were any collisions, select the closes component, otherwise, deselect the current component
		if(results.size()>0){
			scene.hud.select_component(intersect.get(hitboxes.indexOf(results.getClosestCollision().getGeometry())));
		}else{
			scene.hud.deselect_component();
		}
		
		//refresh the model
		refresh_model();
	}

}
