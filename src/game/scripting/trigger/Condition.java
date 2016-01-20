package game.scripting.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.checkbox.*;
import de.lessvoid.nifty.controls.checkbox.builder.*;
import de.lessvoid.nifty.controls.listbox.*;
import de.lessvoid.nifty.controls.listbox.builder.*;
import de.lessvoid.nifty.controls.*;
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
import game.*;
import game.map.entity.Entity_Event;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;


//superclass of event trigger conditions; method will check to see if the event condition is satisfied
//also, acts as automatic fail condition
public class Condition implements Serializable {
	public static final long serialVersionUID=1L;
	
	public Condition(){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
	}
	
	//note: the parent event is fed in as an argument, since the condition cannot save the parent as a field
	//this method will be overwritten by subclasses of conditions
	public boolean satisfied(Event parent,Entity_Event e){
		//by default, return false;
		return false;
	}
	
	//text for printing
	public String to_print(){
		return "[Never]";
	}
	
	public void print(ListBox l,boolean selected){
		String s="";
		if(selected){
			s="***";
		}
		try{
			s+=Integer.toString(l.getItems().size());
		}catch(Exception ex){
			s+="[failure]";
		}
		s+=":";
		s+=this.to_print();
		l.addItem(s);
	}
	
	//edit display methods
	public void display(Nifty nifty,Element panel){
		new PanelBuilder(){{
			width("100%");
			height("100%");
			childLayoutVertical();
			alignCenter();
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Select Condition Type");
			}});
			
			//buttons for selecting a condition type
			
			//quest based condition
			text(new TextBuilder(){{
				width("25%");
				height("5%");
				font("Interface/Fonts/Console.fnt");
				text("Quest Conditions");
			}});
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("5%");
				
				control(new ButtonBuilder("make_condition_arc","Arc Condition"){{
					width("25%");
					height("100%");
				}});
				control(new ButtonBuilder("make_condition_segment","Segment Condition"){{
					width("25%");
					height("100%");
				}});
			}});
			
			//entity based conditions
			text(new TextBuilder(){{
				width("25%");
				height("5%");
				font("Interface/Fonts/Console.fnt");
				text("Entity Conditions");
			}});
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("5%");
				
				control(new ButtonBuilder("make_condition_proximity","Hero Proximity"){{
					width("25%");
					height("100%");
				}});
			}});
			
			//variable conditions
			text(new TextBuilder(){{
				width("25%");
				height("5%");
				font("Interface/Fonts/Console.fnt");
				text("Variable Conditions");
			}});
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("5%");
				
				control(new ButtonBuilder("make_condition_switch","Switch Condition"){{
					width("25%");
					height("100%");
				}});
			}});
			
		}}.build(nifty,nifty.getScreen("start"),panel);
	}
	
	//update edit display
	public void update_edit_display(){
		
	}
	
	//button methods
	
	private static Condition lastcondition=null;
	
	//necessary to circumvent oddities of niftygui/jme3
	protected boolean fixnifty(){
		Condition test=((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.selected_condition;
		if(test.equals(lastcondition)){
			return true;
		}
		lastcondition=test;
		
		//remove old command
		((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.current_conditions.remove(test);
		return false;
	}
	
	@NiftyEventSubscriber(id="make_condition_arc")
	public void make_condition_arc(String id,ButtonClickedEvent event){
		//necessary to circumvent oddities of niftygui/jme3
		if(fixnifty()){
			return;
		}
		//add new arc condition
		Condition condition=new Condition_Arc(null,false,"active");
		((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.current_conditions.add(condition);
		//refresh the hud
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
	
	@NiftyEventSubscriber(id="make_condition_segment")
	public void make_condition_segment(String id,ButtonClickedEvent event){
		if(fixnifty()){
			return;
		}
		//add new segment condition
		Condition condition=new Condition_Segment(null,false,"active");
		((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.current_conditions.add(condition);
		//refresh the hud
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
	
	@NiftyEventSubscriber(id="make_condition_proximity")
	public void make_condition_proximity(String id,ButtonClickedEvent event){
		if(fixnifty()){
			return;
		}
		//add new condition
		Condition condition=new Condition_Proximity(3);
		((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.current_conditions.add(condition);
		//refresh the hud
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
	
	@NiftyEventSubscriber(id="make_condition_switch")
	public void make_condition_switch(String id,ButtonClickedEvent event){
		if(fixnifty()){
			return;
		}
		//add new condition
		Condition condition=new Condition_Switch(null,false);
		((Scene_WorldEdit)Process_Main.$scene).hud.event_condition_window.current_conditions.add(condition);
		//refresh the hud
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
}
