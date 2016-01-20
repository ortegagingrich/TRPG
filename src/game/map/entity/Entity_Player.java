package game.map.entity;

import game.battle.battler.Battler;
import game.battle.battler.Character;
import game.map.Map;
import game.scene.Scene_Explore;
import game.system.Process_Graphics;
import game.system.Process_Main;

//class of the player entity; each map instance contains its own copy of this, which is active when that map is loaded
public class Entity_Player extends Entity_Battler {
	
	private Character character;
	
	
	public Entity_Player(Character c,float xi,float yi,float zi,Map m){
		super(xi,yi,zi,m,0.05f);
		character=c;
		retrieve_model();
	}
	
	//return name
	
	public String name(){
		return character.name;
	}
	
	public Character character(){
		return character;
	}
	
	public Battler battler(){
		return character;
	}
	
	//update methods
	
	public void update(){
		//if not the active character, set to follow the active character
		if(this!=map.player()&&(Process_Main.$scene instanceof Scene_Explore)){
			if(!((Scene_Explore)Process_Main.$scene).movement_fixed){
				update_follow();
			}
		}
		//regular update
		super.update();
	}
	
	
	private void update_follow(){
		//determine target x and z coordinates
		int tarx=Math.round(x); //by default, set as current coordinates
		int tarz=Math.round(z);
		if(map.player().face=="u"){
			if(map.tile_open(Math.round(map.player().x),Math.round(map.player().z)-3)){
				tarx=Math.round(map.player().x);
				tarz=Math.round(map.player().z)-3;
			}
		}else if(map.player().face=="d"){
			if(map.tile_open(Math.round(map.player().x),Math.round(map.player().z)+3)){
				tarx=Math.round(map.player().x);
				tarz=Math.round(map.player().z)+3;
			}
		}else if(map.player().face=="l"){
			if(map.tile_open(Math.round(map.player().x)-3,Math.round(map.player().z))){
				tarx=Math.round(map.player().x)-3;
				tarz=Math.round(map.player().z);
			}
		}else if(map.player().face=="r"){
			if(map.tile_open(Math.round(map.player().x)+3,Math.round(map.player().z))){
				tarx=Math.round(map.player().x)+3;
				tarz=Math.round(map.player().z);
			}
		}
		
		//if target tile is available, set as entity target
		if(map.tile_open(tarx,tarz)){
			set_target(tarx,tarz);
		}
	}
	
	//end initiative
	
	public void end_initiative(){
		//make lose concentration
		make_lose_concentration();
	}
	
	//retrieve model
	@Override
	public void retrieve_model(){
		super.retrieve_model();
		//set hitbox
		hitbox=Process_Graphics.$graphics.make_box(x+0.5f,y+0.9f,z+0.5f,0.3f,0.9f,0.3f);
	}
	
	
}
