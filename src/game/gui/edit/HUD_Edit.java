package game.gui.edit;

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
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.textfield.*;
import de.lessvoid.nifty.controls.textfield.builder.*;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.slider.*;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.slider.builder.*;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.gui.HUD;
import game.gui.right_menu.*;
import game.map.Map;
import game.scene.Scene_WorldEdit;
import game.scripting.*;
import game.system.Process_Graphics;
import game.system.Process_Main;

import java.util.ArrayList;

import asset.model.*;

//class for the HUD used in world edit mode
public class HUD_Edit extends HUD{
	
	public String mode;
	/*List of modes:
	 * 1) "start" (place party starting location)
	 * 2) "floor" (place floor tiles)
	 * 3) "enemy" (place enemies)
	 * 4) "event" (place events)
	 * 5) "static" (place static entities (with models))
	 * 6) "light" (adjust ambient/directional lights)
	 * 7) "terrain" (adjust terrain)
	*/
	
	public boolean skip;  //indicates if action processing for clicking should be skipped (i.e. if button was pressed)
	public boolean frozen;  //indicates if a window has placed a hold on clicking evaluation for outside of hud
	
	//right click menu windows
	public Window_Event_Right_Click_Menu event_right_click_window;
	
	//sub-windows
	public Window_Journal journal_window;
	public Window_Rename rename_window;
	public Window_Variables variable_window;
	public Window_Event event_window;
	public Window_Event_Condition event_condition_window;
	public Window_Coordinate_Select coordinate_select_window;
	
	private Controller_Edit control;
	private Model selected_model;
	private String current_library_id="test";
	
	//kept for reference
	private Scene_WorldEdit scene;
	
	public HUD_Edit(Scene_WorldEdit s){
		scene=s;
		setup("start");
	}
	
	public HUD_Edit(String m,Scene_WorldEdit s){
		scene=s;
		setup(m);
	}
	
	private void setup(String m){
		mode=m;
		skip=false;
		frozen=false;
		control=new Controller_Edit(this,scene);
		
		//make right-click menus
		event_right_click_window=new Window_Event_Right_Click_Menu(this);
		
		//make sub-windows
		journal_window=new Window_Journal(this);
		rename_window=new Window_Rename(this);
		variable_window=new Window_Variables(this);
		event_window=new Window_Event(this);
		event_condition_window=new Window_Event_Condition(this);
		coordinate_select_window=new Window_Coordinate_Select(this);
		
		//window booleans (only for old window types)
		window_map_rename_open=false;
		window_map_delete_open=false;
		
		//make actual panel
		make_panel();
	}
	
	//selected model methods
	
	public Model selected_model(){
		return selected_model;
	}
	
	public void select_model(Model m){
		selected_model=m;
	}
	
	//current library methods
	
	public String current_library_id(){
		return current_library_id;
	}
	
	public Model_Library current_library(){
		return Process_Graphics.get_library(current_library_id);
	}
	
	//update methods
	
	public void update(){
		skip=false;
		//update right click menu windows
		event_right_click_window.update();
		//update dependent windows
		journal_window.update();
		rename_window.update();
		variable_window.update();
		event_window.update();
		event_condition_window.update();
		coordinate_select_window.update();
	}
	
