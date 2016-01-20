package game.gui.edit;

import game.*;
import game.gui.HUD;
import game.scripting.Command;
import game.scripting.Command_Block;
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
import de.lessvoid.nifty.controls.ListBox;
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


//panel that displays the main script listbox
public class Panel_Script_Display {
	
	private HUD_Edit hud;
	
	public Command selected_command;
	public Command_Block selected_command_block; //parent block associated with selected command
	public ArrayList<Command> commands;  //arraylist of commands of the event in the order that they appear in the listbox
	public ArrayList<Command_Block> blocks;  //arraylist of parent blocks associated with each command; 1-1 correspondance with commands
	
	private Event event;
	
	public Panel_Script_Display(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		commands=new ArrayList<Command>();
		blocks=new ArrayList<Command_Block>();
	}
	
	//update
	public void update(){
		//if no event selected, return
		if(event==null){
			return;
		}
		//System.out.println(selected_command);
		//handle command selection
		try{
			//check to see if current command matches the last one
			Command com=null;
			Command_Block blo=null;
			ListBox l=hud.panel.getScreen("start").findNiftyControl("Command Console",ListBox.class);
			if(l.getSelectedIndices().size()>0){
				com=commands.get((Integer)l.getSelectedIndices().get(0));
				blo=blocks.get((Integer)l.getSelectedIndices().get(0));
			}
			if(com!=null&&selected_command!=com){
				selected_command=com;
				selected_command_block=blo;
				//de-select necessary panels
				hud.event_window.panel_command_edit.arc_select_panel.deselect();
				hud.event_window.panel_command_edit.map_select_panel.deselect();
				//refresh the hud
				hud.refresh();
			}
		}catch(Exception ex){
		}
		//insert command selection handling
	}
	
	//refresh/build method
	public void refresh(Nifty nifty,Element panel,Event ev){
		event=ev;
		//if no event selected, return (show nothing)
		if(event==null){
			return;
		}
		
		new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			
			control(new ListBoxBuilder("Command Console"){{
				width("100%");
				height("90%");
				displayItems(19);
				optionalHorizontalScrollbar();
			}});
			
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("5%");
				
				control(new ButtonBuilder("insert_command","Insert"){{
					width("75%");
					height("100%");
				}});
				
				control(new ButtonBuilder("delete_command","Delete"){{
					width("25%");
					height("100%");
				}});
			}});
			
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		//print the event commands to the listbox
		ListBox l=nifty.getScreen("start").findNiftyControl("Command Console",ListBox.class);
		commands.clear();
		blocks.clear();
		ev.script.print(l,commands,blocks,selected_command,null);
	}
	
	//button methods
	@NiftyEventSubscriber(id="insert_command")
	public void insert(String id,ButtonClickedEvent event){
		//get the index of the selected event
		ListBox l=hud.panel.getScreen("start").findNiftyControl("Command Console",ListBox.class);
		//check to make sure an event is selected
		if(l.getSelectedIndices().size()==0){
			//if nothing selected, do nothing
			return;
		}
		//add a new event at the appropriate spot
		selected_command_block.add_command(selected_command_block.get_commands().indexOf(selected_command));
		//refresh the hud
		hud.refresh();
	}
	
	@NiftyEventSubscriber(id="delete_command")
	public void delete(String id,ButtonClickedEvent event){
		//if an event is selected (not left over from previous selection)
		ListBox l=hud.panel.getScreen("start").findNiftyControl("Command Console",ListBox.class);
		if(l.getSelectedIndices().size()==0){
			//do nothing
			return;
		}
		//remove the selected event
		selected_command_block.remove_command(selected_command);
		//refresh the hud
		hud.refresh();
	}
}
