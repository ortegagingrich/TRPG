package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.map.Map;
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

public class Panel_Map_Select {
	
	private HUD_Edit hud;
	
	public int selected_map_id=-1;
	private ArrayList<Integer> maps;  //arraylist of map ids in the listbox
	
	//saved elements for fast use
	private ListBox list;
	
	public Panel_Map_Select(HUD_Edit h){
		hud=h;
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		maps=new ArrayList<Integer>();
	}
	
	//return selected map
	public Map selected_map(){
		return Process_Main.quest.universe.get_map(selected_map_id);
	}
	
	private long count=0;
	//update method
	public void update(){
		count++;
		//handle map selection every 10 frames
		if(count%10==0){
			try{
				//check to see if selected map matches the previous one
				int id=-1;
				
				if(list.getSelection().size()>0){
					id=maps.get((Integer)list.getSelectedIndices().get(0));
				}
				if(id!=selected_map_id&&id!=-1){
					selected_map_id=id;
					hud.refresh();
				}
			}catch(Exception ex){}
		}
		
	}
	
	public void deselect(){
		selected_map_id=-1;
	}
	
	//refresh/build method
	public void refresh(Nifty nifty,Element panel){
		new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
	    		height("10%");
        		font("Interface/Fonts/Default.fnt");
                if(selected_map_id!=-1){
                	text("Selected Map: "+selected_map().name);
                }else{
                	text("Selected Map: [No Map Selected]");
                }
	    	}});
			
			control(new ListBoxBuilder("map select list"){{
				width("100%");
    			height("90%");
    			displayItems(9);
        		optionalHorizontalScrollbar();
			}});
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		//populate listbox
		list=nifty.getScreen("start").findNiftyControl("map select list",ListBox.class);
		for(Map m:Process_Main.quest.universe.get_maps()){
			maps.add(m.id());
			list.addItem(m.id_string()+" "+m.name);
		}
		
	}
	
	//control/button methods
	
	
}
