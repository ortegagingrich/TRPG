package game.battle.ai;

import game.battle.Battle_Action;
import game.map.entity.Entity_Dynamic;

//encapsulates an AI's proposed action; contains a battle action and a target
public class AI_Action {
	
	public Entity_Dynamic target;
	public Battle_Action action;
	
	public AI_Action(Entity_Dynamic t,Battle_Action a){
		target=t;
		action=a;
	}

}
