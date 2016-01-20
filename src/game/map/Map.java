package game.map;

import com.jme3.scene.*;
import com.jme3.math.Ray;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.collision.*;

import game.battle.battler.Character;
import game.map.entity.Entity;
import game.map.entity.Entity_Dynamic;
import game.map.entity.Entity_Enemy;
import game.map.entity.Entity_Event;
import game.map.entity.Entity_Player;
import game.map.entity.Entity_Start;
import game.map.entity.Entity_Static;
import game.system.Process_Main;
import game.system.graphics.Graphics;
import io.game_dump.Dump_Entity_Enemy;
import io.game_dump.Dump_Entity_Event;
import io.game_dump.Dump_Entity_Static;
import io.game_dump.Dump_Map;

import java.util.ArrayList;
import com.jme3.light.*;
import jme3tools.optimize.GeometryBatchFactory;

//class of game maps; created with universe upon quest creation
public class Map {
	
	private int id;
	public String name;
	public String regionid="test";
	
	public Node map_node;
	public Floor floor;
	public ArrayList<Entity_Static> statics;
	private Node statics_node;
	private Spatial statics_node_optimized;
	public ArrayList<Entity_Enemy> enemies;
	private Node enemies_node;
	public ArrayList<Entity_Event> events;
	public Node events_node;
	public Entity_Start starting;
	private Node players_node;
	private Entity_Player player;
	private ArrayList<Entity_Player> players;
	
	//terrain
	public Terrain terrain;
	private Node terrain_node;
	
	//lights
	private AmbientLight ambient;
	public ColorRGBA ambient_color=ColorRGBA.White;
	public float ambient_intensity=0.4f;
	private DirectionalLight directional1;
	public Vector3f directional1_vector=new Vector3f(-1,-1,1);
	public ColorRGBA directional1_color=ColorRGBA.White;
	public float directional1_intensity=0.3f;
	private DirectionalLight directional2;
	public Vector3f directional2_vector=new Vector3f(1,0,1);
	public ColorRGBA directional2_color=ColorRGBA.White;
	public float directional2_intensity=0.3f;
	
	
	public Map(int n){
		id=n;
		name="";
		map_node=new Node();
		
		//make fixed environment
		make_environment();
	}
	
	public int id(){
		return id;
	}
	
	public String id_string(){  //return 3 digit representation of the id
		return game.Util.make_string(id,3);
	}
	
	////////////setup methods
	
	public void make_environment(){
		//make floor
		floor=new Floor();
		Graphics.graphics_manager.attachChild(map_node,floor.floor_node);
		//next handle static entities
		statics=new ArrayList<Entity_Static>();
		statics_node=new Node();
		statics_node_optimized=new Node();
		//Graphics.graphics_manager.attachChild(map_node,statics_node);
		//next handle enemy entities
		enemies=new ArrayList<Entity_Enemy>();
		enemies_node=new Node();
		Graphics.graphics_manager.attachChild(map_node,enemies_node);
		//next handle event entities
		events=new ArrayList<Entity_Event>();
		events_node=new Node();
		Graphics.graphics_manager.attachChild(map_node,events_node);
		//make empty players arraylist
		players=new ArrayList<Entity_Player>();
		players_node=new Node();
		Graphics.graphics_manager.attachChild(map_node,players_node);
		
		//optimize nodes
		optimize_nodes();
		
		//make default terrain
		terrain=new Terrain();
		terrain_node=new Node();
		Graphics.graphics_manager.attachChild(map_node,terrain_node);
		
		//refresh terrain
		refresh_terrain();
		
		//make default lights
		ambient=new AmbientLight();
		ambient.setColor(ColorRGBA.Red.mult(0.8f));
		directional1=new DirectionalLight();
		directional1.setDirection(new Vector3f(-1,-1,1));
		directional1.setColor(ColorRGBA.White.mult(0.1f));
		directional2=new DirectionalLight();
		directional2.setDirection(new Vector3f(1,0,1));
		directional2.setColor(ColorRGBA.Orange.mult(0.2f));
		
		//refresh lights
		refresh_lights();
	}
	
