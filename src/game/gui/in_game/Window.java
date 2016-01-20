package game.gui.in_game;

import de.lessvoid.nifty.*;
import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.builder.*;

//superclass of gui windows (only for in game; editor windows do not have this as a superclass)
public class Window {
	
	protected HUD_Main hud;
	
	protected int screen_x;
	protected int screen_y;
	protected int width;
	protected int height;
	
	protected boolean visible;
	
	public Window(HUD_Main hu,boolean initvis,int sx,int sy,int w,int h){
		hud=hu;
		visible=initvis;
		//set location
		screen_x=sx;
		screen_y=sy;
		width=w;
		height=h;
	}
	
	//position manipulation methods
	public int screen_x(){
		return screen_x;
	}
	
	public int screen_y(){
		return screen_y;
	}
	
	public void set_position(int sx,int sy){
		screen_x=sx;
		screen_y=sy;
	}
	
	//visibility
	public boolean visible(){
		return visible;
	}
	
	public void show(){
		visible=true;
		hud.refresh();
	}
	
	public void hide(){
		visible=false;
		hud.refresh();
	}
	
	public void toggle(){
		visible=!visible;
		hud.refresh();
	}
	
	//update method (overwritten)
	public void update(){
		//update dependent sub-panels, if applicable
	}
	
	//refresh method (overwritten; this just shows a template)
	public void refresh(Nifty nifty,Element parent){
		//if window is not visible, just return
		if(!visible){
			return;
		}
		//make base window
		Element base=new PanelBuilder(""){{
			childLayoutAbsolute();
			x(Integer.toString(screen_x)+"px");
			y(Integer.toString(screen_y)+"px");
			width(Integer.toString(width)+"px");
			height(Integer.toString(height)+"px");
			style("nifty-panel-bright");
		}}.build(nifty,nifty.getScreen("start"),parent);
		
		//show sub-panels inside of the base window
		
	}
}
