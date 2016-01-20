package game.gui.in_game;

import game.scene.Scene_Initiative;
import game.system.Process_Main;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Controller_Initiative implements ScreenController {
	
	@Override
	public void bind(Nifty n, Screen s) {
		
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {
		
	}
	
	//interact methods:
	
	public void trigger_skip(){
		trigger("Skip");
	}
	
	public void trigger_move(){
		trigger("Move");
	}
	
	public void trigger_attack(){
		trigger("Attack");
	}
	
	public void trigger_initiative(){
		trigger("Initiative");
	}
	
	public void trigger(String s){
		((Scene_Initiative)Process_Main.$scene).hud.make_trigger(s);
	}

}
