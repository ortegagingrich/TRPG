package game.scripting.message;

import java.io.Serializable;
import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.effects.impl.TextColor;


//superclass of message fragments; prints directly to a nifty panel
public class Fragment implements Serializable {
	public static final long serialVersionUID=1L;
	
	public Fragment(){
		
	}
	
	//size (used to determine how many will fit in a row)
	public int size(){
		return to_print().length();
	}
	
	//boolean indicates if wrapping is allowed for this fragment; by default, true
	public boolean wrap_allowed(){
		return true;
	}
	
	//gets string to print
	protected String to_print(){
		return "";
	}
	
	//prints to a given nifty panel
	public void print(Nifty nifty,Element panel){
		new TextBuilder(){{
			color("ffffff00");  //nifty-gui sucks;
			font("Interface/Fonts/Default.fnt");
            text(to_print());
		}}.build(nifty,nifty.getScreen("start"),panel);
	}
}
