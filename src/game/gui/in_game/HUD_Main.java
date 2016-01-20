package game.gui.in_game;

import de.lessvoid.nifty.*;
import game.*;
import game.gui.HUD;
import game.system.Process_Graphics;

//main HUD for gameplay; will contain separate screens for menu/exploration/initiative etc.
//note that the "screens" are not Nifty-gui screens (only start screen is used here), but instances of gui.Screen class
public class HUD_Main extends HUD {
	
	private Screen current_screen;
	//screens
	private Screen blank_screen;
	private Screen_Main main_screen;
	
	private Nifty nifty;
	
	public HUD_Main(){
		nifty=Process_Graphics.$graphics.make_nifty();
		//make screens
		blank_screen=new Screen(this,nifty);
		main_screen=new Screen_Main(this,nifty);
		
		//set to main screen
		switch_main();
	}
	
	//switch to a screen
	public void switch_blank(){
		current_screen=blank_screen;
		refresh();
	}
	
	public void switch_main(){
		current_screen=main_screen;
		refresh();
	}
	
	//retrieve screens
	public Screen_Main main_screen(){
		return main_screen;
	}
	
	//update
	public void update(){
		//update current_screen
		current_screen.update();
	}
	
	//refresh
	public void refresh(){
		//remove previous screen
		nifty.removeScreen("start");
		//show new screen
		current_screen.display(nifty);
	}
	
}
