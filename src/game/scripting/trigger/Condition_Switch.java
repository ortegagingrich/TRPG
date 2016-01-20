package game.scripting.trigger;

import java.io.Serializable;
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
import game.*;
import game.gui.edit.HUD_Edit;
import game.gui.edit.Panel_Arc_Select;
import game.map.entity.Entity_Event;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Condition_Switch extends Condition implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String[] address; //address of the appropriate switch
	private boolean invert; //indicates that the condition is satisfied if the switch is false
	
	public Condition_Switch(Switch s,boolean i){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		set_switch(s);
		invert=i;
	}
	
	//return/set attributes
	public Switch get_switch(){
		if(address==null){
			return null;
		}
		return Process_Main.quest.journal.find_switch(address);
	}
	
	public void set_switch(Switch s){
		if(s!=null){
			address=s.address();
		}else{
			address=null;
		}
	}
	
	public boolean is_inverted(){
		return invert;
	}
	
	public void set_invert(boolean i){
		invert=i;
	}
	
	//check if satisfied
	@Override
	public boolean satisfied(Event parent,Entity_Event e){
		try{
			//retrieve switch
			Switch s=get_switch();
			//return appropriate value
			if(invert){
				return !s.value();
			}else{
				return s.value();
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	//text for printing
	@Override
	public String to_print(){
		String s="";
		s+="[Switch]";
		try{
			s+=get_switch().name();
		}catch(Exception ex){
			s+="[no switch selected]";
		}
		s+=" is ";
		s+=Boolean.toString(!invert);
		s+="?";
		return s;
	}
	
	//edit display method
	@Override
	public void display(Nifty nifty,Element panel){
		Element back=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			alignCenter();
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Default.fnt");
				text("Switch Condition");
			}});
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Console.fnt");
				if(get_switch()==null){
					text("Selected Switch: "+"[No Switch Selected]");
				}else{
					text("Selected Switch: "+get_switch().name());
				}
			}});
			
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		Element segmentselectpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("70%");
			style("nifty-panel");
		}}.build(nifty,nifty.getScreen("start"),back);
		//fill panel
		panel_arc_select().refresh(nifty,segmentselectpanel,true);
		
		//panel for other options
		Element otheroptions=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("20%");
			style("nifty-panel");
		}}.build(nifty,nifty.getScreen("start"),back);
		
		//choose a Switch
		
		new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("50%");
			valignCenter();
			
			text(new TextBuilder(){{
				width("50%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Select a Switch:");
			}});
			
			control(new ButtonBuilder("select condition switch arc","Arc Switch"){{
				width("25%");
				height("100%");
			}});
			control(new ButtonBuilder("select condition switch segment","Segment Switch"){{
				width("25%");
				height("100%");
			}});
		}}.build(nifty,nifty.getScreen("start"),otheroptions);
		
		
		//invert select panel
		Element invertselect=new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("50%");
			
			text(new TextBuilder(){{
				width("30%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Invert?");
			}});
		}}.build(nifty,nifty.getScreen("start"),otheroptions);
		new CheckboxBuilder("Switch Condition Invert Select"){{
			
			checked(invert);
		}}.build(nifty,nifty.getScreen("start"),invertselect);
		
	}
	
	//update edit display
	@Override
	public void update_edit_display(){
		//if switch select window is visible, set selected switch
		if(hud().variable_window.visible()){
			Switch s=hud().variable_window.selected_switch;
			if(get_switch()!=s&&s!=null){
				set_switch(s);
				hud().refresh();
			}
		}
		//set invert
		CheckBox c=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("Switch Condition Invert Select",CheckBox.class);
		if(invert!=c.isChecked()){
			invert=c.isChecked();
			hud().refresh();
		}
	}
	
	//control methods
	
	@NiftyEventSubscriber(id="select condition switch arc")
	public void select_condition_switch_arc(String id,ButtonClickedEvent event){
		//try to open variable window
		try{
			Variable_Handler h=panel_arc_select().selected_arc.variable_handler;
			hud().variable_window.show(h);
		}catch(Exception ex){}
	}
	
	@NiftyEventSubscriber(id="select condition switch segment")
	public void select_condition_switch_segment(String id,ButtonClickedEvent event){
		//try to open variable window
		try{
			Variable_Handler h=panel_arc_select().selected_segment.variable_handler;
			hud().variable_window.show(h);
		}catch(Exception ex){}
	}
	
	//convenient panel retrieve methods
	private Panel_Arc_Select panel_arc_select(){
		return hud().event_condition_window.panel_arc_select;
	}
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
}
