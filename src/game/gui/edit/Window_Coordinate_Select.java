package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.map.Map;
import game.scene.Scene_WorldEdit;
import game.scripting.Command_Teleport_Player;
import game.system.Process_Graphics;
import game.system.Process_Main;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.checkbox.*;
import de.lessvoid.nifty.controls.checkbox.builder.*;
import de.lessvoid.nifty.controls.listbox.*;
import de.lessvoid.nifty.controls.listbox.builder.*;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.textfield.*;
import de.lessvoid.nifty.controls.textfield.builder.*;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.slider.*;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.slider.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.elements.*;
import java.util.ArrayList;

import asset.model.*;

//window for selecting coordinates
public class Window_Coordinate_Select {
	
	private HUD_Edit hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	public Object object;  //object whose parameters are to selected
	private int oldmap=-1;
	
	public Window_Coordinate_Select(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=0;
		screen_y=0;
	}
	
	//show/hide methods
	public void show(Object newobject){
		//order is important for multithreading; target must be set before visibility
		object=newobject;
		visible=true;
		//freeze the hud
		hud.frozen=true;
		hud.refresh();
	}
	
	//same as above, but temporarily transitions to new map for coordinate selecting
	public void show(Object newobject,Map map){
		//backup old map
		oldmap=((Scene_WorldEdit)Process_Main.$scene).world.map.id();
		//show new map
		((Scene_WorldEdit)Process_Main.$scene).world.display_map(map);
		//super method
		show(newobject);
	}
	
	public void hide(){
		//show previous map
		if(oldmap!=-1){
			((Scene_WorldEdit)Process_Main.$scene).world.display_map(Process_Main.quest.universe.get_map(oldmap));
		}else{
		}
		//settings
		visible=false;
		object=null;
		oldmap=-1;
		//unfreeze the hud
		hud.frozen=false;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update method
	public void update(){
		//if not visible, or no object to be set, do nothing
		if(!visible||object==null){
			return;
		}
		
		//handle new value set process;
		//branch based on object type
		if(object instanceof Command_Teleport_Player){
			((Command_Teleport_Player)object).set_coordinates(((Command_Teleport_Player)object).map_id(),x(),y(),z());
		}
		
		//on left click, close the window;
		if(Process_Main.$input.trigger("Mouse Left")){
			hide();
		}
	}
	
	//refresh/build method
	public void refresh(Nifty nifty){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:nifty.getScreen("start").getLayerElements()){
			l.disable();
			l.hide();
		}
		
		Element layer=new LayerBuilder("coordinate select layer"){{
			childLayoutVertical();
			width("100%");
			height("100%");
		}}.build(nifty,nifty.getScreen("start"),nifty.getScreen("start").getRootElement());
		
		new PanelBuilder(){{
			childLayoutVertical();
			width("20%");
			height("5%");
			style("nifty-panel");
			
			text(new TextBuilder(){{
				width("100%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Select Coordinates");
			}});
		}}.build(nifty,nifty.getScreen("start"),layer);
	}
	
	//data return methods
	private int x(){
		return ((Scene_WorldEdit)Process_Main.$scene).world.selector.x;
	}
	
	private int y(){
		return ((Scene_WorldEdit)Process_Main.$scene).world.selector.y;
	}
	
	private int z(){
		return ((Scene_WorldEdit)Process_Main.$scene).world.selector.z;
	}
}
