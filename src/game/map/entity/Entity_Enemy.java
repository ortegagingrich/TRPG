package game.map.entity;

import game.battle.ai.AI_Action;
import game.battle.ai.AI_Battle;
import game.battle.battler.Battler;
import game.battle.battler.Enemy;
import game.map.Map;
import game.map.entity.mechanics.Move_Path;
import game.system.Process_Graphics;
import io.game_dump.Dump_Entity_Enemy;


//enemy entities; created a priori with map, or when a map is loaded from a spawner entity
public class Entity_Enemy extends Entity_Battler {
	
	private Enemy battler;
	private AI_Battle AI;
	
	public Entity_Enemy(String type,int xi,int yi,int zi,Map m){
		super(xi,yi,zi,m,0.05f);
		battler=new Enemy(type,this);
		AI=battler.make_AI(this);
		retrieve_model();
	}
	
	//return name
	public String name(){
		return battler.name;
	}
	
	public Battler battler(){
		return battler;
	}
	
	public Enemy enemy(){
		return battler;
	}
	
	//awareness methods
	
	@Override
	public boolean can_forget_about(Entity_Dynamic e){
		return AI.can_forget_about(e)&&super.can_forget_about(e);
	}
	
	//AI reference methods
	public Move_Path decide_move1(){
		return AI.move_phase_1();
	}
	
	public Move_Path decide_move2(){
		return AI.move_phase_2();
	}
	
	public AI_Action decide_action(){
		return AI.action_phase();
	}
	
	public String decide_face(){
		return AI.decide_face();
	}
	
	public boolean will_act(){
		return AI.will_act();
	}
	
	//retrieve model
	public void retrieve_model(){
		super.retrieve_model();
		//set hitbox
		hitbox=Process_Graphics.$graphics.make_box(x+0.5f,y+0.5f,z+0.5f,0.3f,0.5f,0.3f);
	}
	
	public void write(Dump_Entity_Enemy d){
		battler.write(d.battler);
		super.write(d);
	}
	
	public void read(Dump_Entity_Enemy d){
		battler.read(d.battler);
		super.read(d);
		AI=battler.make_AI(this);
	}

}
