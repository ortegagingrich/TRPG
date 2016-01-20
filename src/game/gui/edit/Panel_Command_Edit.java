package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.scripting.Command;
import game.system.Process_Graphics;
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


public class Panel_Command_Edit {
	
	private HUD_Edit hud;
	
	private Command command;
	
	//sub panels (Note: when adding panels here, remember to call their de-select methods from Panel_Script_Display when command selection changes)
	public Panel_Map_Select map_select_panel;
	public Panel_Arc_Select arc_select_panel;
	
	public Panel_Command_Edit(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		
		//make panels
		map_select_panel=new Panel_Map_Select(hud);
		arc_select_panel=new Panel_Arc_Select(hud);
	}
	
	//update
	public void update(){
		//if a command is selected, update its display
		if(command!=null){
			command.update_edit_display();
		}
		//update panels
		map_select_panel.update();
		arc_select_panel.update();
	}
	
	//build/refresh method:
	public void refresh(Nifty nifty,Element panel,Command c){
		//if no command selected, do nothing
		if(c==null){
			return;
		}
		
		//set selected command as c
		command=c;
		
		//have the selected command print to the panel
		command.display(nifty,panel);
	}
	
}
