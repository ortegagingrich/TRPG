package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.map.entity.Entity_Event;
import game.save.journal.Arc;
import game.save.journal.Arc_Segment;
import game.scripting.Event;
import game.scripting.Switch;
import game.scripting.Variable;
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


public class Window_Rename {
	
	private HUD hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	public Object target; //target of rename procedure
	private String oldname; //old name of the target;
	
	public Window_Rename(HUD h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=200;
		screen_y=100;
	}
	
	//show/hide methods
	
	public void show(Object newtarget,String o){
		//order is important for multithreading; target must be set before visibility
		target=newtarget;
		visible=true;
		if(o==null){
			oldname="";
		}else{
			oldname=o;
		}
		hud.refresh();
	}
	
	public void hide(){
		visible=false;
		target=null;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update
	public void update(){
		if(!visible||target==null||current_name()==null){
			return;
		}
		//handle rename process
		//branch based on target type
		if(target instanceof Arc){
			((Arc)target).rename(current_name());
		}else if(target instanceof Arc_Segment){
			((Arc_Segment)target).rename(current_name());
		}else if(target instanceof Variable){
			((Variable)target).rename(current_name());
		}else if(target instanceof Switch){
			((Switch)target).rename(current_name());
		}else if(target instanceof Event){
			((Event)target).name=current_name();
		}else if(target instanceof Entity_Event){
			((Entity_Event)target).rename(current_name());
		}
	}
	
	//refresh/build method
	public void refresh(Nifty panel){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:panel.getScreen("start").getLayerElements()){
			l.disable();
			//l.hide();
		}
		
		Element layer=new LayerBuilder("rename layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),panel.getScreen("start").getRootElement());
		
		//make overall panel
		new PanelBuilder("back"){{
			childLayoutVertical();
	    	x(Integer.toString(screen_x)+"px");
	    	y(Integer.toString(screen_y)+"px");
	    	width("15%");
	    	height("11%");
	    	style("nifty-panel");
	    	text(new TextBuilder(){{
	    		height("30%");
        		font("Interface/Fonts/Default.fnt");
                text("Rename: "+oldname);
	    	}});
	    	//text entry
	    	control(new TextFieldBuilder("Rename Window Field",oldname){{
    			maxLength(30);
    			width("80%");
    		}});
	    	//button to close
	    	control(new ButtonBuilder("close_rename_window","Confirm"){{
	    		width("40%");
	    		height("30%");
	    	}});
		}}.build(panel,panel.getScreen("start"),layer);
	}
	
	//return methods
	private String current_name(){
		if(!visible){
			return null;
		}
		//otherwise, get contents of textfield
		if(hud.panel.getScreen("start")==null){
			return null;
		}
		if(hud.panel.getScreen("start").findNiftyControl("Rename Window Field",TextField.class)==null){
			return null;
		}
		try{
			return hud.panel.getScreen("start").findNiftyControl("Rename Window Field",TextField.class).getText();
		}catch(Exception ex){
			return null;
		}
	}
	
	//button interact methods
	@NiftyEventSubscriber(id="close_rename_window")
	public void close_rename_window(String id,ButtonClickedEvent event){
		hide();
	}
	
}
