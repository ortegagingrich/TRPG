package game.save.inventory;

//class of inventory items
public class Item {
	
	public String name;
	public boolean available;  //boolean indicating if this particular item is available to the party (shows up in inventory)
	
	public Item(String n){
		name=n;
		available=false;
	}
	
}
