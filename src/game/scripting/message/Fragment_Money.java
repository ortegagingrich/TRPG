package game.scripting.message;

import java.io.Serializable;
import game.*;
import game.system.Process_Main;

//class of fragments that return the number of credits the party has
//syntax: "@money"
public class Fragment_Money extends Fragment implements Serializable {
	public static final long serialVersionUID=1L;
	
	public Fragment_Money(){
		
	}
	
	//return the string to print
	@Override
	protected String to_print(){
		return Integer.toString(Process_Main.quest.party.inventory.credits);
	}
	
}