	@Override
	public void refresh(){
		//dispose_panel();  //no longer needed; handled in the make panel screen
		try{
			make_panel();
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	private void make_panel(){
		panel=Process_Graphics.$graphics.make_nifty();
		//try screen buffering
		panel.gotoScreen("old");
		//dispose old screen
		dispose_panel();
		//lay out panel:
		// <screen>
	    panel.addScreen("start", new ScreenBuilder("start"){{
	        controller(control);     
	 
	        // <layer>
	        layer(new LayerBuilder("Layer_ID") {{
	            childLayoutAbsolute();
	            width("100%");
            	height("100%");
	 
	            // <panel>
	            panel(new PanelBuilder("buttons") {{
	            	//childLayoutCenter(); // panel properties, add more... 
	            	childLayoutAbsolute();
	            	width("100%");
	            	height("100%");
	            	
	            	//dummy button just to keep text field from being highlighted
					control(new ButtonBuilder("",""){{
						width("1px");
						height("1px");
					}});
	 
	                // GUI elements
	                control(new ButtonBuilder("test1", "Start"){{
	                    height("3%");
	                    width("5%");
	                    x("0px");
	                    y("0px");
	                    interactOnClick("set_mode_start()");
	                }});
	                control(new ButtonBuilder("test2", "Floor"){{
	                    height("3%");
	                    width("5%");
	                    x("0px");
	                    y("3%");
	                    interactOnClick("set_mode_floor()");
	                }});
	                control(new ButtonBuilder("test3", "Enemies"){{
	                    height("3%");
	                    width("5%");
	                    x("0px");
	                    y("6%");
	                    interactOnClick("set_mode_enemy()");
	                }});
	                control(new ButtonBuilder("Events","Events"){{
	                	height("3%");
	                	width("5%");
	                	x("0px");
	                	y("9%");
	                	interactOnClick("set_mode_event()");
	                }});
	                control(new ButtonBuilder("Statics","Statics"){{
	                	height("3%");
	                	width("5%");
	                	x("0px");
	                	y("12%");
	                	interactOnClick("set_mode_static()");
	                }});
	                control(new ButtonBuilder("scenelights","Lights"){{
	                	height("3%");
	                	width("5%");
	                	x("0px");
	                	y("15%");
	                	interactOnClick("set_mode_light()");
	                }});
	                control(new ButtonBuilder("sceneterrain","Terrain"){{
	                	height("3%");
	                	width("5%");
	                	x("0px");
	                	y("18%");
	                	interactOnClick("set_mode_terrain()");
	                }});
	                
	 
	                //.. add more GUI elements here              
	 
	            }});
	            
	            panel(new PanelBuilder("map select"){{
	            	childLayoutAbsolute();
	            	x("85%");
	            	y("0px");
	            	width("16%");
	            	height("40%");
	            	style("nifty-panel");
	            	
	            	text(new TextBuilder(){{
	            		x("35%");
	            		y("2%");
                		font("Interface/Fonts/Default.fnt");
                        text("Maps");
                	}});
	            	
	            	control(new ButtonBuilder("new map","New"){{
	            		height("8%");
	            		width("15%");
	            		x("83%");
	            		y("0%");
	            		interactOnClick("new_map()");
	            	}});
	            	
	            	control(new ListBoxBuilder("maps"){{
	            		x("0px");
	            		y("10%");
	            		width("100%");
	            		height("100%");
	            		displayItems(11);
	            		optionalHorizontalScrollbar();
	            		
	            	}});
	            	
	            	control(new ButtonBuilder("open map","Open"){{
	            		height("8%");
	            		width("23%");
	            		x("10%");
	            		y("91%");
	            		interactOnClick("load_map()");
	            	}});
	            	
	            	control(new ButtonBuilder("rename map","Rename"){{
	            		height("8%");
	            		width("23%");
	            		x("35%");
	            		y("91%");
	            		interactOnClick("open_map_rename()");
	            	}});
	            	
	            	control(new ButtonBuilder("delete map","Delete"){{
	            		height("8%");
	            		width("23%");
	            		x("60%");
	            		y("91%");
	            		interactOnClick("confirm_delete_map()");
	            	}});
	            	
	            }});
	            
	            //windows not always open
	            
	            //map rename window
	            if(window_map_rename_open){
	            	panel(new PanelBuilder("map rename"){{
	            		childLayoutAbsolute();
	                	x("40%");
	                	y("45%");
	                	width("20%");
	                	height("10%");
	                	style("nifty-panel");
	                	
	                	text(new TextBuilder(){{
		            		x("35%");
		            		y("0%");
	                		font("Interface/Fonts/Default.fnt");
	                        text("Rename Map");
	                	}});
	                	
	                	text(new TextBuilder(){{
	                		x("0%");
	                		y("60%");
	                		font("Interface/Fonts/Console.fnt");
	                		text("ID "+window_map_rename_selected_map.id_string()+":");
	                	}});
	                	
	                	control(new TextFieldBuilder("map rename entry",window_map_rename_selected_map.name){{
	                		x("17%");
	                		y("50%");
	                		maxLength(30);
	                		width("65%");
	                	}});
	                	
	                	control(new ButtonBuilder("map rename accept","Accept"){{
	                		x("83%");
	                		y("50%");
	                		width("18%");
	                		height("40%");
	                		interactOnClick("close_map_rename()");
	                	}});
	                }});
	            }
	            
	            //confirm delete window
	            if(window_map_delete_open){
	            	panel(new PanelBuilder("map delete"){{
	            		childLayoutAbsolute();
	                	x("40%");
	                	y("30%");
	                	width("20%");
	                	height("20%");
	                	style("nifty-panel");
	                	
	                	text(new TextBuilder(){{
		            		x("20%");
		            		y("0%");
	                		font("Interface/Fonts/Default.fnt");
	                        text("Confirm Delete Map");
	                	}});
	                	
	                	text(new TextBuilder(){{
		            		x("5%");
		            		y("40%");
		            		width("80%");
		            		height("40%");
	                		font("Interface/Fonts/Console.fnt");
	                        text("Are you sure you want to delete this map?  This CANNOT be undone.");
	                        wrap(true);
	                	}});
	                	
	                	control(new ButtonBuilder("confirm delete","Delete"){{
	                		x("20%");
	                		y("80%");
	                		width("20%");
	                		height("15%");
	                		interactOnClick("delete_map()");
	                	}});
	                	
	                	control(new ButtonBuilder("cancel delete","Cancel"){{
	                		x("60%");
	                		y("80%");
	                		width("20%");
	                		height("15%");
	                		interactOnClick("cancel_delete_map()");
	                	}});
	                	
	            	}});
	            }
	            
	            //static entity window
	            
	            if(mode=="static"){
	            	panel(new PanelBuilder("static entities"){{
		            	childLayoutVertical();
		            	x("85%");
		            	y("40%");
		            	width("16%");
		            	height("60%");
		            	style("nifty-panel");
		            	
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Select Model");
		            	}});
		            	
		            	//listbox of available models
		            	control(new ListBoxBuilder("list models"){{
		            		width("100%");
		            		height("60%");
		            		displayItems(12);
		            		optionalHorizontalScrollbar();
		            	}});
		            	
		            	//panel of buttons
		            	panel(new PanelBuilder("static model buttons"){{
		            		width("100%");
		            		height("5%");
		            		childLayoutHorizontal();
		            		align(Align.Center);
		            		//make buttons
		            		control(new ButtonBuilder("Select","Select"){{
								width("50%");
								height("100%");
								interactOnClick("select_model()");
							}});
							control(new ButtonBuilder("Cancel","Cancel"){{
								width("50%");
								height("100%");
								interactOnClick("deselect_model()");
							}});
		            	}});
		            	
		            }});
	            }
	            
	            //lights window
	            if(mode=="light"){
	            	panel(new PanelBuilder("lights"){{
		            	childLayoutVertical();
		            	x("75%");
		            	y("60%");
		            	width("26%");
		            	height("40%");
		            	style("nifty-panel");
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Adjust Lights");
		            	}});
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Ambient");
		            	}});
		            	
		            	panel(new PanelBuilder("acolor"){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("7%");
		            			text(" R ");
		            		}});
		            		control(new TextFieldBuilder("ambient r",Float.toString(scene.world.map.ambient_color.getRed())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" G ");
		            		}});
		            		control(new TextFieldBuilder("ambient g",Float.toString(scene.world.map.ambient_color.getGreen())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" B ");
		            		}});
		            		control(new TextFieldBuilder("ambient b",Float.toString(scene.world.map.ambient_color.getBlue())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" A ");
		            		}});
		            		control(new TextFieldBuilder("ambient a",Float.toString(scene.world.map.ambient_color.getAlpha())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" I ");
		            		}});
		            		control(new TextFieldBuilder("ambient i",Float.toString(scene.world.map.ambient_intensity)){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            	}});
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Directional 1");
		            	}});
		            	
		            	panel(new PanelBuilder("d1color"){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" R ");
		            		}});
		            		control(new TextFieldBuilder("dir1 r",Float.toString(scene.world.map.directional1_color.getRed())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" G ");
		            		}});
		            		control(new TextFieldBuilder("dir1 g",Float.toString(scene.world.map.directional1_color.getGreen())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" B ");
		            		}});
		            		control(new TextFieldBuilder("dir1 b",Float.toString(scene.world.map.directional1_color.getBlue())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" A ");
		            		}});
		            		control(new TextFieldBuilder("dir1 a",Float.toString(scene.world.map.directional1_color.getAlpha())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" I ");
		            		}});
		            		control(new TextFieldBuilder("dir1 i",Float.toString(scene.world.map.directional1_intensity)){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            	}});
		            	
		            	panel(new PanelBuilder("d1direction"){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" X ");
		            		}});
		            		control(new TextFieldBuilder("dir1 x",Float.toString(scene.world.map.directional1_vector.x)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" Y ");
		            		}});
		            		control(new TextFieldBuilder("dir1 y",Float.toString(scene.world.map.directional1_vector.y)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" Z ");
		            		}});
		            		control(new TextFieldBuilder("dir1 z",Float.toString(scene.world.map.directional1_vector.z)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            	}});
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Directional 2");
		            	}});
		            	
		            	panel(new PanelBuilder("d2color"){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" R ");
		            		}});
		            		control(new TextFieldBuilder("dir2 r",Float.toString(scene.world.map.directional2_color.getRed())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" G ");
		            		}});
		            		control(new TextFieldBuilder("dir2 g",Float.toString(scene.world.map.directional2_color.getGreen())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" B ");
		            		}});
		            		control(new TextFieldBuilder("dir2 b",Float.toString(scene.world.map.directional2_color.getBlue())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" A ");
		            		}});
		            		control(new TextFieldBuilder("dir2 a",Float.toString(scene.world.map.directional2_color.getAlpha())){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("8%");
		            			text(" I ");
		            		}});
		            		control(new TextFieldBuilder("dir2 i",Float.toString(scene.world.map.directional2_intensity)){{
		                		maxLength(6);
		                		width("12%");
		                	}});
		            	}});
		            	
		            	panel(new PanelBuilder("d2direction"){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" X ");
		            		}});
		            		control(new TextFieldBuilder("dir2 x",Float.toString(scene.world.map.directional2_vector.x)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" Y ");
		            		}});
		            		control(new TextFieldBuilder("dir2 y",Float.toString(scene.world.map.directional2_vector.y)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("10%");
		            			text(" Z ");
		            		}});
		            		control(new TextFieldBuilder("dir2 z",Float.toString(scene.world.map.directional2_vector.z)){{
		                		maxLength(6);
		                		width("15%");
		                	}});
		            	}});
		            	
		            	panel(new PanelBuilder("buttons"){{
		            		childLayoutHorizontal();
		            		height("10%");
		            		width("100%");
		            		
		            		control(new ButtonBuilder("Apply Changes","Apply Changes"){{
		            			width("50%");
		            			height("100%");
		            			interactOnClick("confirm_light_changes()");
		            		}});
		            		control(new ButtonBuilder("Revert Changes","Revert Changes"){{
		            			width("50%");
		            			height("100%");
		            			interactOnClick("cancel_light_changes()");
		            		}});
		            	}});
		            	
	            	}});
	            }
	            
	            //terrain panel
	            if(mode.equals("terrain")){
	            	panel(new PanelBuilder("terrain"){{
	            		childLayoutVertical();
		            	x("85%");
		            	y("60%");
		            	width("16%");
		            	height("40%");
		            	style("nifty-panel");
		            	
		            	text(new TextBuilder(){{
		            		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Terrain");
		            	}});
		            	
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("5%");
		            		
		            		control(new CheckboxBuilder("terrain visible"){{
			            		height("100%");
			            		width("8%");
			            		checked(scene.world.map.terrain.visible);
			            		interactOnClick("toggle_terrain_visible()");
			            	}});
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		                        height("100%");
		                        width("25%");
		                        text("Visible");
		            		}});
		            	}});
		            	//height map
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		                        height("100%");
		                        width("30%");
		                        text("Heightmap: ");
		            		}});
		            		
		            		control(new TextFieldBuilder("heightmap name",scene.world.map.terrain.heightmap_name){{
		            			maxLength(30);
		            			width("40%");
		            		}});
		            		
		            		control(new ButtonBuilder("change","change"){{
		            			height("80%");
		            			width("15%");
		            			interactOnClick("change_heightmap()");
		            		}});
		            		control(new ButtonBuilder("cancel","cancel"){{
		            			height("80%");
		            			width("15%");
		            			interactOnClick("cancel_light_changes()");
		            		}});
		            	}});
		            	//texture r
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		                        height("100%");
		                        width("50%");
		                        text("R Texture: "+scene.world.map.terrain.texturer);
		            		}});
		            		
		            		control(new ButtonBuilder("change","change"){{
		            			height("80%");
		            			width("20%");
		            			interactOnClick("change_texture_r()");
		            		}});
		            	}});
		            	//texture g
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		                        height("100%");
		                        width("50%");
		                        text("G Texture: "+scene.world.map.terrain.textureg);
		            		}});
		            		
		            		control(new ButtonBuilder("change","change"){{
		            			height("80%");
		            			width("20%");
		            			interactOnClick("change_texture_g()");
		            		}});
		            	}});
		            	//texture b
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		                        height("100%");
		                        width("50%");
		                        text("B Texture: "+scene.world.map.terrain.textureb);
		            		}});
		            		
		            		control(new ButtonBuilder("change","change"){{
		            			height("80%");
		            			width("20%");
		            			interactOnClick("change_texture_b()");
		            		}});
		            	}});
		            	//terrain height
		            	panel(new PanelBuilder(){{
		            		childLayoutHorizontal();
		            		width("100%");
		            		height("10%");
		            		
		            		text(new TextBuilder(){{
		            			font("Interface/Fonts/Console.fnt");
		            			height("100%");
		            			width("40%");
		            			text("Base Height: ");
		            		}});
		            		
		            		control(new TextFieldBuilder("terrain height",Float.toString(scene.world.map.terrain.base_height)){{
		            			maxLength(8);
		            			width("30%");
		            		}});
		            		
		            		control(new ButtonBuilder("change","change"){{
		            			height("80%");
		            			width("30%");
		            			interactOnClick("change_terrain_height()");
		            		}});
		            	}});
	            	}});
	            }
	            
	            //texture select panel
	            if(window_terrain_texture_open){
	            	panel(new PanelBuilder(){{
	            		childLayoutVertical();
	            		x("30%");
	            		y("20%");
	            		width("40%");
	            		height("60%");
	            		style("nifty-panel");
	            		
	            		//texture listboxes
	            		//panel label
						text(new TextBuilder(){{
							font("Interface/Fonts/Default.fnt");
							width("100%");
							height("5%");
							text("Select Texture");
						}});
						//library select
						text(new TextBuilder(){{
							font("Interface/Fonts/Console.fnt");
							width("100%");
							height("5%");
							text("Select Library");
						}});
						control(new ListBoxBuilder("texture library select"){{
							width("100%");
							height("30%");
							displayItems(7);
							optionalHorizontalScrollbar();
						}});
						control(new ButtonBuilder("Open Library","Open Library"){{
							width("100%");
							height("5%");
							interactOnClick("show_texture_library()");
						}});
						//texture select
						text(new TextBuilder(){{
							font("Interface/Fonts/Console.fnt");
							width("100%");
							height("5%");
							text("Select Texture");
						}});
						control(new ListBoxBuilder("texture select"){{
							width("100%");
							height("40%");
							displayItems(12);
							optionalHorizontalScrollbar();
						}});
	            		
	            		
	            		//close panel
	            		panel(new PanelBuilder(){{
	            			childLayoutHorizontal();
	            			width("80%");
            				height("10%");
            				control(new ButtonBuilder("Accept","Accept"){{
            					height("90%");
            					width("50%");
            					interactOnClick("accept_texture_change()");
            				}});
            				control(new ButtonBuilder("Cancel","Cancel"){{
            					height("90%");
            					width("50%");
            					interactOnClick("cancel_texture_change()");
            				}});
	            			
	            		}});
	            		
	            	}});
	            }
	            
	            //more panels
	          }});
	        // more layers
	      }}.build(panel));
	    // </screen>
	    
	    
	    niftyguisucks();
	    //catch all nifty gui sucks button (for debugging)
	    
	    //build right click menus
	    event_right_click_window.refresh(panel);
	    //build windows (will check on their own if is visible or not)
	    //NOTE: ORDER MATTERS; SEE WINDOW DEPENDENCIES DOCUMENT
	    journal_window.refresh(panel);
	    event_window.refresh(panel);
	    event_condition_window.refresh(panel);
	    variable_window.refresh(panel);
	    rename_window.refresh(panel);
	    coordinate_select_window.refresh(panel);
	    
	    //temporary test
	    /*
	    new PanelBuilder(){{
	    	childLayoutVertical();
	    	width("50%");
	    	height("50%");
	    	style("nifty-panel");
	    	//test
	    	control(new ButtonBuilder("test,test"){{
	    		width("50%");
	    		height("50%");
	    		interactOnClick("journal_window.test()");
	    	}});
	    }}.build(panel,panel.getScreen("start"),panel.getScreen("start").getLayerElements().get(0));
	    */
	    //end temporary test
	    panel.gotoScreen("start"); // start the screen
	}
	
	//Nifty-GUI is stupid methods
	
	private void niftyguisucks(){
		//populate map list
		ListBox l=panel.getScreen("start").findNiftyControl("maps",ListBox.class);
		for(Map m:Process_Main.quest.universe.get_maps()){
			l.addItem(" "+m.id_string()+": "+m.name);
		}
		//if statics window is open, populate models list
		if(mode.equals("static")){
			l=panel.getScreen("start").findNiftyControl("list models",ListBox.class);
			for(Model m:current_library().models_condensed()){
				l.addItem(m.name);
			}
		}
		//if texture change window is open, populate texture list
		if(window_terrain_texture_open){
			l=panel.getScreen("start").findNiftyControl("texture library select",ListBox.class);
			for(String lib:Process_Graphics.texture_library.get_categories()){
				l.addItem(lib);
			}
			//texture listbox
			l=panel.getScreen("start").findNiftyControl("texture select",ListBox.class);
			for(String tex:Process_Graphics.texture_library.get_textures(current_texture_library)){
				l.addItem(tex);
			}
		}
	}
	
	
	
	public void dispose_panel(){
		panel.removeScreen("start");
	}
	
	//window manipulation methods
	
	//map rename window
	
	private boolean window_map_rename_open;
	private Map window_map_rename_selected_map;  //map currently selected for renaming 
	
	public void open_map_rename_window(Map m){
		window_map_rename_open=true;
		window_map_rename_selected_map=m;
		refresh();
	}
	
	public void close_map_rename_window(){
		//rename map
		window_map_rename_selected_map.name=panel.getScreen("start").findNiftyControl("map rename entry",TextField.class).getText();
		//close map rename window
		window_map_rename_open=false;
		window_map_rename_selected_map=null;
		refresh();
	}
	
	//map delete confirm window
	
	private boolean window_map_delete_open;
	private Map window_map_delete_selected_map;  //map currently set for deleting
	
	public void open_map_delete_confirm_window(Map m){
		window_map_delete_open=true;
		window_map_delete_selected_map=m;
		refresh();
	}
	
	public void delete_map(){
		Process_Main.quest.universe.delete_map(window_map_delete_selected_map.id());
		close_map_delete_window();
	}
	
	public void close_map_delete_window(){
		window_map_delete_open=false;
		window_map_delete_selected_map=null;
		refresh();
	}
	
	//terrain texture window
	private boolean window_terrain_texture_open;
	private String current_texture_library;
	private String type; //type of texture (R,G,B)
	
	public void open_terrain_texture_window(String t){
		type=t;
		current_texture_library="test";
		window_terrain_texture_open=true;
		refresh();
	}
	
	public void show_texture_library(String l){
		current_texture_library=l;
		refresh();
	}
	
	public void set_terrain_texture(String tex){
		scene.world.map.terrain.set_texture(type,tex);
	}
	
	public void close_terrain_texture_window(){
		window_terrain_texture_open=false;
		refresh();
	}
	
	
	
	
	//actual public methods
	public void hide_right_click_menus(){
		event_right_click_window.hide();
	}
}
