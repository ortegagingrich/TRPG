package game.save;

import game.battle.battler.Character;
import game.save.inventory.Inventory;
import io.game_dump.Dump_Character;
import io.game_dump.Dump_Party;

import java.util.ArrayList;

//class of the player party; created upon quest creation
public class Party {
	
	public ArrayList<Character> characters;  //all playable characters, including unavailable characters
	public ArrayList<Character> current;  //characters currently in the party
	public Inventory inventory;
	
	//characters
	private Character chE;
	private Character chT;
	
	public Party(){
		inventory=new Inventory();
		characters=new ArrayList<Character>();
		current=new ArrayList<Character>();
	}
	
	//start method; compiles initial party
	public void start(){
		//make characters
		chE=new Character("E");
		characters.add(chE);
		current.add(chE);
		
		chT=new Character("T");
		characters.add(chT);
		current.add(chT);
	}
	
	//return character methods
	public Character chE(){
		return chE;
	}
	
	public Character chT(){
		return chT;
	}
	
	//battle methods:
	
	//checks if the current party is defeated
	public boolean defeated(){
		//check if any party members have non-zero HP
		for(Character c:current){
			if(!c.defeated()){
				return false;
			}
		}
		
		return true; //by default return true
	}
	
	//dump methods:
	
	public void write(Dump_Party d){
		for(Character c:characters){
			Dump_Character dc=new Dump_Character();
			c.write(dc);
			d.characters.add(dc);
			if(current.contains(c)){
				d.current.add(dc);
			}
		}
	}
	
	public void read(Dump_Party d){
		for(Dump_Character dc:d.characters){
			Character c=new Character("");
			c.read(dc);
			characters.add(c);
			if(d.current.contains(dc)){
				current.add(c);
			}
		}
	}

}
