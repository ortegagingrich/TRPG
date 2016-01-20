package game.save.inventory;

//class of standard weapon items
public class Item_Weapon extends Item_Equipable {
	
	public int str;
	public int range;
	
	public Item_Weapon(String n){
		super(n);
		set_stats();
	}
	
	private void set_stats(){
		//temporary; just set generic stats
		str=5;
		range=1;
	}

}
