package game.gui.right_menu;

import game.*;
import game.gui.HUD;
import game.gui.edit.HUD_Edit;
import game.map.entity.Entity_Event;
import game.scene.Scene_WorldEdit;
import game.scene.World;
import game.system.Clipboard;
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

//class for right click menu
public class Window_Event_Right_Click_Menu {
	
	private HUD_Edit hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	private static final String buttonwidth="70px";
	private static final String buttonheight="30px";
	
	public static Entity_Event target;
	
	public Window_Event_Right_Click_Menu(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=0;
		screen_y=0;
	}
	
	//show/hide methods
	
	public void show(Entity_Event e){
		visible=true;
		screen_x=Process_Main.$input.mouse_x();
		screen_y=Process_Main.$input.mouse_y();
		System.out.println(screen_x);
		target=e;
		//lock the selector
		world().selector.locked=true;
		//refresh the hud
		hud.refresh();
	}
	
	public void hide(){
		if(visible){
			visible=false;
			target=null;
			//unlock the selector
			world().selector.locked=false;
			//refresh the hud
			hud.refresh();
		}
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update method
	public void update(){
		//do nothing
	}
	
	//refresh/build method(s)
	public void refresh(Nifty nifty){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:nifty.getScreen("start").getLayerElements()){
			l.disable();
			//l.hide();
		}
		
		Element layer=new LayerBuilder("right click menu layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(nifty,nifty.getScreen("start"),nifty.getScreen("start").getRootElement());
		
		//make panel
		Element panel=new PanelBuilder(){{
			childLayoutVertical();
			x(Integer.toString(screen_x)+"px");
	    	y(Integer.toString(screen_y)+"px");
	    	width("5px");
	    	height("5px");
		}}.build(nifty,nifty.getScreen("start"),layer);
		
		//branch based on whether or not there is an event selected
		if(target==null){
			//new event button
			new ButtonBuilder("make_new_event_entity","New"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
			//new teleport event button
			new ButtonBuilder("make_new_teleport_event_entity","New Teleport"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
			//paste event button
			new ButtonBuilder("paste_selected_event_entity","Paste"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
		}else{
			//edit event button
			new ButtonBuilder("edit_selected_event_entity","Edit"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
			//copy event button
			new ButtonBuilder("copy_selected_event_entity","Copy"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
			//delete event button
			new ButtonBuilder("delete_selected_event_entity","Delete"){{
				width(buttonwidth);
				height(buttonheight);
			}}.build(nifty,nifty.getScreen("start"),panel);
		}
		
	}
	
	//button methods?
	
	@NiftyEventSubscriber(id="make_new_event_entity")
	public void make_new_event_entity(String id,ButtonClickedEvent ev){
		world().map.add_entity_event(world().secondary_selector.x,world().secondary_selector.y,world().secondary_selector.z);
		hide();
	}
	
	@NiftyEventSubscriber(id="make_new_teleport_event_entity")
	public void make_new_teleport_event_entity(String id,ButtonClickedEvent ev){
		world().map.add_entity_event(Entity_Event.make_teleport_event(world().secondary_selector.x,world().secondary_selector.y,world().secondary_selector.z,world().map));
		hide();
	}
	
	@NiftyEventSubscriber(id="edit_selected_event_entity")
	public void edit_selected_event_entity(String id,ButtonClickedEvent ev){
		hud.event_window.show(world().map.retrieve_entity_event(world().secondary_selector.x,world().secondary_selector.z));
		hide();
	}
	
	@NiftyEventSubscriber(id="copy_selected_event_entity")
	public void copy_selected_event_entity(String id,ButtonClickedEvent ev){
		//copy selected event (if there is one)
		Entity_Event e=world().map.retrieve_entity_event(world().secondary_selector.x,world().secondary_selector.z);
		if(e!=null){
			Clipboard.save_entity_event(e);
		}
		hide();
	}
	
	@NiftyEventSubscriber(id="paste_selected_event_entity")
	public void paste_selected_event_entity(String id,ButtonClickedEvent ev){
		//retrieve new entity
		Entity_Event e=Clipboard.get_entity_event();
		if(e!=null){
			//move the new entity to the secondary selector
			e.set_position(world().secondary_selector.x,world().secondary_selector.y,world().secondary_selector.z);
			//add entity to the map
			world().map.add_entity_event(e);
		}
		hide();
	}
	
	@NiftyEventSubscriber(id="delete_selected_event_entity")
	public void delete_selected_event_entity(String id,ButtonClickedEvent ev){
		world().map.remove_entity_event(world().secondary_selector.x,world().secondary_selector.z);
		hide();
	}
	
	//convenient retrieve methods
	private World world(){
		return ((Scene_WorldEdit)Process_Main.$scene).world;
	}
	
}
