package game.gui.edit;

import game.*;
import game.scripting.trigger.*;
import game.system.Process_Graphics;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.*;
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

public class Window_Event_Condition {
	
	private HUD_Edit hud;
	
	public Condition selected_condition;
	
	private String condition_type;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	//list panels (public) here
	public Panel_Arc_Select panel_arc_select;
	public ArrayList<Condition> current_conditions;
	
	public Window_Event_Condition(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=200;
		screen_y=90;
		
		//make panels here
		panel_arc_select=new Panel_Arc_Select(hud);
	}
	
	//show/hide methods
	public void show(ArrayList<Condition> c,String type){ //type: type of condition to be set (e.g. interact, autostart, if)
		current_conditions=c;
		condition_type=type;
		visible=true;
		hud.refresh();
	}
	
	public void hide(){
		current_conditions=null;
		visible=false;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update
	public void update(){
		//handle condition selection
		try{
			//check to see if selected event matches the previous one
			Condition c=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("event condition list",ListBox.class);
			if(l.getSelectedIndices().size()>0){
				c=current_conditions.get((Integer)l.getSelectedIndices().get(0));
			}
			if(c!=null&&selected_condition!=c){
				selected_condition=c;
				
				//make panels de-select
				panel_arc_select.deselect();
				
				hud.refresh();
			}
		}catch(Exception ex){
		}
		//try to update display for selected condition
		if(selected_condition!=null){
			selected_condition.update_edit_display();
		}
		
		//update sub-panels
		panel_arc_select.update();
	}
	
	//refresh/build method
	public void refresh(Nifty nifty){
		if(!visible||current_conditions==null){
			return;
		}
		
		//disable elements in other layers pre-existing layers
		for(Element l:nifty.getScreen("start").getLayerElements()){
			l.disable();
		}
		
		Element layer=new LayerBuilder("upper layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
			
		}}.build(nifty,nifty.getScreen("start"),nifty.getScreen("start").getRootElement());
		
		
		
		//make window panel
		Element back=new PanelBuilder(){{
			childLayoutHorizontal();
			x(Integer.toString(screen_x)+"px");
			y(Integer.toString(screen_y)+"px");
			width("60%");
			height("85%");
			
			panel(new PanelBuilder(){{
				childLayoutVertical();
				width("40%");
				height("50%");
				style("nifty-panel");
				
				text(new TextBuilder(){{
					width("100%");
					height("10%");
					font("Interface/Fonts/Default.fnt");
					text("Set "+condition_type+" conditions");
				}});
				
				control(new ListBoxBuilder("event condition list"){{
					width("100%");
					height("80%");
					displayItems(8);
					optionalHorizontalScrollbar();
					optionalVerticalScrollbar();
				}});
				
				panel(new PanelBuilder(){{
					childLayoutHorizontal();
					width("100%");
					height("10%");
					
					control(new ButtonBuilder("add_condition","Add Condition"){{
						width("50%");
						height("100%");
						font("Interface/Fonts/Console.fnt");
					}});
					control(new ButtonBuilder("remove_condition","Remove Condition"){{
						width("50%");
						height("100%");
						font("Interface/Fonts/Console.fnt");
					}});
				}});
				
				//accept and exit panel
				panel(new PanelBuilder(){{
					childLayoutCenter();
					width("100%");
					height("10%");
					
					control(new ButtonBuilder("close_event_condition_window","Accept"){{
						width("20%");
						height("100%");
					}});
				}});
			}});
			
		}}.build(nifty,nifty.getScreen("start"),layer);
		
		//make panel for right side
		Element remainder=new PanelBuilder(){{
			childLayoutVertical();
			width("60%");
			height("100%");
		}}.build(nifty,nifty.getScreen("start"),back);
		
		//panel for editing the selected condition
		Element editpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("90%");
			style("nifty-panel");
		}}.build(nifty,nifty.getScreen("start"),remainder);
		
		//if a valid condition is selected, show it in the edit panel
		if(selected_condition!=null){
			selected_condition.display(nifty,editpanel);
		}
		
		
		
		
		//populate listbox
		ListBox l=hud.panel.getScreen("start").findNiftyControl("event condition list",ListBox.class);
		for(Condition c:current_conditions){
			c.print(l,c.equals(selected_condition));
		}
	}
	
	//button methods
	@NiftyEventSubscriber(id="close_event_condition_window")
	public void close_event_condition_window(String id,ButtonClickedEvent e){
		//hide the window
		hide();
	}
	
	@NiftyEventSubscriber(id="add_condition")
	public void add_condition(String id,ButtonClickedEvent e){
		current_conditions.add(new Condition());
		//refresh the hud
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="remove_condition")
	public void remove_condition(String id,ButtonClickedEvent e){
		if(selected_condition!=null){
			current_conditions.remove(selected_condition);
			//refresh the hud
			hud.refresh();
		}
	}
}
