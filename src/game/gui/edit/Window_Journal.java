package game.gui.edit;

import game.*;
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



public class Window_Journal {
	
	private HUD_Edit hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y; //note: coordinates are only for modular window use; it will be impossible to move windows in runtime
	
	public Panel_Arc_Select panel_arc_select;
	public Panel_Arc_Edit panel_arc_edit;
	
	
	public Window_Journal(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=false;
		screen_x=100;
		screen_y=50; //default values
		
		//make panels
		panel_arc_select=new Panel_Arc_Select(hud);
		panel_arc_edit=new Panel_Arc_Edit(hud);
		
	}
	
	//show/hide methods:
	public void show(){
		visible=true;
		hud.refresh();
	}
	
	public void hide(){
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
		//update dependent sub-panels
		panel_arc_select.update();
		panel_arc_edit.update();
	}
	
	//refresh/build method:
	public void refresh(Nifty panel){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:panel.getScreen("start").getLayerElements()){
			l.disable();
		}
		
		Element layer=new LayerBuilder("journal layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),panel.getScreen("start").getRootElement());
		
		//make overall panel
		Element back=new PanelBuilder("back"){{
			childLayoutHorizontal();
	    	x(Integer.toString(screen_x)+"px");
	    	y(Integer.toString(screen_y)+"px");
	    	width("50%");
	    	height("60%");
	    	style("nifty-panel");
	    	
		}}.build(panel,panel.getScreen("start"),layer);
		
		//arc selection panel
		//make frame
		Element arcpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("40%");
			height("100%");
			style("nifty-panel");
		}}.build(panel,panel.getScreen("start"),back);
		//fill with arc select panel
		panel_arc_select.refresh(panel,arcpanel,true);
	    
		//make arc/segment edit panel
		Element arceditpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("60%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),back);
		
		//fill panel
		panel_arc_edit.refresh(panel,arceditpanel);
		
	}
	
	
	
}