	public void refresh_lights(){
		Graphics.graphics_manager.setColor(ambient,ambient_color.mult(ambient_intensity));
		Graphics.graphics_manager.setColor(directional1,directional1_color.mult(directional1_intensity));
		Graphics.graphics_manager.setColor(directional2,directional2_color.mult(directional2_intensity));
		Graphics.graphics_manager.setDirection(directional1,directional1_vector);
		Graphics.graphics_manager.setDirection(directional2,directional2_vector);
	}
	
	public void refresh_terrain(){
		//detach previous terrain
		Graphics.graphics_manager.detachAllChildren(terrain_node);
		//attach new terrain spatial
		Graphics.graphics_manager.attachChild(terrain_node,terrain.get_spatial());
	}
	
	//node optimization method (currently only works for static entities)
	private void optimize_nodes(){
		//detach old node
		Graphics.graphics_manager.detachChild(map_node,statics_node_optimized);
		statics_node_optimized=statics_node;
		//Graphics.graphics_manager.optimize(statics_node);
		Graphics.graphics_manager.attachChild(map_node,statics_node_optimized);
	}
	
	//map light display methods
	public void show_lights(){
		//show ambient light
		Graphics.graphics_manager.addRootLight(ambient);
		Graphics.graphics_manager.addRootLight(directional1);
		Graphics.graphics_manager.addRootLight(directional2);
		//show static entity lights
		for(Entity_Static s:statics){
			s.show_lights();
		}
	}
	
	public void hide_lights(){
		//hide map ambient light
		Graphics.graphics_manager.removeRootLight(ambient);
		Graphics.graphics_manager.removeRootLight(directional1);
		Graphics.graphics_manager.removeRootLight(directional2);
		//hide static entity lights
		for(Entity_Static s:statics){
			s.hide_lights();
		}
	}
	
	//get list of entities which may impede movement/vision
	
	public ArrayList<Entity> impedables(){
		ArrayList<Entity> i=new ArrayList<Entity>();
		i.addAll(enemies);  //enemy entities
		i.addAll(players);  //player entities
		i.addAll(events);   //event entities
		//add static entities which are impedable
		for(Entity_Static s:statics){
			if(s.impedable){
				i.add(s);
			}
		}
		return i;
	}
	
	//make players
	
	public void make_player(){
		//make entity for party leader and show
		Character main=Process_Main.quest.party.current.get(0);
		player=new Entity_Player(main,Process_Main.quest.x,Process_Main.quest.y,Process_Main.quest.z,this);
		main.entity=player;
		players=new ArrayList<Entity_Player>();
		players.add(player);
		Graphics.graphics_manager.detachAllChildren(players_node);
		Graphics.graphics_manager.attachChild(players_node,player.model);
		//make entities for other party members
		/*if(Process_Main.quest.party.current.size()>1){
			for(int i=1;i<Process_Main.quest.party.current.size();i++){
				Character c=Process_Main.quest.party.current.get(i);
				Entity_Player p=new Entity_Player(c,player.x,player.y,player.z,this);
				c.entity=p;
				players.add(p);
			}
		}*/
	}
	
	//get main player entity
	public Entity_Player player(){
		return player;
	}
	
	//get list of all player entities
	public ArrayList<Entity_Player> players(){
		return players;
	}
	
	//select another player entity as main
	public void set_main_player(Entity_Player p){
		players.remove(p);
		players.add(0,p);
		player=p;
	}
	
	//cycle through players
	public void cycle_player(){
		players.remove(0);
		players.add(player);
		player=players.get(0);
	}
	
	public void cycle_player_rev(){
		player=players.get(players.size()-1);
		players.remove(players.size()-1);
		players.add(0,player);
	}
	
	public void show_all_players(){
		for(Entity_Player p:players){
			Graphics.graphics_manager.attachChild(players_node,p.model);
		}
	}
	
