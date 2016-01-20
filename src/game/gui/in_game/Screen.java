package game.gui.in_game;

import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.*;

//superclass of screens for the main hud
public class Screen {
	
	protected HUD_Main hud;
	
	public Screen(HUD_Main h,Nifty n){
		hud=h;
	}
	
	//update (to be overwritten as needed)
	public void update(){
		
	}
	
	//main display method (overwritten by subclasses) Note: disposal of previous screen already done in HUD_Main class
	public void display(Nifty n){
		//blank hud
		n.addScreen("start",new ScreenBuilder("start"){{
			//blank gui screen (e.g. for cutscenes)
		}}.build(n));
		//show screen
		n.gotoScreen("start");
	}
}
