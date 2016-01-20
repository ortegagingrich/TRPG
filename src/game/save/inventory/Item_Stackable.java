package game.save.inventory;

public class Item_Stackable extends Item {
	
	public byte quantity; //number of this particular item; can only hold up to 64 of each type
	
	public Item_Stackable(String n){
		super(n);
		//set initial quantity
		quantity=0;
	}

}
