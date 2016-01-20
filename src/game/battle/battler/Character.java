package game.battle.battler;

import game.save.inventory.Item_Equipable;
import game.save.inventory.Item_Weapon;
import io.game_dump.Dump_Character;



//class of playable characters; made at quest creation
public class Character extends Battler {
	
	private Item_Equipable equipped; //currently equipped item
	
	public Character(String n){
		super();
		name=n;
		//set stats
		set_stats();
	}
	
	public void set_stats(){
		//temporary set generic stats
		hp_max=30;
		str_n=1;
		def_n=1;
		acc_n=1;
		dex_n=1;
		int_n=5;
		mov_n=6;
		super.set_stats();
	}
	
	//equipment methods
	public void set_equipped(Item_Equipable e){
		equipped=e;
	}
	
	public Item_Equipable equipped(){
		return equipped;
	}
	
	//overwrite stat return methods
	
	@Override
	public int strength(){
		//get base strength
		int base=super.strength();
		//if a weapon is equipped, add its strength, otherwise, just return base
		if(equipped instanceof Item_Weapon){
			return base+((Item_Weapon)equipped).str;
		}else{
			return base;
		}
	}
	
	@Override
	public int attack_range(){
		if(equipped instanceof Item_Weapon){
			return ((Item_Weapon)equipped).range;
		}else{
			return 1;  //default attack range of 1 (punching)
		}
	}
	
	//dump methods
	
	public void write(Dump_Character d){
		d.name=name;
		super.write(d);
	}
	
	public void read(Dump_Character d){
		name=d.name;
		super.read(d);
	}

}
