package game.battle.ai;

import game.battle.battler.Enemy;
import game.map.Map;
import game.map.entity.Entity_Dynamic;
import game.map.entity.Entity_Enemy;
import game.map.entity.mechanics.Move_Path;

//superclass for battle AIs; also, generic AI, which does absolutely nothing, unless methods are overriden
public class AI_Battle {
	
	public Entity_Enemy entity;
	public Enemy enemy;
	
	private Map map;
	
	public AI_Battle(Entity_Enemy e){
		entity=e;
		enemy=e.enemy();
		map=e.map;
	}
	
	//checks to see if the AI would act on the given turn
	public boolean will_act(){
		return !(move_phase_1()==null&&action_phase()==null);
	}
	
	//move decision methods
	public Move_Path move_phase_1(){
		return null;
	}
	
	public Move_Path move_phase_2(){
		return null;
	}
	
	//decide which way to face at the end of a turn
	public String decide_face(){
		return entity.face();
	}
	
	//decide on a battle action
	public AI_Action action_phase(){
		return null;
	}
	
	//determine if the entity can forget about another
	public boolean can_forget_about(Entity_Dynamic e){
		return true;
	}

}
