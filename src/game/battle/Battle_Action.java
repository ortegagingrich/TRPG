package game.battle;

import game.battle.battler.Battler;
import game.battle.battler.Enemy;
import game.map.entity.Entity_Dynamic;

import java.util.ArrayList;

//class of battle actions; contains the data related to an attack/skill
public class Battle_Action {
	
	//range parameters
	public int splash_range;  //range of effect; 0, if only one target
	public boolean affects_allies;  //action can affect allies
	public boolean affects_enemies;  //action can affect enemies
	
	//attributes; note some may not be 0, indicating no effect in that regard
	public int power; //effect on health; note that this is negative for healing actions
	
	//special effects
	public ArrayList<Entity_Dynamic> alert;  //entities which the action will make the target aware of
	public Entity_Dynamic make_aggro;  //makes the target set this entity as its primary target;
	
	public Battle_Action(){
		//default range parameters
		splash_range=0;
		affects_allies=true;
		affects_enemies=true;
		//default attributes
		power=0;
		//default special effects effects
		alert=new ArrayList<Entity_Dynamic>();
		make_aggro=null;
	}
	
	public void execute(Battler target){
		//attribute effects:
		
		//determine damage
		//temporary: just do full damage equal to power
		target.damage(power);
		
		//special effects:
		
		//alert target to other entities
		for(Entity_Dynamic e: alert){
			target.entity.make_aware_of(e);
		}
		
		//make aggro
		if(target instanceof Enemy&&make_aggro!=null){
			target.entity.make_aware_of(make_aggro);
			target.entity.make_concentrate_on(make_aggro);
		}
	}

}
