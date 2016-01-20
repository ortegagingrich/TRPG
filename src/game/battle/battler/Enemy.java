package game.battle.battler;

import game.battle.Battle_Action;
import game.battle.ai.AI_Battle;
import game.battle.ai.AI_Battle_Aggressive;
import game.map.entity.Entity_Dynamic;
import game.map.entity.Entity_Enemy;
import io.game_dump.Dump_Enemy;



//enemy battlers; made as needed
public class Enemy extends Battler {
	
	public String type;
	public boolean resurrectable;
	
	public Enemy(String t){
		super();
		type=t;
		name=type;
		//set stats
		set_stats();
	}
	
	public Enemy(String t,Entity_Dynamic e){
		super();
		type=t;
		name=type;
		entity=e;
		//set stats
		set_stats();
	}
	
	public void set_stats(){
		//temporary set generic stats
		hp_max=1;
		str_n=1;
		def_n=1;
		acc_n=1;
		dex_n=1;
		int_n=1;
		mov_n=6;
		resurrectable=false; //by default, enemies are not resurrectable
		super.set_stats();
	}
	
	public AI_Battle make_AI(Entity_Enemy e){
		AI_Battle ai;
		
		//temporary; just make generic aggressive AI (mindlessly attacks first player it sees)
		ai=new AI_Battle_Aggressive(e);
		
		return ai;
	}
	
	//battle action methods
	
	public Battle_Action rally_allies(){
		Battle_Action a=new Battle_Action();
		a.splash_range=visibility();
		a.affects_enemies=false;
		a.alert=entity.aware_of();
		a.make_aggro=entity.concentrating_on();
		return a;
	}
	
	//dump methods:
	
	public void write(Dump_Enemy d){
		d.type=type;
		super.write(d);
	}
	
	public void read(Dump_Enemy d){
		type=d.type;
		name=type;
		super.read(d);
	}

}
