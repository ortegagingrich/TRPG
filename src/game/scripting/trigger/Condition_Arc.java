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
import de.lessvoid.nifty.controls.RadioButton;
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
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Condition_Arc extends Condition implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String identifier;
	private boolean invert;
	private String type;
	
	public Condition_Arc(Arc a,boolean i,String t){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		set_arc(a);
		invert=i;
		type=t;
	}
	
	//return/set values
	public Arc arc(){
		if(identifier==null){
			return null;
		}
		return Process_Main.quest.journal.get_arc(identifier);
	}
	
	public void set_arc(Arc a){
		if(a==null){
			identifier=null;
		}else{
			identifier=a.identifier();
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
	
	//check if satisfied
	@Override
	public boolean satisfied(Event parent,Entity_Event e){
		boolean result=false;
		
		try{
			if(type.equals("active")){
				result=arc().active();
			}else if(type.equals("started")){
				result=arc().started();
			}else if(type.equals("finished")){
				result=arc().finished();
			}
		}catch(Exception ex){
			System.out.println("CONDITION ERROR: No Arc Selected");
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
		s+="[Quest Arc]";
		try{
			s+=arc().name();
		}catch(Exception ex){
			s+="[no arc selected]";
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
				text("Quest Arc Condition");
			}});
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Default.fnt");
				if(arc()==null){
					text("Selected Arc: "+"[No Arc Selected]");
				}else{
					text("Selected Arc: "+arc().name());
				}
			}});
			
			
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		
		Element arcselectpanel=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("70%");
			style("nifty-panel");
		}}.build(nifty,nifty.getScreen("start"),back);
		//fill panel
		panel_arc_select().refresh(nifty,arcselectpanel,false);
		
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
		new CheckboxBuilder("Arc Condition Invert Select"){{
			
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
			
			control(new CheckboxBuilder("arc type started"){{
				checked(type=="started");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Started");
			}});
			control(new CheckboxBuilder("arc type active"){{
				checked(type=="active");
			}});
			text(new TextBuilder(){{
				width("15%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Active");
			}});
			control(new CheckboxBuilder("arc type finished"){{
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
		//set selected arc as the arc selected in panel, if different
		if(panel_arc_select().selected_arc!=arc()){
			set_arc(panel_arc_select().selected_arc);
			hud().refresh();
		}
		//set invert
		CheckBox c=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("Arc Condition Invert Select",CheckBox.class);
		if(invert!=c.isChecked()){
			invert=c.isChecked();
			hud().refresh();
		}
		//set type
		//get checkboxes
		CheckBox cs=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("arc type started",CheckBox.class);
		CheckBox ca=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("arc type active",CheckBox.class);
		CheckBox cf=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("arc type finished",CheckBox.class);
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
