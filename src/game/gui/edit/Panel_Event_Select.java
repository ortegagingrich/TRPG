package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.map.entity.Entity_Event;
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


//panel to be used within the event window; selects which 
public class Panel_Event_Select {
	
	private HUD_Edit hud;
	
	public Event selected_event;
	
	private Entity_Event entity;
	private ArrayList<Event> events;
	
	public Panel_Event_Select(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		events=new ArrayList<Event>();
	}
	
	//update
	public void update(){
		//if no entity selected, then return
		if(entity==null){
			return;
		}
		//handle event selection
		try{
			//check to see if selected event matches the previous one
			Event e=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Event List",ListBox.class);
			if(l.getSelectedIndices().size()>0){
				e=entity.events.get((Integer)l.getSelectedIndices().get(0));
			}
			if(e!=null&&selected_event!=e){
				selected_event=e;
				hud.refresh();
			}
		}catch(Exception ex){
		}
	}
	
	//refresh/build method
	public void refresh(Nifty nifty,Element panel,Entity_Event event){
		//if no entity selected, return
		if(event==null){
			return;
		}
		
		new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				height("20%");
        		font("Interface/Fonts/Default.fnt");
                text("Events");
			}});
			
			//make list box
			control(new ListBoxBuilder("Event List"){{
				width("100%");
				height("70%");
				displayItems(4);
				optionalHorizontalScrollbar();
				optionalVerticalScrollbar();
			}});
			
			//make panel for event addition, insertion, deletion
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("20%");
				
				control(new ButtonBuilder("add_event","Add"){{
					width("40%");
					height("100%");
				}});
				
				control(new ButtonBuilder("rename_event","Rename"){{
					width("20%");
					height("100%");
				}});
				
				control(new ButtonBuilder("insert_event","Insert"){{
					width("20%");
					height("100%");
				}});
				
				control(new ButtonBuilder("delete_event","Delete"){{
					width("20%");
					height("100%");
				}});
			}});
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		//populate listbox
		entity=event;
		events.clear();
		if(event!=null){
			ListBox l=nifty.getScreen("start").findNiftyControl("Event List",ListBox.class);
			int n=1;
			for(Event e:event.events){
				events.add(e);
				if(e.equals(selected_event)){
					l.addItem("***"+Integer.toString(n)+":"+e.name);
				}else{
					l.addItem(Integer.toString(n)+":"+e.name);
				}
				n++;
			}
		}
	}
	
	//button methods
	
	@NiftyEventSubscriber(id="add_event")
	public void add(String id,ButtonClickedEvent event){
		//add an new event
		entity.events.add(new Event());
		//refresh the hud
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="rename_event")
	public void rename(String id,ButtonClickedEvent event){
		//open rename window for the selected event
		if(selected_event!=null){
			hud.rename_window.show(selected_event,selected_event.name);
		}
		//refresh the hud
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="insert_event")
	public void insert(String id,ButtonClickedEvent event){
		//get the index of the selected event
		ListBox l=hud.panel.getScreen("start").findNiftyControl("Event List",ListBox.class);
		//check to make sure an event is selected
		if(l.getSelectedIndices().size()==0){
			//if nothing selected, do nothing
			return;
		}
		//add a new event at the appropriate spot
		entity.events.add((Integer)l.getSelectedIndices().get(0),new Event());
		//refresh the hud
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="delete_event")
	public void delete(String id,ButtonClickedEvent event){
		//if an event is selected (not left over from previous selection)
		ListBox l=hud.panel.getScreen("start").findNiftyControl("Event List",ListBox.class);
		if(l.getSelectedIndices().size()==0){
			//do nothing
			return;
		}
		//remove the selected event
		entity.events.remove(selected_event);
		//refresh the hud
		hud.refresh();
	}
}
