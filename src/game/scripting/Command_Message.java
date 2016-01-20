package game.scripting;

import java.io.Serializable;
import java.util.ArrayList;
import de.lessvoid.nifty.controls.*;
import game.*;
import game.gui.*;
import game.gui.edit.HUD_Edit;
import game.gui.in_game.Window_Message;
import game.scene.Scene_Explore;
import game.scene.Scene_WorldEdit;
import game.scripting.message.*;
import game.system.Process_Graphics;
import game.system.Process_Main;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.*;
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

public class Command_Message extends Command implements Serializable {
	public static final long serialVersionUID=1L;
	
	private Message message;
	
	public Command_Message(int d){
		super(d);
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		//make message
		message=new Message();
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
		s+="MESSAGE:'";
		s+=message.preview();
		s+="...'";
		l.addItem(s);
		c.add(this);
	}
	
	//execution methods:
	@Override
	public void execute(Event_Process p){
		//first put a hold on actions
		Process_Main.quest.interpreter.hold_action(this);
		//get message window
		Window_Message w=((Scene_Explore)Process_Main.$scene).hud.main_screen().message_window;
		//next, set message window to current text 
		w.show_message(message);
		//wait until the message is dismissed
		w.wait_until_dismissed();
		//finally, remove action hold
		Process_Main.quest.interpreter.release_action(this);
	}
	
	@Override
	public void pause(){
		super.pause();
	}
	
	@Override
	public void resume(){
		super.resume();
	}
	
	//command-specific methods
	
	//get raw text
	public String get_text(){
		if(message.text()==null){
			return "";
		}else{
			return message.text();
		}
	}
	
	//set text
	public void set_text(String s){
		message.parse_string(s);
	}
	
	
	//edit display method
	@Override
	public void display(Nifty nifty,Element panel){
		//preliminary test
		Element base=new PanelBuilder(){{
			childLayoutVertical();
			alignCenter();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Command: Message");
			}});
			
			//simple work-around; make text field for message
			
			
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		Element field=new TextFieldBuilder("message command text"){{
			height("20%");
			width("100%");
			maxLength(2000);
			text(get_text());
		}}.build(nifty,nifty.getScreen("start"),base);
		
		//text to show how many characters are in the current message
		new ButtonBuilder("",Integer.toString(get_text().length())+" characters (at last refresh)"){{
			width("100%");
			height("3%");
			font("Interface/Fonts/Console.fnt");
		}}.build(nifty,nifty.getScreen("start"),base);
		
	}
	
	//update edit display method
	@Override
	public void update_edit_display(){
		TextField f=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("message command text",TextField.class);
		
		try{if(!get_text().equals(f.getText())&&!f.hasFocus()){
			set_text(f.getText());
			hud().refresh();
		}}catch(Exception ex){}
	}
	
	
	//for convenience
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
	
}
