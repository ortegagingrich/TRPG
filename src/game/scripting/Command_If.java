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
import game.gui.edit.HUD_Edit;
import game.scene.Scene_WorldEdit;
import game.scripting.trigger.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Command_If extends Command implements Serializable {
	public static final long serialVersionUID=1L;
	
	private Command_Block if_block;
	private Command_Block else_block;
	private ArrayList<Condition> conditions;
	
	public Command_If(int d){
		super(d);
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		//set default values
		if_block=new Command_Block(d+1);
		else_block=new Command_Block(d+1);
		conditions=new ArrayList<Condition>();
	}
	
	public ArrayList<Condition> conditions(){
		return conditions;
	}
	
	//print command
	@Override
	public void print(ListBox l,ArrayList<Command> c,ArrayList<Command_Block> b,Command selected,Command_Block parent){
		String s="";
		if(this.equals(selected)){
			s+="***";
		}
		for(int i=0;i<depth;i++){
			s+=" ";
		}
		s+="<"+Integer.toString(c.size())+">";
		//header
		s+="IF: ";
		if(conditions.size()==0){
			s+="[ALWAYS]";
		}
		for(Condition cond:conditions){
			s+=cond.to_print()+",";
		}
		l.addItem(s);
		c.add(this);
		//if block
		if_block.print(l,c,b,selected,parent);
		//else header
		String s2="";
		for(int i=0;i<depth;i++){
			s2+=" ";
		}
		s2+="<"+Integer.toString(c.size())+">";
		l.addItem(s2+"ELSE:");
		c.add(this);
		b.add(parent);
		//else block
		else_block.print(l,c,b,selected,parent);
		//footer
		String s3="";
		for(int i=0;i<depth;i++){
			s3+=" ";
		}
		s3+="<"+Integer.toString(c.size())+">";
		l.addItem(s3+"ENDIF");
		b.add(parent);
		c.add(this);
	}
	
	//execution methods
	@Override
	public void execute(Event_Process p){
		for(Condition c:conditions){
			if(!c.satisfied(p.get_event(),null)){
				else_block.execute(p);
			}
		}
		
		if_block.execute(p);
		
	}
	
	@Override
	public void pause(){
		super.pause();
		//pause sub-blocks
		if_block.pause();
		else_block.pause();
	}
	
	@Override
	public void resume(){
		super.resume();
		//resume sub-blocks
		if_block.resume();
		else_block.resume();
	}
	
	//command-specific methods
	
	
	
	//edit display methods
	
	@Override
	public void display(Nifty nifty,Element panel){
		Element base=new PanelBuilder(){{
			childLayoutVertical();
			alignCenter();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Command: If Branch");
			}});
			
			//
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		//make a list of current conditions
		new ListBoxBuilder("if condition list"){{
			width("100%");
			height("30%");
			displayItems(10);
			optionalHorizontalScrollbar();
			optionalVerticalScrollbar();
		}}.build(nifty,nifty.getScreen("start"),base);
		//populate listbox
		ListBox condlist=nifty.getScreen("start").findNiftyControl("if condition list",ListBox.class);
		for(Condition c:conditions){
			condlist.addItem(c.to_print());
		}
		
		//button to edit conditions
		new ButtonBuilder("edit_if_conditions","Change Conditions"){{
			width("100%");
			height("5%");
		}}.build(nifty,nifty.getScreen("start"),base);
	}
	
	@Override
	public void update_edit_display(){
		
	}
	
	//button methods:
	
	private static Command last_command=null;  //last command used for button evaluation
	
	@NiftyEventSubscriber(id="edit_if_conditions")
	public void edit_if_conditions(String id,ButtonClickedEvent event){
		//necessary to circumvent oddities of java/nifty-gui;
		Command test=((Scene_WorldEdit)Process_Main.$scene).hud.event_window.panel_script_display.selected_command;
		if(test.equals(last_command)){
			return;
		}
		last_command=test;
		
		//first get current command
		Command_If c=((Command_If)hud().event_window.panel_script_display.selected_command);
		//open event condition window for this command's conditions
		hud().event_condition_window.show(c.conditions,"if branch");
	}
	
	//convenient panel retrieve methods
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
}
