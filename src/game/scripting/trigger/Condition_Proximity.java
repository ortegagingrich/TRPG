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
import game.map.entity.Entity_Event;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

public class Condition_Proximity extends Condition implements Serializable {
	public static final long serialVersionUID=1L;
	
	private float distance=0.5f;
	
	public Condition_Proximity(float d){
		super();
		distance=d;
	}
	
	//return/modify attributes
	public float distance(){
		return distance;
	}
	
	public void set_distance(int d){
		distance=d;
	}
	
	//condition met method
	@Override
	public boolean satisfied(Event parent,Entity_Event e){
		//check the player's taxicab distance from the event
		if(e.taxi_distance_from_player()<=distance){
			return true;
		}else{
			return false;
		}
	}
	
	//text to print
	@Override
	public String to_print(){
		String s="";
		s+="[Proximity]";
		s+="Player";
		s+=" distance < ";
		s+=Float.toString(distance);
		s+="?";
		return s;
	}
	
	//edit display
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
				text("Proximity Condition");
			}});
			
			text(new TextBuilder(){{
				width("50%");
				height("5%");
				font("Interface/Fonts/Console.fnt");
				text("Player Proximity");
			}});
		}}.build(nifty,nifty.getScreen("start"),panel);
		
		new PanelBuilder(){{
			childLayoutHorizontal();
			width("100%");
			height("10%");
			
			text(new TextBuilder(){{
				width("25%");
				height("100%");
				font("Interface/Fonts/Default.fnt");
				text("Maximum Distance:");
			}});
			
			control(new TextFieldBuilder("Proximity Condition Field"){{
				maxLength(10);
				width("25%");
			}});
		}}.build(nifty,nifty.getScreen("start"),back);
	}
	
	//edit display update
	@Override
	public void update_edit_display(){
		//try to parse the proximity field
		try{
			TextField f=Process_Graphics.$graphics.make_nifty().getScreen("start").findNiftyControl("Proximity Condition Field",TextField.class);
			String s=f.getText();
			float d=Float.parseFloat(s);
			if(distance!=d){
				distance=d;
				hud().refresh();
			}
		}catch(Exception ex){}
	}
	
	//controls
	
	//convenient panel retrieve methods
	private HUD_Edit hud(){
		return ((Scene_WorldEdit)Process_Main.$scene).hud;
	}
}
