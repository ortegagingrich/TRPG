package game.gui.animation_test;

import game.*;
import game.scene.*;
import game.system.*;
import game.map.entity.Entity_Event;
import game.scripting.trigger.Panel_Condition_Master;
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.checkbox.*;
import de.lessvoid.nifty.controls.checkbox.builder.*;
import de.lessvoid.nifty.controls.listbox.*;
import de.lessvoid.nifty.controls.listbox.builder.*;
import de.lessvoid.nifty.controls.ListBox;
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
import asset.sprite.*;

//WARNING: THIS CLASS IS ONLY TO BE USED FOR ANIMATION TEST; IT IS NOT COMPATIBLE WITH OTHER WINDOW TYPES
public class Window_Spritelist {
	
	private HUD_Animation_Test hud;
	
	private boolean visible;
	private int screen_x;
	private int screen_y;
	
	//list panels here (public)
	
	public Window_Spritelist(HUD_Animation_Test h){
		Process_Graphics.$graphics.make_nifty().subscribeAnnotations(this);
		hud=h;
		visible=true;
		screen_x=0;
		screen_y=0;
		
		//make panels here
		
	}
	
	//show/hide methods
	public void show(){
		visible=true;
		hud.refresh();
	}
	
	public void hide(){
		visible=false;
		hud.refresh();
	}
	
	public void toggle(){
		visible=!visible;
		hud.refresh();
	}
	
	public boolean visible(){
		return visible;
	}
	
	//update method
	public void update(){
		if(!visible){
			return;
		}
		//if visible, update sub-panels here
		
	}
	
	//refresh/build method
	public void refresh(Nifty panel){
		if(!visible){
			return;
		}
		
		//disable other existing layers
		for(Element l:panel.getScreen("start").getLayerElements()){
			l.disable();
		}
		
		//make new layer
		Element layer=new LayerBuilder("sprite list layer"){{
			childLayoutAbsolute();
			width("100%");
			height("100%");
		}}.build(panel,panel.getScreen("start"),panel.getScreen("start").getRootElement());
		
		//make back panel
		Element back=new PanelBuilder("back"){{
			childLayoutVertical();
	    	x(Integer.toString(screen_x)+"px");
	    	y(Integer.toString(screen_y)+"px");
	    	width("15%");
	    	height("100%");
	    	style("nifty-panel");
	    	
	    	panel(new PanelBuilder(){{
	    		childLayoutHorizontal();
	    		width("100%");
	    		height("4%");
	    		text(new TextBuilder(){{
	    			width("50%");
		    		height("100%");
	        		font("Interface/Fonts/Default.fnt");
	                text("Sprites:");
		    	}});
	    		control(new ButtonBuilder("display_sprite","Select"){{
	    			width("50%");
	    			height("80%");
	    		}});
	    	}});
	    	
		}}.build(panel,panel.getScreen("start"),layer);
		
		//make listbox
		new ListBoxBuilder("list of sprites"){{
			width("100%");
			height("95%");
			displayItems(35);
			optionalHorizontalScrollbar();
		}}.build(panel,panel.getScreen("start"),back);
		
		//populate listbox
		ListBox l=panel.getScreen("start").findNiftyControl("list of sprites",ListBox.class);
		for(Sprite s:Process_Graphics.spriteset.sprites){
			l.addItem(s.name());
		}
		
	}
	
	//return methods
	private Sprite selected_sprite(){
		try{
			ListBox l=hud.panel.getScreen("start").findNiftyControl("list of sprites",ListBox.class);
			if(l.getSelectedIndices().size()>0){
				return Process_Graphics.spriteset.sprites.get((Integer)l.getSelectedIndices().get(0));
			}
		}catch(Exception ex){}
		return null;
	}
	
	//button interact methods
	
	@NiftyEventSubscriber(id="display_sprite")
	public void display_sprite(String id,ButtonClickedEvent event){
		//set selected sprite as animation test sprite
		((Scene_AnimationTest)Process_Main.$scene).animator.set_sprite(selected_sprite());
	}
}
