package game.scripting.trigger;

import game.*;
import game.gui.HUD;
import game.gui.edit.HUD_Edit;
import game.scripting.Event;
import game.system.Process_Graphics;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
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


//master panel used for setting event conditions
public class Panel_Condition_Master {
	
	private HUD_Edit hud;
	
	public Panel_Condition_Master(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
	}
	
	//update method
	public void update(){
		
	}
	
	//refresh method
	public void refresh(Nifty nifty,Element panel,Event e){
		if(e==null){
			return;
		}
		//set existence condition button
		new ButtonBuilder("open_exist_condition_window","Set Existence Conditions"){{
			width("100%");
			height("20%");
		}}.build(nifty,nifty.getScreen("start"),panel);
		//set autostart condition button
		new ButtonBuilder("open_autostart_condition_window","Set Auto-Start Conditions"){{
			width("100%");
			height("20%");
		}}.build(nifty,nifty.getScreen("start"),panel);
		//set interact condition button
		new ButtonBuilder("open_interact_condition_window","Set Interaction Conditions"){{
			width("100%");
			height("20%");
		}}.build(nifty,nifty.getScreen("start"),panel);
	}
	
	//button activation methods
	
	@NiftyEventSubscriber(id="open_exist_condition_window")
	public void open_exist_condition_window(String id,ButtonClickedEvent e){
		//open condition window for existence conditions
		hud.event_condition_window.show(hud.event_window.panel_event_select.selected_event.existence_conditions(),"existence");
	}
	
	@NiftyEventSubscriber(id="open_autostart_condition_window")
	public void open_autostart_condition_window(String id,ButtonClickedEvent e){
		hud.event_condition_window.show(hud.event_window.panel_event_select.selected_event.autostart_conditions(),"auto-start");
	}
	
	@NiftyEventSubscriber(id="open_interact_condition_window")
	public void open_interact_condition_window(String id,ButtonClickedEvent e){
		hud.event_condition_window.show(hud.event_window.panel_event_select.selected_event.interact_conditions(),"interaction");
	}
}
