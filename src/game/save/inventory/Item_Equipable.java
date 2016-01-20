package game.save.inventory;

import game.battle.battler.Character;

//class of equipable items
public class Item_Equipable extends Item {
	
	public boolean equipped; //boolean indicating if the item is currently in use
	public Character user; //character currently using the item, if any
	
	public Item_Equipable(String n){
		super(n);
		equipped=false;
		user=null;
	}

}
