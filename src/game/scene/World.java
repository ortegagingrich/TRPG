package game.scene;

import game.map.Map;
import game.map.entity.Entity_Enemy;
import game.map.entity.Entity_Event;
import game.map.entity.Entity_Player;
import game.system.*;
import game.system.graphics.Graphics;

import com.jme3.scene.Node;

//handles basically everything in the game world
//3d objects are handled by the objects themselves
//created upon boot up
public class World {
	
	public Node world_node;
	public Overlay_Grid grid;
	public Overlay_Selector selector;
	public Overlay_Selector_Secondary secondary_selector;
	public Map map;
	
	public World(Scene_WorldEdit s){
		world_node=new Node();
		grid=new Overlay_Grid(0,0,100,100,world_node);
		selector=new Overlay_Selector(world_node);
		secondary_selector=new Overlay_Selector_Secondary(world_node,selector);
	}
	
	public World(Scene_Explore s){
		world_node=new Node();
		grid=new Overlay_Grid(0,0,100,100,world_node);
		grid.hide();
		selector=new Overlay_Selector(world_node);
		secondary_selector=new Overlay_Selector_Secondary(world_node,selector);
		selector.hide();
		secondary_selector.hide();
	}
	
	public void update(){
		//update players
		for(Entity_Player p:map.players()){
			p.update();
		}
		//update enemy entities
		for(Entity_Enemy e:map.enemies){
			e.update();
		}
		//update event entities
		for(Entity_Event e:map.events){
			e.update();
		}
		//update selectors
		selector.update();
		secondary_selector.update();
	}
	
	
	public void display_map(Map m){
		hide_map();
		map=m;
		Graphics.graphics_manager.attachChild(world_node,m.map_node);
		//display map lights
		map.show_lights();
	}
	
	public void hide_map(){
		if(map!=null){
			Graphics.graphics_manager.detachChild(world_node,map.map_node);
			//hide lights
			map.hide_lights();
		}
		//unload all sprite textures to save memory
		Process_Graphics.spriteset.unload_all_sprites();
	}
	
	//method that prepares current map for game play (i.e. adds player entity); called each time a map is entered?
	public void prepare_map(){
		//add player entity
		map.make_player();
		//show players
		map.show_all_players();
		//hide player start
		map.remove_starting();
		//hide floor
		map.floor.hide();
		//hide grid
		grid.hide();
		//hide selectors
		selector.hide();
		secondary_selector.hide();
	}
	
	public void show_world(){
		Graphics.graphics_manager.attachChild(Process_Main.rootNode,world_node);
	}
	
	public void hide_world(){
		Graphics.graphics_manager.detachChild(Process_Main.rootNode,world_node);
	}

}
