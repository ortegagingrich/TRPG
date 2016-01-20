package game.battle;

import game.battle.battler.Battler;
import game.battle.battler.Character;
import game.battle.battler.Enemy;
import game.map.Map;
import game.map.entity.Entity;
import game.map.entity.Entity_Dynamic;
import game.map.entity.Entity_Enemy;
import game.map.entity.mechanics.Move_Path;
import game.scene.Scene_Initiative;
import game.system.Process_Graphics;
import game.system.Process_Main;
import game.system.graphics.Graphics;

import java.util.ArrayList;
import com.jme3.scene.Node;

//battle manager class
public class Battle_Manager {
	
	public ArrayList<Battler> battlers;  //array list of battlers in order of initiative
	public ArrayList<Enemy> enemies; //array list of enemies in battle; not necessarily in order
	public ArrayList<Character> allies; //array list of allies in battle; not necessarily in order
	public ArrayList<Initiative_Order> orders;  //orders to be carried out passively
	public Initiative_Order current_order;  //order currently being executed
	public String mode;    //scene mode
	public Battler current;
	
	//temporary storage (for within a given turn)
	public boolean moved; //indicates if movement already done on current turn
	public boolean acted; //indicates if already acted on current turn
	public Battle_Action proposed; //selected battle action is stored here while a target is selected
	
	private Map map;
	private Node temp_visuals;  //node for temporary visual aids (e.g. available tiles for moving)
	private Node available_moves;  //sub node of temp_visuals
	
	public Battle_Manager(Map m){
		map=m;
		temp_visuals=new Node();
		available_moves=new Node();
		Graphics.graphics_manager.attachChild(map.map_node,temp_visuals);
		
		orders=new ArrayList<Initiative_Order>();
		mode="start";
		moved=false;
		acted=false;
		
		battlers=new ArrayList<Battler>();
		allies=new ArrayList<Character>();
		enemies=new ArrayList<Enemy>();
		//add party battlers
		for(Character c:Process_Main.quest.party.current){
			battlers.add(c);
			allies.add(c);
		}
		//add enemy entities
		for(Entity_Enemy e:map.enemies){
			battlers.add(e.battler());
			enemies.add(e.enemy());
		}
		sort_battlers();
	}
	
	public void dispose(){
		//hide all battle based visuals
		Graphics.graphics_manager.detachChild(map.map_node,temp_visuals);
	}
	
	// check methods
	
	public boolean can_end(){ //checks to see if the player can end initiative
		//if no enemies, then yes
		if(enemies.size()==0){
			return true;
		}
		
		//if a single enemy is aware, then no
		if(map.enemy_aware_of_party()){
			return false;
		}
		
		//default yes:
		return true;
	}
	
	//update methods
	
	public void update(){
		//remove non-resurrectable defeated enemies
		Enemy toremove=null;
		for(Enemy e:enemies){
			if(e.defeated()&&!e.resurrectable){
				toremove=e;
			}
		}
		if(toremove!=null){
			remove_enemy(toremove);
		}
		
		//check for game over
		if(Process_Main.quest.party.defeated()){
			//add handling for game over:
			
		}
	}
	
	public void update_order(){
		//set current order, if not already set
		if(current_order==null){
			current_order=orders.get(0);
		}
		//update current order
		current_order.update();
		//check if finished
		if(current_order.finished){
			orders.remove(0);
			current_order=null;
		}
	}
	
	//battler management methods
	
	private void remove_enemy(Enemy e){
		//remove from lists
		enemies.remove(e);
		battlers.remove(e);
		//dispose entity
		map.remove_entity_enemy(Math.round(e.entity.x),Math.round(e.entity.z));
	}
	
	public void sort_battlers(){
		//sort battlers by dexterity
		ArrayList<Battler> tbat=new ArrayList<Battler>();
		for(int speed=1000;speed>=0;speed--){
			for(Battler b:battlers){
				if(b.dexterity()==speed){
					tbat.add(b);
				}
			}
		}
		battlers=tbat;
	}
	
