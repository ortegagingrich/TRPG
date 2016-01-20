package game.battle.battler;

import game.battle.Battle_Action;
import game.map.entity.Entity_Dynamic;
import io.game_dump.Dump_Battler;



//class of battlers with battle stats; made as needed
public class Battler{
	
	public Entity_Dynamic entity; //battler's entity, if available
	
	public String name;
	
	protected int hp_max;
	protected int hp;
	protected int str_n;
	protected int def_n;
	protected int acc_n;
	protected int dex_n;
	protected int mov_n;
	protected int int_n;
	
	public void set_stats(){
		hp=hp_max;
	}
	
	
	//battle stat methods:
	
	public int hp_max(){
		return hp_max;
	}
	
	public int hp(){
		return hp;
	}
	
	public int strength(){
		return str_n;
	}
	
	public int defense(){
		return def_n;
	}
	
	public int accuracy(){
		return acc_n;
	}
	
	public int dexterity(){
		return dex_n;
	}
	
	public int intelligence(){
		return int_n;
	}
	
	public int move_speed(){
		return mov_n;
	}
	
	public int visibility(){
		return 12;
	}
	
	public int attack_range(){
		return 1;
	}
	
	public boolean defeated(){
		if(hp<=0){
			return true;
		}
		return false;
	}
	
	//stat modification commands
	
	public void damage(int val){
		hp-=val;
		if(hp<0){
			hp=0;
		}
		if(hp>hp_max){
			hp=hp_max;
		}
	}
	
	public void heal(int val){
		damage(-val);
	}
	
	public void kill(){
		hp=0;
	}
	
	public void full_heal(){
		hp=hp_max;
	}
	
	
	//battle action generating methods:
	
	public Battle_Action attack(){
		Battle_Action a=new Battle_Action();
		a.power=strength();
		return a;
	}
	
	public Battle_Action alert_allies(){
		Battle_Action a=new Battle_Action();
		a.splash_range=visibility();
		a.affects_enemies=false;
		a.alert=entity.aware_of();
		return a;
	}
	
	// I/O methods:
	
	public void write(Dump_Battler d){
		d.hp=hp;
		d.hp_max=hp_max;
		d.def_n=def_n;
		d.acc_n=acc_n;
		d.dex_n=dex_n;
		d.mov_n=mov_n;
	}
	
	public void read(Dump_Battler d){
		hp=d.hp;
		hp_max=d.hp_max;
		def_n=d.def_n;
		acc_n=d.acc_n;
		dex_n=d.dex_n;
		mov_n=d.mov_n;
	}

}
