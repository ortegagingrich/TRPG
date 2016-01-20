package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.save.journal.Arc;
import game.save.journal.Arc_Segment;
import game.system.Process_Graphics;
import game.system.Process_Main;
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


//panel which selects an arc
public class Panel_Arc_Select {
	
	private HUD hud;
	
	public Arc selected_arc;
	public Arc_Segment selected_segment;
	
	private boolean sidequests; //indicates whether or not side quests are shown
	private ArrayList<String> arc_identifiers;  //list of identifiers for arcs in the listbox
	private ArrayList<Integer> segment_identifiers; //list of ids for segments in the listbox
	
	public Panel_Arc_Select(HUD h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		arc_identifiers=new ArrayList<String>();
		segment_identifiers=new ArrayList<Integer>();
	}
	
	//update
	public void update(){
		
		//handle arc selection
		try{
			//check to see if selected arc matches the previous one
			Arc a=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Arc List",ListBox.class);
			if(l.getSelection().size()>0){
				String identifier=arc_identifiers.get((Integer)l.getSelectedIndices().get(0));
				a=Process_Main.quest.journal.get_arc(identifier);
			}
			if(selected_arc!=a&&a!=null){
				selected_arc=a;
				selected_segment=null;
				hud.refresh();
			}
		}catch(Exception ex){
		}
		
		//handle segment selection
		try{
			//check to see if selected segment matches the previous one
			Arc_Segment s=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Segment List",ListBox.class);
			if(l.getSelection().size()>0){
				int identifier=segment_identifiers.get((Integer)l.getSelectedIndices().get(0));
				s=selected_arc.get_segment(identifier);
			}
			if(selected_segment!=s&&s!=null){
				selected_segment=s;
				hud.refresh();
			}
		}catch(Exception ex){
			
		}
		
	}
	
	//de-select method (for switching between uses)
	public void deselect(){
		selected_segment=null;
		selected_arc=null;
	}
	