	public void set_next_battler(){
		Battler next=battlers.get(0);
		battlers.remove(0);
		battlers.add(next);
		while(next.defeated()){  //cycle, until a battler is found that is not defeated
			next=battlers.get(0);
			battlers.remove(0);
			battlers.add(next);
		}
		current=next;
		((Scene_Initiative)Process_Main.$scene).hud.refresh();
	}
	
	public Battler get_battler(int x,int z){
		for(Battler b:battlers){
			if(Math.round(b.entity.x)==x&&Math.round(b.entity.z)==z){
				return b;
			}
		}
		return null;
	}
	
	// Battle orders methods:
	
	public void pan_camera_to(Entity e){
		orders.add(new Initiative_Order("camera to",e.x,e.y+10,e.z-15));
	}
	
	//finds the appropriate path first
	public void move_entity_to(Entity_Dynamic e,int destx,int destz){
		//first get the appropriate move path
		Move_Path path=null;
		for(Move_Path p:availables){
			if((Math.round(p.destination.x)==destx)&&(Math.round(p.destination.y)==destz)){
				path=p;
			}
		}
		//if an appropriate path was found, move along it
		if(path!=null){
			move_entity_along(e,path);
		}
	}
	
	//to move entity, if we already have a move path
	public void move_entity_along(Entity_Dynamic e,Move_Path path){
		//add move order
		orders.add(new Initiative_Order("move path",path,e,null));
		//set moved flag to true
		moved=true;
	}
	
	//tries to execute a battle action
	public void execute_action(Battle_Action action,Entity_Dynamic target,Battler originator){
		//first determine if the action is successful
		//temporary: all attacks hit for now
		
		//hit all targets within the splash range of the target
		//check characters
		if((action.affects_allies&&originator instanceof Character)||(action.affects_enemies&&originator instanceof Enemy)){
			for(Character c:allies){
				if(c.entity.taxi_distance_from(target)<=action.splash_range){
					action.execute(c);
				}
			}
		}
		//check enemies
		if((action.affects_allies&&originator instanceof Enemy)||(action.affects_enemies&&originator instanceof Character)){
			for(Enemy e:enemies){
				if(e.entity.taxi_distance_from(target)<=action.splash_range){
					action.execute(e);
				}
			}
		}
		//set acted flag to true
		acted=true;
		//to do: add orders for animations, etc.
		
	}
	
	// visual helper methods
	
	public void mark_tile(Node n,int x,int z){ //marks a tile as available
		Process_Graphics.$graphics.draw_box(n,x+0.5f,0,z+0.5f,0.45f,0.01f,0.45f);
	}
	
	private ArrayList<Move_Path> availables;  //list of available moves
	
	public void show_available_moves(Battler b){  //displays available move slots
		//determine available moves
		availables=b.entity.available_moves(b.move_speed());
		
		//show available moves
		for(Move_Path tile:availables){
			mark_tile(available_moves,Math.round(tile.destination.x),Math.round(tile.destination.y));
		}
		Graphics.graphics_manager.attachChild(temp_visuals,available_moves);
	}
	
	public void hide_available_moves(){
		Graphics.graphics_manager.detachChild(temp_visuals,available_moves);
		Graphics.graphics_manager.detachAllChildren(available_moves);
		//clear repository of available moves
		availables.clear();
	}
	
	public void show_available_targets(Battler b){
		int range=b.attack_range();
		for(int i=Math.round(b.entity.x)-range;i<=Math.round(b.entity.x)+range;i++){
			int d=range-Math.abs(Math.round(b.entity.x)-i);
			for(int j=Math.round(b.entity.z)-d;j<=Math.round(b.entity.z)+d;j++){
				if(map.floor.tile_exist(i,j)){
					mark_tile(available_moves,i,j);
				}
			}
		}
		Graphics.graphics_manager.attachChild(temp_visuals,available_moves);
	}
	
	public void hide_available_targets(){
		Graphics.graphics_manager.detachChild(temp_visuals,available_moves);
		Graphics.graphics_manager.detachAllChildren(available_moves);
	}
	
}
