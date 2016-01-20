package game.gui.edit;

import game.*;
import game.scripting.Switch;
import game.scripting.Variable;
import game.scripting.Variable_Handler;
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


//window for variable creation/selection
public class Window_Variables {
	
	private HUD_Edit hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	public Variable selected_variable;
	public Switch selected_switch;
	
	private Variable_Handler handler;
	
	public Window_Variables(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=600;
		screen_y=100;
	}
	
	//show/hide methods
	
	public void show(Variable_Handler newh){
		//order is important for multithreading; target must be set before visibility
		handler=newh;
		visible=true;
		selected_variable=null;
		selected_switch=null;
		hud.refresh();
	}
	
	public void hide(){
		visible=false;
		handler=null;
		selected_variable=null;
		selected_switch=null;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update
	public void update(){
		//handle switch selection
		try{
			//check to see if selected switch matches the previous one
			Switch s=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Switch List",ListBox.class);
			if(l.getSelection().size()>0){
				s=handler.get_switch((Integer)l.getSelectedIndices().get(0));
			}
			if(selected_switch!=s&&s!=null){
				selected_switch=s;
				hud.refresh();
			}
		}catch(Exception ex){}
		//handle variable selection
		try{
			//check to see if selected variable matches the previous one
			Variable v=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Variable List",ListBox.class);
			if(l.getSelection().size()>0){
				v=handler.get_variable((Integer)l.getSelectedIndices().get(0));
			}
			if(selected_variable!=v&&v!=null){
				selected_variable=v;
				hud.refresh();
			}
		}catch(Exception ex){}
	}
	
	//refresh/build
	public void refresh(Nifty panel){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:panel.getScreen("start").getLayerElements()){
			l.disable();
		}
		
		Element layer=new LayerBuilder("variable layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),panel.getScreen("start").getRootElement());
		
		new PanelBuilder(){{
			childLayoutVertical();
			x(Integer.toString(screen_x)+"px");
			y(Integer.toString(screen_y)+"px");
	    	width("25%");
	    	height("75%");
	    	style("nifty-panel");
	    	
	    	text(new TextBuilder(){{
	    		height("5%");
        		font("Interface/Fonts/Default.fnt");
                text("Local Variables");
	    	}});
	    	
	    	text(new TextBuilder(){{
	    		height("5%");
        		font("Interface/Fonts/Default.fnt");
                text("Switches:");
	    	}});
	    	
	    	//make list box
    		control(new ListBoxBuilder("Switch List"){{
    			width("100%");
    			height("30%");
    			displayItems(9);
        		optionalHorizontalScrollbar();
    		}});
    		
    		//make action controls
    		panel(new PanelBuilder(){{
    			childLayoutHorizontal();
    			width("100%");
    			height("3%");
    			control(new ButtonBuilder("rename_switch","Rename"){{
    				width("50%");
    				height("100%");
    			}});
    			control(new ButtonBuilder("add_switch","New"){{
    				width("50%");
    				height("100%");
    			}});
    		}});
    		
    		//same thing for arc segments
    		
    		text(new TextBuilder(){{
	    		height("5%");
        		font("Interface/Fonts/Default.fnt");
                text("Variables");
	    	}});
	    	
	    	//make list box
    		control(new ListBoxBuilder("Variable List"){{
    			width("100%");
    			height("30%");
    			displayItems(9);
        		optionalHorizontalScrollbar();
    		}});
    		
    		//make action controls
    		panel(new PanelBuilder(){{
    			childLayoutHorizontal();
    			width("100%");
    			height("3%");
    			control(new ButtonBuilder("rename_variable","Rename"){{
    				width("50%");
    				height("100%");
    			}});
    			control(new ButtonBuilder("add_variable","New"){{
    				width("50%");
    				height("100%");
    			}});
    		}});
    		
    		//make button to close the window
    		control(new ButtonBuilder("close_variable_window","Accept"){{
    			width("100%");
    			height("5%");
    		}});
	    	
	    }}.build(panel,panel.getScreen("start"),layer);
	    
	    //populate listboxes
	    //populate switch listbox
	    ListBox l=panel.getScreen("start").findNiftyControl("Switch List",ListBox.class);
	    for(Switch s:handler.get_switches()){
	    	if(selected_switch==s){
	    		l.addItem(Integer.toString(s.id())+":"+s.name()+"*");
	    	}else{
	    		l.addItem(Integer.toString(s.id())+":"+s.name());
	    	}
	    }
	    //populate variable listbox
	    l=panel.getScreen("start").findNiftyControl("Variable List",ListBox.class);
	    for(Variable v:handler.get_variables()){
	    	if(selected_variable==v){
	    		l.addItem(Integer.toString(v.id())+":"+v.name()+"*");
	    	}else{
	    		l.addItem(Integer.toString(v.id())+":"+v.name());
	    	}
	    }
	    
	}
	
	//button interact methods
	
	@NiftyEventSubscriber(id="add_switch")
	public void add_switch(String id,ButtonClickedEvent event){
		handler.make_switch("---");
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="rename_switch")
	public void rename_switch(String id,ButtonClickedEvent event){
		hud.rename_window.show(selected_switch,selected_switch.name());
	}
	
	@NiftyEventSubscriber(id="add_variable")
	public void add_variable(String id,ButtonClickedEvent event){
		handler.make_variable("---");
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="rename_variable")
	public void rename_variable(String id,ButtonClickedEvent event){
		hud.rename_window.show(selected_variable,selected_variable.name());
	}
	
	@NiftyEventSubscriber(id="close_variable_window")
	public void close_variable_window(String id,ButtonClickedEvent event){
		hide();
	}
	
}
