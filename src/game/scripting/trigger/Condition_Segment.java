package game.scripting.trigger;

import java.io.Serializable;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.radiobutton.*;
import de.lessvoid.nifty.controls.radiobutton.builder.*;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
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
import game.save.journal.Arc;
import game.save.journal.Arc_Segment;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Condition_Segment extends Condition implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String parent_identifier;
	private int id;
	private boolean invert;
	private String type;
	
	public Condition_Segment(Arc_Segment s,boolean i,String t){
		set_segment(s);
		invert=i;
		type=t;
	}
	
	//return/set values
	public Arc parent_arc(){
		if(parent_identifier==null){
			return null;
		}
		return Process_Main.quest.journal.get_arc(parent_identifier);
	}
	
	public Arc_Segment segment(){
		if(parent_identifier==null){
			return null;
		}
		return parent_arc().get_segment(id);
	}
	
	public void set_segment(Arc_Segment s){
		if(s==null){
			parent_identifier=null;
		}else{
			parent_identifier=s.parent_identifier();
			id=s.id();
		}
	}
	
	public boolean is_inverted(){
		return invert;
	}
	
	public void set_invert(boolean i){
		invert=i;
	}
	
	public String type(){
		return type;
	}
	
	public void set_type(String t){
		type=t;
	}
	
	//check conditions
	@Override
	public boolean satisfied(Event parent,Entity_Event e){
		boolean result=false;
		
		try{
			if(type.equals("active")){
				result=segment().active();
			}else if(type.equals("finished")){
				result=segment().finished();
			}else if(type.equals("started")){
				result=segment().started();
			}
		}catch(Exception ex){
			System.out.println("CONDITION ERROR: No Segment Selected");
		}
		
		if(invert){
			return !result;
		}else{
			return result;
		}
	}
	
	//text for printing
	@Override
	public String to_print(){
		String s="";
		try{
			s+="[Quest Segment]"+parent_arc().name();
			s+=parent_arc().name()+"-"+segment().name();
		}catch(Exception ex){
			s+="[no segment selected]";
		}
		s+=" is";
		if(invert){
			s+=" not";
		}
		s+=" "+type+"?";
		return s;
	}
	
	//edit display
	@Override
	public void display(Nifty nifty,Element panel){
		Element back=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Default.fnt");
				text("Quest Segment Condition");
			}});
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Default.fnt");
				if(segment()==null){
					text("Selected Segment: "+"[No Segment Selected]");
				}else{
					text("Selected Segment: "+segment().name());
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
		new CheckboxBuilder("Segment Condition Invert Select"){{
			
			checked(invert);
		}}.build(nifty,nifty.getScreen("start"),invertselect);
		
		//type panel
		Element typeselect=new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("50%");
			
			
			text(new TextBuilder(){{
				width("30%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Condition Type: ");
			}});
			
			control(new CheckboxBuilder("segment type started"){{
				checked(type=="started");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Started");
			}});
			control(new CheckboxBuilder("segment type active"){{
				checked(type=="active");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Active");
			}});
			control(new CheckboxBuilder("segment type finished"){{
				checked(type=="finished");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Finished");
			}});
			
		}}.build(nifty,nifty.getScreen("start"),otheroptions);
		
		
	}
	
	//update edit display
	@Override
	public void update_edit_display(){
		//set selected segment
		if(panel_arc_select().selected_segment!=segment()){
			set_segment(panel_arc_select().selected_segment);
			hud().refresh();
		}
		//set invert
		CheckBox c=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("Segment Condition Invert Select",CheckBox.class);
		if(invert!=c.isChecked()){
			invert=c.isChecked();
			hud().refresh();
		}
		//set type
		//get checkboxes
		CheckBox cs=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("segment type started",CheckBox.class);
		CheckBox ca=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("segment type active",CheckBox.class);
		CheckBox cf=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("segment type finished",CheckBox.class);
		if(cs.isChecked()!=("started".equals(type))){
			type="started";
			hud().refresh();
		}else if(ca.isChecked()!=("active".equals(type))){
			type="active";
			hud().refresh();
		}else if(cf.isChecked()!=("finished".equals(type))){
			type="finished";
			hud().refresh();
		}
	}
	
	//control methods
	
	
	//convenient panel retrieve methods
	private Panel_Arc_Select panel_arc_select(){
		return hud().event_condition_window.panel_arc_select;
	}
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
}
