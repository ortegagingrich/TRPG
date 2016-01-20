package game.gui.animation_test;

import game.gui.HUD;
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
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.textfield.*;
import de.lessvoid.nifty.controls.textfield.builder.*;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.slider.*;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.slider.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.gui.right_menu.*;
import game.map.Map;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Main;

import java.util.ArrayList;

import asset.model.*;

public class HUD_Animation_Test extends HUD {
	
	//sub-windows
	public Window_Spritelist spritelist_window;
	public Window_Direction_Select direction_select_window;
	
	public HUD_Animation_Test(){
		setup();
	}
	
	private void setup(){
		//make sub-windows
		spritelist_window=new Window_Spritelist(this);
		direction_select_window=new Window_Direction_Select(this);
		
		//make actual panel
		make_panel();
	}
	
	//update method
	public void update(){
		//update sub-windows
		spritelist_window.update();
		direction_select_window.update();
		
	}
	
	//refresh/build
	public void refresh(){
		try{
			make_panel();
		}catch(Exception ex){}
	}
	
	private void make_panel(){
		panel=Process_Graphics.$graphics.make_nifty();
		//try screen buffering
		panel.gotoScreen("old");
		//dispose old screen
		dispose_panel();
		
		panel.addScreen("start",new ScreenBuilder("start"){{
			
		}}.build(panel));
		
		//build windows
		spritelist_window.refresh(panel);
		direction_select_window.refresh(panel);
		
		//set screen
		panel.gotoScreen("start");
	}
	
	private void dispose_panel(){
		panel.removeScreen("start");
	}
	
}