	public void hide_players(){
		for(Entity_Player p:players){
			Graphics.graphics_manager.detachChild(players_node,p.model);
		}
		Graphics.graphics_manager.attachChild(players_node,player.model);
	}
	
	///////////checking methods:
	
	//checks to see if a tile is open for the player to enter
	public boolean tile_open(int x,int z){
		boolean result=true;
		
		//check to make sure there is a floor tile
		if(!floor.tile_exist(x,z)){
			result=false;
		}
		
		//check to make sure no impedables are occupying the space
		for(Entity e:impedables()){
			if(Math.round(e.x)==x&&Math.round(e.z)==z&&e.occupies_tile()){
				result=false;
			}
		}
		
		//return the result
		return result;
	}
	
	public boolean visible_from(Entity e1,Entity e2,int visibility){
		//make test ray
		Ray line=new Ray(e1.coordinates(),e2.coordinates().subtract(e1.coordinates()));
		//if the entities are too far, just return false
		float distance=(e2.coordinates().subtract(e1.coordinates())).length();
		if(distance>visibility){
			return false;
		}
		//get list of impedables, other than the given entities
		ArrayList<Entity> list=impedables();
		list.remove(e1);
		list.remove(e2);
		//collide the ray with each impedable;
		for(Entity e:list){
			CollisionResults c=new CollisionResults();
			e.hitbox.collideWith(line,c);
			//check collisions to make sure they are between the entities
			for(int i=0;i<c.size();i++){
				Vector3f p=c.getCollision(i).getContactPoint();
				if(e1.coordinates().subtract(p).length()<distance){
					return false;
				}
			}
			
		}
		//if nothing went wrong, return true;
		return true;
	}
	
	//checks if any enemies are aware of any players
	public boolean enemy_aware_of_party(){
		//if a single enemy is aware of a player, then yes
		for(Entity_Enemy e:enemies){
			for(Entity_Dynamic d:e.aware_of()){
				if(d instanceof Entity_Player){
					return true;
				}
			}
		}
		
		//default of no:
		return false;
	}
	
	///////////modify methods
	
	public void add_starting(int x,int y,int z){
		starting=new Entity_Start(x,y,z);
		Graphics.graphics_manager.attachChild(statics_node,starting.model);
	}
	
	public void remove_starting(){
		if(starting!=null){
			Graphics.graphics_manager.detachChild(statics_node,starting.model);
			starting=null;
		}
	}
	
	public void add_entity_static(int x,int y,int z,float yrot,int bid,String lid){
		Entity_Static e=new Entity_Static(x,y,z,yrot,bid,lid);
		statics.add(e);
		Graphics.graphics_manager.attachChild(statics_node,e.model);
		optimize_nodes();
	}
	
	public void add_entity_static(Entity_Static e){
		statics.add(e);
		Graphics.graphics_manager.attachChild(statics_node,e.model);
		optimize_nodes();
	}
	
	public void remove_entity_static(int x,int y,int z){
		Entity_Static toremove=null;
		for(Entity_Static e:statics){
			if(e.x==x&&e.y==y&&e.z==z){
				toremove=e;
			}
		}
		if(toremove!=null){
			//statics_node.detachChild(toremove.model);
			toremove.dispose();
			Graphics.graphics_manager.detachChild(statics_node,toremove.model);
			statics.remove(toremove);
		}
		optimize_nodes();
	}
	
	public void add_entity_enemy(String type,int x,int y,int z){
		Entity_Enemy e=new Entity_Enemy(type,x,y,z,this);
		enemies.add(e);
		//enemies_node.attachChild(e.model);
		Graphics.graphics_manager.attachChild(enemies_node,e.model);
	}
	
	public void add_entity_enemy(Entity_Enemy e){
		enemies.add(e);
		if(!e.battler().defeated()){
			Graphics.graphics_manager.attachChild(enemies_node,e.model);
		}
	}
	
