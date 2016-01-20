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

public class Command_Block extends Command implements Serializable {
	public static final long serialVersionUID=1L;
	
	private ArrayList<Command> commands;
	private int current_index;
	
	private boolean paused;
	private boolean finished;
	
	public Command_Block(int d){
		super(d);
		commands=new ArrayList<Command>();
		finished=false;
	}
	
	//printing method:
	@Override
	public void print(ListBox l,ArrayList<Command> co,ArrayList<Command_Block> b,Command selected,Command_Block parent){
		//first check to make sure the last command in the list is an empty command
		if(commands.size()>0){
			if(commands.get(commands.size()-1).getClass()!=Command.class){
				add_command();
			}
		}else{
			add_command();
		}
		//print sub-commands
		for(Command c:commands){
			//print self to block slot
			b.add(this);
			c.print(l,co,b,selected,this);
		}
	}
	
	//execution methods:
	@Override
	public void execute(Event_Process p){
		//initially set paused boolean to false
		paused=false;
		
		//main update loop
		for(int i=0;i<commands.size();i++){
			current_index=i;
			//execute the current command
			current_command().execute(p);
			//check if paused; if so, wait until not paused
			while(paused){
				try{
					Thread.sleep(2);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			//if finished, end evaluation
			if(finished){
				return;
			}
		}
	}
	
	@Override
	public void pause(){
		//also pause current command
		current_command().pause();
		//pause block operation
		paused=true;
	}
	
	@Override
	public void resume(){
		//resume current command
		current_command().resume();
		//resume block evaluation
		paused=false;
	}
	
	@Override
	public void stop(){
		finished=true;
		//stop commands (in case it is also a command block)
		for(Command c:commands){
			c.stop();
		}
	}
	
	//command-specific methods:
	
	//command currently being executed
	private Command current_command(){
		if(commands.size()==0){
			return null;
		}else{
			return commands.get(current_index);
		}
	}
	
	public void add_command(){
		commands.add(new Command(depth));
	}
	
	public void add_command(int pos){
		commands.add(pos,new Command(depth));
	}
	
	public void add_command(Command com){
		commands.add(com);
	}
	
	//removes a specific command
	public void remove_command(Command c){
		commands.remove(c);
	}
	
	public void remove_command(int p){
		commands.remove(p);
	}
	
	//just get list of commands
	public ArrayList<Command> get_commands(){
		return commands;
	}
	
	//finds a command and replaces it with a new command
	public void replace_command(Command oldc,Command newc){
		for(int i=0;i<commands.size();i++){
			if(commands.get(i).equals(oldc)){
				commands.set(i,newc);
				return;
			}
		}
	}
	
	//edit display
	@Override
	public void display(Nifty nifty,Element panel){
		//do nothing
		
	}
	
}
