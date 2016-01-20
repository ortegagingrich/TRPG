package game.gui.edit;

import game.*;
import game.map.entity.Entity_Event;
import game.scripting.trigger.Panel_Condition_Master;
import game.system.Process_Graphics;
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


//class for the window used to edit events
public class Window_Event {
	
	//current event entity (by default, is null)
	public Entity_Event entity;
	
	private HUD_Edit hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	//list panels (public) here
	public Panel_Event_Select panel_event_select;
	public Panel_Script_Display panel_script_display;
	public Panel_Command_Edit panel_command_edit;
	public Panel_Condition_Master panel_condition_master;
	
	public Window_Event(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=100;
		screen_y=50;
		
		//make panels here
		panel_event_select=new Panel_Event_Select(h);
		panel_script_display=new Panel_Script_Display(h);
		panel_command_edit=new Panel_Command_Edit(h);
		panel_condition_master=new Panel_Condition_Master(h);
	}
	
	//show/hide methods
	public void show(Entity_Event e){
		//if the entity is really null, do nothing
		if(e==null){
			return;
		}
		//make sure to de-select commands
		panel_script_display.selected_command=null;
		panel_script_display.selected_command_block=null;
		visible=true;
		entity=e;
		hud.refresh();
	}
	
	public void hide(){
		//de-select entity
		entity=null;
		panel_event_select.selected_event=null;
		visible=false;
		hud.refresh();
	}
	
	public void toggle(){
		visible=!visible;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update
	public void update(){
		if(!visible){
			return;
		}
		
		//if visible, update sub-panels here
		panel_event_select.update();
		panel_script_display.update();
		panel_command_edit.update();
		panel_condition_master.update();
	}
	
	//refresh method (constructs window and sub-panels)
	public void refresh(Nifty panel){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:panel.getScreen("start").getLayerElements()){
			l.disable();
		}
		
		Element layer=new LayerBuilder("event window layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),panel.getScreen("start").getRootElement());
		
		//make back panel
		Element back=new PanelBuilder("back"){{
			childLayoutHorizontal();
	    	x(Integer.toString(screen_x)+"px");
	    	y(Integer.toString(screen_y)+"px");
	    	width("80%");
	    	height("80%");
	    	style("nifty-panel");
		}}.build(panel,panel.getScreen("start"),layer);
		
		//make left/right panels
		Element left=new PanelBuilder("Left"){{
			childLayoutVertical();
			width("40%");
			height("100%");
			style("nifty-panel");
		}}.build(panel,panel.getScreen("start"),back);
		
		Element right=new PanelBuilder("Right"){{
			childLayoutVertical();
			width("60%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),back);
		
		//make sub-panels
		
		//entity name subpanel
		Element namepan=new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("3%");
			
			text(new TextBuilder(){{
				width("80%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text(entity.name());
			}});
			
			control(new ButtonBuilder("rename_entity","Rename"){{
				width("20%");
				height("100%");
			}});
			
		}}.build(panel,panel.getScreen("start"),left);
		
		//condition/event subpanel
		Element evcond=new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("25%");
		}}.build(panel,panel.getScreen("start"),left);
		
		//make event select panel
		Element eventpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("50%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),evcond);
		//fill panel
		panel_event_select.refresh(panel,eventpanel,entity);
		
		//make condition master panel
		Element conditionpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("50%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),evcond);
		//fill panel
		panel_condition_master.refresh(panel,conditionpanel,panel_event_select.selected_event);
		
		//make script display panel
		Element scriptdisp=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("70%");
		}}.build(panel,panel.getScreen("start"),left);
		//fill panel
		panel_script_display.refresh(panel,scriptdisp,panel_event_select.selected_event);
		
		//make command edit panel
		Element paneledit=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),right);
		//fill panel
		panel_command_edit.refresh(panel,paneledit,panel_script_display.selected_command);
		
		//make accept button
		new ButtonBuilder("accept","Accept"){{
			width("100%");
			height("3%");
		}}.build(panel,panel.getScreen("start"),left);
	}
	
	//button methods:
	@NiftyEventSubscriber(id="accept")
	public void accept(String id,ButtonClickedEvent event){
		//close window
		hide();
	}
	
	//rename entity
	@NiftyEventSubscriber(id="rename_entity")
	public void rename_entity(String id,ButtonClickedEvent event){
		//open rename window
		hud.rename_window.show(entity,entity.name());
	}
}
