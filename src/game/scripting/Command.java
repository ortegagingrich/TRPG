package game.scripting;

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
import game.gui.HUD;
import game.scene.Scene_WorldEdit;
import game.system.Process_Graphics;
import game.system.Process_Main;

//superclass of event scripting commands; also serves as placeholder for commands in scripting window
public class Command implements Serializable {
	public static final long serialVersionUID=1L;
	
	protected int depth;
	
	private boolean paused;
	
	public Command(int d){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		depth=d+1;
		paused=false;
	}
	
	//debug
	public Command self(){
		return this;
	}
	
	//printing method
	public void print(ListBox l,ArrayList<Command> c,ArrayList<Command_Block> b,Command selected,Command_Block parent){
		String s="";
		if(this.equals(selected)){
			s+="***";
		}
		for(int i=0;i<depth;i++){
			s+=" ";
		}
		s+="<"+Integer.toString(c.size())+">";
		l.addItem(s);
		c.add(this);
	}
	
	//execution methods:
	public void execute(Event_Process p){
		
	}
	
	public void pause(){
		paused=true;
	}
	
	public void resume(){
		paused=false;
	}
	
	public void stop(){//note that only command blocks can actually stop mid-evaluation; all other commands must finish first
		
	}
	
	protected void wait_for_resume(){
		while(paused){
			//wait until un-paused
		}
	}
	
	
	//edit display methods
	public void display(Nifty nifty,Element panel){
		//generic test
		new PanelBuilder(){{
			childLayoutVertical();
			alignCenter();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Select Command Type");
			}});
			
			//temporary test button
			control(new ButtonBuilder("make_command_message","Message"){{
				width("15%");
				height("5%");
			}});
			control(new ButtonBuilder("make_command_teleport_player","Teleport Player"){{
				width("15%");
				height("5%");
			}});
			control(new ButtonBuilder("make_command_if","If Branch"){{
				width("15%");
				height("5%");
			}});
			
		}}.build(nifty,nifty.getScreen("start"),panel);
	}
	
	//update edit display method
	public void update_edit_display(){
		
	}
	
	//make command type button methods
	
	private static Command last_command=null;  //last command used for button evaluation
	
	@NiftyEventSubscriber(id="make_command_message")
	public void make_command_message(String id,ButtonClickedEvent event){
		//necessary to circumvent oddities of java/nifty-gui;
		Command test=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command;
		if(test.equals(last_command)){
			return;
		}
		last_command=test;
		
		//first get parent block
		Command_Block block=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block;
		//make new command
		Command_Message c=new Command_Message(block.depth);
		//replace this with the new command
		block.replace_command(test,c);
		//de-select old command
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command=null;
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block=null;
		//refresh
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
	
	@NiftyEventSubscriber(id="make_command_teleport_player")
	public void make_command_teleport_player(String id,ButtonClickedEvent event){
		//necessary to circumvent oddities of java/nifty-gui;
		Command test=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command;
		if(test.equals(last_command)){
			return;
		}
		last_command=test;
		
		//first get parent block
		Command_Block block=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block;
		//make new command
		Command_Teleport_Player c=new Command_Teleport_Player(block.depth);
		//replace this with the new command
		block.replace_command(test,c);
		//de-select old command
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command=null;
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block=null;
		//refresh
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
	
	@NiftyEventSubscriber(id="make_command_if")
	public void make_command_if(String id,ButtonClickedEvent event){
		//necessary to circumvent oddities of java/nifty-gui;
		Command test=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command;
		if(test.equals(last_command)){
			return;
		}
		last_command=test;
		
		//first get parent block
		Command_Block block=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block;
		//make new command
		Command_If c=new Command_If(block.depth);
		//replace this with the new command
		block.replace_command(test,c);
		//de-select old command
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command=null;
		((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command_block=null;
		//refresh
		((Scene_WorldEdit)Process_Main.$scene).hud.refresh();
	}
}