	public void remove_entity_enemy(int x,int z){
		Entity_Enemy toremove=null;
		for(Entity_Enemy e:enemies){
			if(Math.round(e.x)==x&&Math.round(e.z)==z){
				toremove=e;
			}
		}
		if(toremove!=null){
			//dispose entity
			toremove.dispose();
		
			enemies.remove(toremove);
		}
	}
	
	public void add_entity_event(int x,int y,int z){
		//first make sure that there is no event already at this location
		if(retrieve_entity_event(x,z)!=null){
			return;
		}
		Entity_Event e=new Entity_Event(x,y,z,this);
		events.add(e);
		Graphics.graphics_manager.attachChild(events_node,e.model);
	}
	
	public void add_entity_event(Entity_Event e){
		//first make sure that there is no event already at this location
		if(retrieve_entity_event(Math.round(e.x),Math.round(e.z))!=null){
			return;
		}
		events.add(e);
		//attach model (which may be empty, of course)
		Graphics.graphics_manager.attachChild(events_node,e.model);
	}
	
	public Entity_Event retrieve_entity_event(int x,int z){
		for(Entity_Event e:events){
			if(Math.round(e.x)==x&&Math.round(e.z)==z){
				return e;
			}
		}
		return null;
	}
	
	public void remove_entity_event(int x,int z){
		Entity_Event toremove=null;
		for(Entity_Event e:events){
			if(Math.round(e.x)==x&&Math.round(e.z)==z){
				toremove=e;
			}
		}
		if(toremove!=null){
			//dispose entity
			toremove.dispose();
			
			events.remove(toremove);
		}
	}
	
	//////////saving methods
	
	public void write(Dump_Map d){
		d.id=id;
		d.name=name;
		floor.write(d.floor);
		for(Entity_Static s:statics){
			Dump_Entity_Static ds=new Dump_Entity_Static();
			s.write(ds);
			d.statics.add(ds);
		}
		for(Entity_Enemy e:enemies){
			Dump_Entity_Enemy de=new Dump_Entity_Enemy();
			e.write(de);
			d.enemies.add(de);
		}
		for(Entity_Event e:events){
			Dump_Entity_Event de=new Dump_Entity_Event();
			e.write(de);
			d.events.add(de);
		}
		//write terrain
		d.terrain=terrain;
		//write lighting information
		d.ambient_color=ambient_color;
		d.ambient_intensity=ambient_intensity;
		d.directional1_color=directional1_color;
		d.directional1_intensity=directional1_intensity;
		d.directional1_vector=directional1_vector;
		d.directional2_color=directional2_color;
		d.directional2_intensity=directional2_intensity;
		d.directional2_vector=directional2_vector;
		
		//new:
		
	}
	
	public void read(Dump_Map d){
		id=d.id;
		name=d.name;
		floor.read(d.floor);
		for(Dump_Entity_Static ds:d.statics){
			Entity_Static es=new Entity_Static(0,0,0,0,0,"test");
			es.read(ds);
			add_entity_static(es);
		}
		for(Dump_Entity_Enemy de:d.enemies){
			Entity_Enemy e=new Entity_Enemy("",Math.round(de.x),Math.round(de.y),Math.round(de.z),this);
			e.read(de);
			add_entity_enemy(e);
		}
		for(Dump_Entity_Event de:d.events){
			Entity_Event e=new Entity_Event(Math.round(de.x),Math.round(de.y),Math.round(de.z),this);
			e.read(de);
			add_entity_event(e);
		}
		//read terrain information
		terrain=d.terrain;
		refresh_terrain();
		//read light information
		ambient_color=d.ambient_color;
		ambient_intensity=d.ambient_intensity;
		directional1_color=d.directional1_color;
		directional1_intensity=d.directional1_intensity;
		directional1_vector=d.directional1_vector;
		directional2_color=d.directional2_color;
		directional2_intensity=d.directional2_intensity;
		directional2_vector=d.directional2_vector;
		refresh_lights();
	}

}
