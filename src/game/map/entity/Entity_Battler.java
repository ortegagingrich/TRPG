package game.map.entity;

import game.battle.battler.Battler;
import game.map.Map;

public class Entity_Battler extends Entity_Dynamic {
	
	public Entity_Battler(float xi,float yi,float zi,Map m,float s){
		super(xi,yi,zi,m,s);
	}
	
	//get battler
	public Battler battler(){
		return null;
	}
	
	//awareness
	public int visibility(){
		if(battler()==null){
			return super.visibility();
		}else{
			return battler().visibility();
		}
	}

}
