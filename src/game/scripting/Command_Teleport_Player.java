package game.scripting;

import java.io.Serializable;
import java.util.ArrayList;
import de.lessvoid.nifty.controls.*;
import game.*;
import game.gui.*;
import game.gui.edit.HUD_Edit;
import game.gui.edit.Panel_Map_Select;
import game.map.Map;
import game.map.entity.Entity_Player;
import game.save.Quest;
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

public class Command_Teleport_Player extends Command implements Serializable {
	public static final long serialVersionUID=1L;
	
	private int m; //map id for target
	private int x;
	private int y;
	private int z;
	private String facing;  //u,d,l,r force facing; anything else preserves current facing
	
	//temporary storage fields for elements (so they do not have to be searched for every frame)
	private static CheckBox cu;
	private static CheckBox cd;
	private static CheckBox cl;
	private static CheckBox cr;
	private static CheckBox co;
	
	public Command_Teleport_Player(int d){
		super(d);
		//set default values
		m=Process_Main.quest.universe.startm;
		x=Process_Main.quest.universe.startx;
		y=Process_Main.quest.universe.starty;
		z=Process_Main.quest.universe.startz;
		facing="";
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
		s+="TELEPORT PLAYER: Map ";
		s+="["+Integer.toString(m)+":"+map().name+"] ";
		s+="Coordinates: ";
		s+="x="+Integer.toString(x);
		s+=",y="+Integer.toString(y);
		s+=",z="+Integer.toString(z);
		s+=" Facing:"+facing;
		l.addItem(s);
		c.add(this);
	}
	
	//execution method
	@Override
	public void execute(Event_Process p){
		Quest q=Process_Main.quest;
		q.x=x;
		q.y=y;
		q.z=z;
		q.m=m;
		Map current_map=((Scene_Explore)Process_Main.$scene).world.map;
		
		//first wait for the player to stop moving
		while(current_map.player().moving()){
			
		}
		
		//get old facing information
		String oldface=current_map.player().face();
		
		//if not switching maps, more straightforward; just refresh player entity
		if(current_map.id()==m){
			current_map.make_player();
		}else{
			//first stop all event processes except for the current one
			Process_Main.quest.interpreter.stop_except(p);
			
			//transition to new map
			((Scene_Explore)Process_Main.$scene).transition_to_map(q.universe.get_map(m));
		}
		
		
		//set player facing
		Entity_Player e=current_map.player();
		if(facing.equals("u")){
			e.face_up();
		}else if(facing.equals("d")){
			e.face_down();
		}else if(facing.equals("l")){
			e.face_left();
		}else if(facing.equals("r")){
			e.face_right();
		}else{
			e.face_set(oldface);
		}
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
	public Map map(){
		return Process_Main.quest.universe.get_map(m);
	}
	
	public int map_id(){
		return m;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y();
	}
	
	public int z(){
		return z;
	}
	
	public void set_coordinates(int mi,int xi,int yi,int zi){
		x=xi;
		y=yi;
		z=zi;
		m=mi;
	}
	
	
	//edit display methods
	
	public void display(Nifty nifty,Element panel){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		
		Element base=new PanelBuilder(){{
			childLayoutVertical();
			alignCenter();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Command: Teleport Player");
			}});
			
			text(new TextBuilder(){{
				width("50%");
				height("10%");
				font("Interface/Fonts/Default.fnt");
				text("Selected Map: ["+Integer.toString(m)+":"+map().name+"] ");
			}});
			
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		//map select panel
		Element mapselect=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("50%");
		}}.build(nifty,nifty.getScreen("start"),base);
		panel_map_select().refresh(nifty,mapselect);
		
		//coordinates
		new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("10%");
			
			text(new TextBuilder(){{
				width("50%");
				height("30%");
				font("Interface/Fonts/Default.fnt");
				String s="Selected Coordinates: ";
				s+="x="+Integer.toString(x);
				s+=",y="+Integer.toString(y);
				s+=",z="+Integer.toString(z);
				text(s);
			}});
			
			control(new ButtonBuilder("select_teleport_coordinates","Change Coordinates"){{
				width("25%");
				height("30%");
			}});
		}}.build(nifty,nifty.getScreen("start"),base);
		
		//facing select
		
		new TextBuilder(){{
			width("50%");
			height("3%");
			font("Interface/Fonts/Default.fnt");
			text("Facing");
		}}.build(nifty,nifty.getScreen("start"),base);
		
		new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("5%");
			
			control(new CheckboxBuilder("teleport face up"){{
				checked(facing=="u");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Up");
			}});
			control(new CheckboxBuilder("teleport face down"){{
				checked(facing=="d");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Down");
			}});
			control(new CheckboxBuilder("teleport face left"){{
				checked(facing=="l");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Left");
			}});
			control(new CheckboxBuilder("teleport face right"){{
				checked(facing=="r");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Right");
			}});
			control(new CheckboxBuilder("teleport face old"){{
				checked(facing=="");
			}});
		}}.build(nifty,nifty.getScreen("start"),base);
		
		new TextBuilder(){{
			width("15%");
			height("100%");
			font("Interface/Fonts/Default.fnt");
			text("Original");
		}}.build(nifty,nifty.getScreen("start"),base);
		
		//save elements for convenience
		cu=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("teleport face up",CheckBox.class);
		cd=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("teleport face down",CheckBox.class);
		cl=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("teleport face left",CheckBox.class);
		cr=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("teleport face right",CheckBox.class);
		co=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("teleport face old",CheckBox.class);
	}
	
	//edit update
	@Override
	public void update_edit_display(){
		
		//if coordinate select window is open, do not update
		if(hud().coordinate_select_window.visible()){
			return;
		}
		
		//check selected map
		if(panel_map_select().selected_map_id!=m&&panel_map_select().selected_map_id!=-1){
			m=panel_map_select().selected_map_id;
			hud().refresh();
		}
		
		//set facing
		if(cu.isChecked()!="u".equals(facing)){
			facing="u";
			hud().refresh();
		}else if(cd.isChecked()!="d".equals(facing)){
			facing="d";
			hud().refresh();
		}else if(cl.isChecked()!="l".equals(facing)){
			facing="l";
			hud().refresh();
		}else if(cr.isChecked()!="r".equals(facing)){
			facing="r";
			hud().refresh();
		}else if(co.isChecked()!="".equals(facing)){
			facing="";
			hud().refresh();
		}
		
		
	}
	
	//button methods:
	
	private static Command last_command=null;  //last command used for button evaluation
	
	@NiftyEventSubscriber(id="select_teleport_coordinates")
	public void select_teleport_coordinates(String id,ButtonClickedEvent event){
		//first get current command
		Command_Teleport_Player c=(Command_Teleport_Player)hud().event_window.panel_script_display.selected_command;
		if(c==last_command){
			return;
		}
		last_command=c;
		try{
			Thread.sleep(60);
		}catch(Exception ex){
			//See what you have pushed me to NIFTY GUI!!!!!
		}
		//set secondary selector to show last coordinates
		((Scene_WorldEdit)Process_Main.$scene).world.secondary_selector.x=c.x;
		((Scene_WorldEdit)Process_Main.$scene).world.secondary_selector.y=c.y;
		((Scene_WorldEdit)Process_Main.$scene).world.secondary_selector.z=c.z;
		//open coordinate select window for this command
		hud().coordinate_select_window.show(c,c.map());
	}
	
	//convenient panel retrieve methods
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
	
	private Panel_Map_Select panel_map_select(){
		return hud().event_window.panel_command_edit.map_select_panel;
	}
	
}
