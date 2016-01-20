package game.gui.in_game;

import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.battle.Battle_Manager;
import game.system.Process_Graphics;

import java.awt.Color;
import java.util.ArrayList;

//NOTE: THIS CLASS WILL BE REPLACED WITH AN IMPROVED VERSION; THIS IS CURRENTLY A TEMPORARY VERSION FOR DEBUG
public class HUD_Initiative {
	
	public Nifty panel;
	
	private Battle_Manager manager;
	private boolean built;
	
	private ArrayList<String> triggered;  //arraylist of buttons triggered
	private ArrayList<String> newtrigger;
	
	public HUD_Initiative(Battle_Manager m){
		manager=m;
		built=false;
		triggered=new ArrayList<String>();
		newtrigger=new ArrayList<String>();
		//do not make panel now; wait for refresh
	}
	
	//update methods:
	
	public void update(){
		//set new triggered buttons
		triggered.clear();
		triggered.addAll(newtrigger);
		newtrigger.clear();
	}
	
	public void refresh(){
		dispose_panel();
		make_panel();
	}
	
	//Panel building method
	
	private void make_panel(){
		//Nifty GUI sucks!!!
		built=true;
		panel=Process_Graphics.$graphics.make_nifty();
		//layout GUI
		panel.addScreen("start", new ScreenBuilder("start"){{
	        controller(new Controller_Initiative());     
	 
	        // <layer>
	        layer(new LayerBuilder("Layer_ID") {{
	        	childLayoutAbsolute();
	            //childLayoutCenter();
	            x("0px");
                y("0px");
                
	 
	            // <panel>
	            panel(new PanelBuilder("stats") {{
	            	childLayoutVertical();
	            	style("nifty-panel-bright");
	            	//backgroundColor("#04bf");
	                height("10%");
	                width("15%");
	                x("0px");
                    y("0px");
	            	childLayoutCenter(); // panel properties, add more...   
                    
                    panel(new PanelBuilder("name") {{
                    	childLayoutVertical();
                    	x("0px");
                    	y("0px");
                    	width("20%");
                    	height("20px");
                    	text(new TextBuilder() {{
                            font("Interface/Fonts/Default.fnt");
                            x("0px");
    	                    y("0px");
                            height("100%");
                            width("100%");
                            text(manager.current.name);
                        }});
                    	text(new TextBuilder(){{
                    		font("Interface/Fonts/Default.fnt");
                            x("0px");
    	                    y("0px");
                            height("100%");
                            width("100%");
                            text("HP "+Integer.toString(manager.current.hp())+"/"+Integer.toString(manager.current.hp_max()));
                    	}});
                    	
                    }});
                    
                    
                    
                    
                    //buttons
                 // GUI elements
	                
                    
	                // GUI elements
                    
	                
	 
	                //.. add more GUI elements here              
	 
	            }});
	            // </panel>
	            
	            //message panel
	            panel(new PanelBuilder("message"){{
	            	style("nifty-panel-bright");
	            	x("15%");
	            	y("0px");
	            	width("50%");
	            	height("7%");
	            }});
	            
	            //buttons
	            control(new ButtonBuilder("Skip Turn", "Skip Turn"){{
	            	x("0px");
	            	y("10%");
                    height("5%");
                    width("8%");
                    
                    interactOnClick("trigger_skip()");
                }});
	            control(new ButtonBuilder("Move","Move"){{
	            	x("0px");
	            	y("15%");
	            	height("5%");
	            	width("8%");
	            	
	            	interactOnClick("trigger_move()");
	            }});
	            control(new ButtonBuilder("Attack","Attack"){{
	            	x("0px");
	            	y("20%");
	            	height("5%");
	            	width("8%");
	            	
	            	interactOnClick("trigger_attack()");
	            }});
	            
	            //initiative button
	            control(new ButtonBuilder("Initiative","Initiative"){{
	            	x("45%");
	            	y("90%");
	            	height("10%");
	            	width("10%");
	            	
	            	interactOnClick("trigger_initiative()");
	            }});
	          }});
	        // </layer>
	      }}.build(panel));
		
		panel.gotoScreen("start");
		
	}
	
	public void dispose_panel(){
		if(built){
			panel.removeScreen("start");
		}
		built=false;
	}
	
	//button trigger methods
	public boolean trigger(String s){
		return triggered.contains(s);
	}
	
	public void make_trigger(String s){
		newtrigger.add(s);
	}
	
	//message window methods
	public String make_message(){
		return "";
	}
	
}
