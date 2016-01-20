package game.gui.edit;

import game.*;
import game.save.journal.Arc;
import game.save.journal.Arc_Segment;
import game.system.Process_Graphics;

import com.jme3.scene.plugins.blender.objects.Properties;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Attributes;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventAnnotationProcessor;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.DefaultController;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.*;
import de.lessvoid.nifty.elements.events.*;
import de.lessvoid.nifty.*;

//panel used for editing arcs/segments (RHS of journal window)
public class Panel_Arc_Edit {
	
	private HUD_Edit hud;
	
	public Panel_Arc_Edit(HUD_Edit h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
	}
	
	
	//for convenience:
	private Arc selected_arc(){
		return hud.journal_window.panel_arc_select.selected_arc;
	}
	
	private Arc_Segment selected_segment(){
		return hud.journal_window.panel_arc_select.selected_segment;
	}
	
	//update
	public void update(){
		
	}
	
	//refresh/build
	public void refresh(Nifty nifty,Element panel){
		Element content=new PanelBuilder(){{
			childLayoutVertical();
			width("100%");
			height("100%");
			
			text(new TextBuilder(){{
				height("15%");
        		font("Interface/Fonts/Default.fnt");
        		if(selected_arc()!=null){
        			if(selected_segment()!=null){
        				text(selected_arc().name()+": "+selected_segment().name());
        			}else{
        				text(selected_arc().name());
        			}
        		}
			}});
			
			//variable window buttons
			panel(new PanelBuilder(){{
				childLayoutHorizontal();
				width("100%");
				height("10%");
				if(selected_arc()!=null){
					control(new ButtonBuilder("edit_arc_variables",selected_arc().name()+": VARIABLES"){{
						height("100%");
						width("50%");
					}});
				}
				if(selected_segment()!=null){
					control(new ButtonBuilder("edit_segment_variables",selected_segment().name()+": VARIABLES"){{
						height("100%");
						width("50%");
					}});
				}
			}});
			
			
		}}.build(nifty,nifty.getScreen("start"),panel);
	}
	
	//button methods:
	
	//open variable windows
	@NiftyEventSubscriber(id="edit_arc_variables")
	public void edit_arc_variables(String id,ButtonClickedEvent event){
		hud.variable_window.show(selected_arc().variable_handler);
	}
	
	@NiftyEventSubscriber(id="edit_segment_variables")
	public void edit_segment_variables(String id,ButtonClickedEvent event){
		hud.variable_window.show(selected_segment().variable_handler);
	}
}
