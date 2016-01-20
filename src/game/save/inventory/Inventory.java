package game.save.inventory;

import game.battle.battler.Character;

import java.util.ArrayList;

//class for handling the parties inventory
public class Inventory {
	
	public int credits; //currency owned by the party
	
	private ArrayList<Item_Equipable> equipables;  //arraylist of equipables owned by the party
	private ArrayList<Item_Stackable> stackables;  //arraylist of stackables owned by the party
	
	public Inventory(){
		credits=0;
		equipables=new ArrayList<Item_Equipable>();
		stackables=new ArrayList<Item_Stackable>();
		//setup inventory (make sure all items are available)
	}
	
	//retrieve item methods
	
	public Item_Equipable get_equipable(String n){
		for(Item_Equipable e:equipables){
			if(e.name.equals(n)){
				return e;
			}
		}
		return null;
	}
	
	public Item_Stackable get_stackable(String n){
		for(Item_Stackable s:stackables){
			if(s.name.equals(n)){
				return s;
			}
		}
		return null;
	}
	
	//equipping methods
	public boolean equip(Character c,String n){  //boolean indicates whether or not the attempt at equipping the item was successful
		//first check to see if the character already has the item equipped
		if(c.equipped()!=null){
			if(c.equipped().name.equals(n)){
				return true;
			}
		}
		//next check to see if the item is available and not already equipped
		Item_Equipable e=get_equipable(n);
		if(e.equipped||!e.available){
			return false;
		}
		//unequip current item
		unequip(c);
		//equip the item
		e.equipped=true;
		e.user=c;
		c.set_equipped(e);
		return true;
	}
	
	public boolean equip(Character c,Item_Equipable e){
		return equip(c,e.name);
	}
	
	public void unequip(Character c){
		if(c.equipped()!=null){
			c.equipped().user=null;
			c.equipped().equipped=false;
		}
		c.set_equipped(null);
	}
	
	//forces whatever character has a given item equipped (if any) to unequip it; safety method
	public void force_unequip(Item_Equipable e){
		if(e.user!=null){
			e.user.set_equipped(null);
		}
		e.user=null;
		e.equipped=false;
	}
	
	public void force_unequip(String n){
		force_unequip(get_equipable(n));
	}
	
}