	//refresh/build method
	public void refresh(Nifty nifty,Element panel,boolean include_segment){
		
		new PanelBuilder(){{
			childLayoutVertical();
	    	width("100%");
	    	height("60%");
	    	
	    	text(new TextBuilder(){{
	    		height("10%");
        		font("Interface/Fonts/Default.fnt");
                text("Story Arcs");
	    	}});
	    	
	    	//make buttons for main/side quests
	    	panel(new PanelBuilder(){{
	    		childLayoutHorizontal();
	    		width("50%");
	    		height("10%");
	    		control(new ButtonBuilder("show_main_arcs","Main"){{
	    			width("50%");
	    			height("90%");
	    		}});
	    		control(new ButtonBuilder("show_side_arcs","Side"){{
	    			width("50%");
	    			height("90%");
	    		}});
	    	}});
	    	
	    	//make list box
    		control(new ListBoxBuilder("Arc List"){{
    			width("100%");
    			height("60%");
    			displayItems(8);
        		optionalHorizontalScrollbar();
    		}});
    		
    		//make action controls
    		panel(new PanelBuilder(){{
    			childLayoutHorizontal();
    			width("100%");
    			height("10%");
    			control(new ButtonBuilder("rename_arc","Rename"){{
    				width("50%");
    				height("100%");
    			}});
    			control(new ButtonBuilder("delete_arc","Delete"){{
    				width("25%");
    				height("100%");
    			}});
    			control(new ButtonBuilder("add_arc","New"){{
    				width("25%");
    				height("100%");
    			}});
    		}});
    		
    		
    		
    		
	    	
	    }}.build(nifty,nifty.getScreen("start"),panel);
	    
	  //same thing for arc segments
	    
	    if(include_segment){
	    	new PanelBuilder(){{
	    		childLayoutVertical();
		    	width("100%");
		    	height("50%");
		    	

	    		
	    		text(new TextBuilder(){{
		    		height("10%");
	        		font("Interface/Fonts/Default.fnt");
	                if(selected_arc!=null){
	                	text("Arc Segments: "+selected_arc.name());
	                }else{
	                	text("Arc Segments: [No Arc Selected]");
	                }
		    	}});
		    	
		    	//make list box
	    		control(new ListBoxBuilder("Segment List"){{
	    			width("100%");
	    			height("60%");
	    			displayItems(6);
	        		optionalHorizontalScrollbar();
	    		}});
	    		
	    		//make action controls
	    		panel(new PanelBuilder(){{
	    			childLayoutHorizontal();
	    			width("100%");
	    			height("10%");
	    			control(new ButtonBuilder("rename_segment","Rename"){{
	    				width("50%");
	    				height("100%");
	    			}});
	    			control(new ButtonBuilder("delete_segment","Delete"){{
	    				width("25%");
	    				height("100%");
	    			}});
	    			control(new ButtonBuilder("add_segment","New"){{
	    				width("25%");
	    				height("100%");
	    			}});
	    		}});
	    	}}.build(nifty,nifty.getScreen("start"),panel);
	    }
		
		//populate listboxes
	    //populate arc list (also make list of identifiers)
	    ListBox l=nifty.getScreen("start").findNiftyControl("Arc List",ListBox.class);
	    arc_identifiers=new ArrayList<String>();
	    if(sidequests){
	    	for(Arc a:Process_Main.quest.journal.get_side_arcs()){
	    		if(selected_arc==a){
	    			l.addItem(a.name()+"*");
	    		}else{
	    			l.addItem(a.name());
	    		}
	    		arc_identifiers.add(a.identifier());
	    	}
	    }else{
	    	for(Arc a:Process_Main.quest.journal.get_main_arcs()){
	    		if(selected_arc==a){
	    			l.addItem(a.name()+"*");
	    		}else{
	    			l.addItem(a.name());
	    		}
	    		arc_identifiers.add(a.identifier());
	    	}
	    }
	    
	    if(include_segment){
	    	//populate segment list (also make list of identifiers
		    l=nifty.getScreen("start").findNiftyControl("Segment List",ListBox.class);
		    segment_identifiers=new ArrayList<Integer>();
		    //check to see if a valid arc is selected
		    if(selected_arc!=null){
		    	for(Arc_Segment s:selected_arc.get_segments()){
		    		if(selected_segment==s){
		    			l.addItem(s.name()+"*");
		    		}else{
		    			l.addItem(s.name());
		    		}
		    		segment_identifiers.add(s.id());
		    	}
		    }
	    }
	    
	    
	}
	
	
	//button methods:
	//arc operations
	@NiftyEventSubscriber(id="show_main_arcs")
	public void show_main_arcs(String id,ButtonClickedEvent event){
		sidequests=false;
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="show_side_arcs")
	public void show_side_arcs(String id,ButtonClickedEvent event){
		sidequests=true;
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="rename_arc")
	public void rename_arc(String id,ButtonClickedEvent event){
		((HUD_Edit)hud).rename_window.show(selected_arc,selected_arc.name());
	}
	
	@NiftyEventSubscriber(id="add_arc")
	public void add_arc(String id,ButtonClickedEvent event){
		if(sidequests){
			Process_Main.quest.journal.add_side_arc();
		}else{
			Process_Main.quest.journal.add_main_arc();
		}
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="delete_arc")
	public void delete_arc(String id,ButtonClickedEvent event){
		//deprecate selected arc
		if(selected_arc!=null){
			selected_arc.deprecate();
			selected_arc=null;
			selected_segment=null;
		}
		hud.refresh();
	}
	
	//segment operations
	@NiftyEventSubscriber(id="rename_segment")
	public void rename_segment(String id,ButtonClickedEvent event){
		((HUD_Edit)hud).rename_window.show(selected_segment,selected_segment.name());
	}
	
	@NiftyEventSubscriber(id="add_segment")
	public void add_segment(String id,ButtonClickedEvent event){
		if(selected_arc!=null){
			selected_arc.add_segment();
		}
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="delete_segment")
	public void delete_segment(String id,ButtonClickedEvent event){
		//deprecate selected segment
		if(selected_segment!=null){
			selected_segment.deprecate();
			selected_segment=null;
		}
		hud.refresh();
	}
	
}
