package game.gui.in_game;

import de.lessvoid.nifty.*;
import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.builder.*;

public class Screen_Main extends Screen {
	
	//windows
	public Window test_window;
	public Window_Message message_window;
	
	public Screen_Main(HUD_Main h,Nifty n){
		super(h,n);
		//make windows
		test_window=new Window(hud,false,400,650,800,250);
		message_window=new Window_Message(hud,n);
	}
	
	//update
	@Override
	public void update(){
		//update windows
		test_window.update();
		message_window.update();
	}
	
	//display method
	@Override
	public void display(Nifty n){
		n.addScreen("start",new ScreenBuilder("start"){{
			//layer
			layer(new LayerBuilder("layer"){{
				childLayoutAbsolute();
				width("100%");
				height("100%");
			}});
		}}.build(n));
		
		//show windows
		Element l=n.getScreen("start").getLayerElements().get(0);
		//test_window.refresh(n,l);
		message_window.refresh(n,l);
		
		//show screen
		n.gotoScreen("start");
	}
}
