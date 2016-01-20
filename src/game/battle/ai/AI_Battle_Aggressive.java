package game.battle.ai;

import game.battle.battler.Enemy;
import game.map.entity.Entity_Battler;
import game.map.entity.Entity_Dynamic;
import game.map.entity.Entity_Enemy;
import game.map.entity.Entity_Player;
import game.map.entity.mechanics.Move_Path;

//AI subclass for AIs involving a target
public class AI_Battle_Aggressive extends AI_Battle {
	
	private Entity_Battler target;
	
	public AI_Battle_Aggressive(Entity_Enemy e){
		super(e);
	}
	
	//target-related methods
	
	private void find_target(){
		//find closest target
		int dtest=100;
		target=null;
		for(Entity_Dynamic e:entity.aware_of()){
			int nd=entity.taxi_distance_from(e);
			if(nd<dtest&&e instanceof Entity_Player){
				dtest=nd;
				target=(Entity_Player)e;
				entity.make_concentrate_on(e);
			}
		}
	}
	
	private boolean confirm_target(){
		if(target==null){
			find_target();
		}else if(target.battler().defeated()){
			find_target();
		}else if(!entity.aware_of().contains(target)){
			find_target();
		}
		
		return target!=null;
	}
	
	//override methods:
	
	//move decision methods
	@Override
	public Move_Path move_phase_1(){
		if(!confirm_target()){
			return null;
		}
		
		//if already in attack range, don't move
		if(entity.taxi_distance_from(target)<=entity.battler().attack_range()){
			return null;
		}
		
		//check all potential move paths and choose the one that gets closest to the target
		int dtest=entity.taxi_distance_from(target);
		Move_Path proposed=null;
		for(Move_Path p:entity.available_moves(entity.battler().move_speed())){
			if(target.taxi_distance_from(p.destination)<dtest){
				dtest=target.taxi_distance_from(p.destination);
				proposed=p;
			}
		}
		return proposed;
	}
	
	@Override
	public Move_Path move_phase_2(){
		//if no target, do nothing
		if(!confirm_target()){
			return null;
		}
		
		//by default, do the same as would in phase 1
		return move_phase_1();
	}
	
	//decide which way to face at the end of a turn
	@Override
	public String decide_face(){
		return entity.face();
	}
	
	//decide on a battle action
	@Override
	public AI_Action action_phase(){
		if(!confirm_target()){
			return null;
		}
		
		//if in attack range, attack the target, otherwise alert other monsters
		if(entity.taxi_distance_from(target)<=entity.battler().attack_range()){
			return new AI_Action(target,entity.battler().attack());
		}else{
			return new AI_Action(entity,((Enemy)entity.battler()).rally_allies());
		}
	}
	
	@Override
	public boolean can_forget_about(Entity_Dynamic e){
		return e!=target;
	}

}
