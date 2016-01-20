package game.gui.edit;

import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.listbox.*;
import de.lessvoid.nifty.controls.listbox.builder.*;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.textfield.*;
import de.lessvoid.nifty.controls.textfield.builder.*;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import game.gui.HUD;
import game.scene.Scene_Model;
import game.system.Process_Graphics;

import asset.model.*;

import com.jme3.scene.*;
import java.util.ArrayList;

//class for HUD used in modelling scene
public class HUD_Model extends HUD{
	
	public Nifty panel;
	
	private Controller_Model control;
	private Scene_Model scene;  //kept on hand for quick reference
	private Component current_component;
	
	
	public HUD_Model(Scene_Model s){
		scene=s;
		setup();
	}
	
	//setup method
	private void setup(){
		//make controller
		control=new Controller_Model(this,scene);
		//set default window booleans
		load_model_window_open=false;
		
		//make temporary dummy component
		current_component=new Component();
		//make actual panel
		make_panel();
	}
	
	//methods related to current component
	public Component current_component(){
		return current_component;
	}
	
	public void select_component(Component c){
		current_component=c;
		refresh();
	}
	
	public void deselect_component(){
		current_component=new Component();
		refresh();
	}
	
	//update methods
	public void update(){
		//change current model's name to reflect the contents of the entry field
		scene.world.model.name=panel.getScreen("start").findNiftyControl("model rename entry",TextField.class).getText();
	}
	
	@Override
	public void refresh(){
		dispose_panel();
		try{make_panel();}catch(Exception ex){ex.printStackTrace();}
	}
	
	//panel creation methods
	private void make_panel(){
		panel=Process_Graphics.$graphics.make_nifty();
		//lay out panel
		panel.addScreen("start",new ScreenBuilder("start"){{
			controller(control);
			//add main layer
			layer(new LayerBuilder("main layer"){{
				childLayoutAbsolute();
				width("100%");
				height("100%");
				
				//make panels
				
				//model name panel
				panel(new PanelBuilder("name"){{
					childLayoutAbsolute();
					width("50%");
					height("5%");
					x("25%");
					y("0px");
					style("nifty-panel");
					
					//dummy button just to keep text field from being highlighted
					control(new ButtonBuilder("",""){{
						width("1px");
						height("1px");
					}});
					
					//model name field
					control(new TextFieldBuilder("model rename entry",scene.world.model.name){{
                		x("17%");
                		y("0%");
                		maxLength(30);
                		width("65%");
                	}});
					
				}});
				
				//file panel
				panel(new PanelBuilder("file"){{
					childLayoutAbsolute();
					width("10%");
					height("20%");
					style("nifty-panel");
					
					text(new TextBuilder(){{
                		font("Interface/Fonts/Default.fnt");
                        x("0%");
	                    y("0px");
                        height("8%");
                        width("100%");
                        text("Model");
                	}});
					
					//buttons
					control(new ButtonBuilder("New","New"){{
						width("90%");
						height("20%");
						x("5%");
						y("20%");
						interactOnClick("make_new_model()");
					}});
					control(new ButtonBuilder("Open","Open"){{
						width("90%");
						height("20%");
						x("5%");
						y("45%");
						interactOnClick("open_load_model_window()");
					}});
					control(new ButtonBuilder("Save","Save"){{
						width("90%");
						height("20%");
						x("5%");
						y("70%");
						interactOnClick("save_current_model()");
					}});
				}});
				
				//component list panel
				panel(new PanelBuilder("component list"){{
					childLayoutVertical();
					width("15%");
					height("80%");
					x("0px");
					y("20%");
					style("nifty-panel");
					
					text(new TextBuilder(){{
                		font("Interface/Fonts/Default.fnt");
                        height("8%");
                        width("100%");
                        text("Components");
                	}});
					
					control(new ListBoxBuilder("list components"){{
	            		width("100%");
	            		height("40%");
	            		displayItems(11);
	            		optionalHorizontalScrollbar();
	            	}});
					
					//component select button
					control(new ButtonBuilder("Select","Select"){{
						width("80%");
						height("3%");
						interactOnClick("select_component()");
					}});
					
					//add components panel
					panel(new PanelBuilder(){{
						childLayoutVertical();
						width("90%");
						height("40%");
						//buttons
						control(new ButtonBuilder("Add Quad","Add Quad"){{
							width("100%");
							height("15%");
							interactOnClick("add_component_quad()");
						}});
						control(new ButtonBuilder("Add Box","Add Box"){{
							width("100%");
							height("15%");
							interactOnClick("add_component_box()");
						}});
						control(new ButtonBuilder("Add Cylinder","Add Cylinder"){{
							width("100%");
							height("15%");
							interactOnClick("add_component_cylinder()");
						}});
						control(new ButtonBuilder("Add Sphere","Add Sphere"){{
							width("100%");
							height("15%");
							interactOnClick("add_component_sphere()");
						}});
						control(new ButtonBuilder("Add Light","Add Light"){{
							width("100%");
							height("15%");
							interactOnClick("add_component_light()");
						}});
					}});
					
				}});
				
				//component manipulation panel
				panel(new PanelBuilder("component"){{
					childLayoutAbsolute();
					x("75%");
					y("0px");
					width("25%");
					height("100%");
					style("nifty-panel");
					
					text(new TextBuilder(){{
                		font("Interface/Fonts/Default.fnt");
                        x("0%");
	                    y("0px");
                        height("2%");
                        width("100%");
                        text("Selected Component");
                	}});
					
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("0%");
						y("0%");
						height("2%");
						width("20%");
						text("Label: "+current_component.label());
					}});
					
					//delete component button
					control(new ButtonBuilder("Delete","Delete"){{
						width("15%");
						height("2.5%");
						x("85%");
						y("1.25%");
						interactOnClick("delete_selected_component()");
					}});
					
					//entry table labels
					//columns
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("20%");
						y("2.5%");
						height("2%");
						width("20%");
						text("x");
					}});
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("40%");
						y("2.5%");
						height("2%");
						width("20%");
						text("y");
					}});
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("60%");
						y("2.5%");
						height("2%");
						width("20%");
						text("z");
					}});
					//rows
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("0%");
						y("5%");
						height("2%");
						width("20%");
						text("Position");
					}});
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("0%");
						y("10%");
						height("2%");
						width("20%");
						text("Size");
					}});
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("0%");
						y("15%");
						height("2%");
						width("20%");
						text("Rotation");
					}});
					
					//position entries
					
					control(new TextFieldBuilder("component x entry",Float.toString(current_component.x())){{
                		x("20%");
                		y("5%");
                		maxLength(5);
                		width("15%");
                	}});
					control(new TextFieldBuilder("component y entry",Float.toString(current_component.y())){{
                		x("40%");
                		y("5%");
                		maxLength(5);
                		width("15%");
                	}});
					control(new TextFieldBuilder("component z entry",Float.toString(current_component.z())){{
                		x("60%");
                		y("5%");
                		maxLength(5);
                		width("15%");
                	}});
					
					//size entries
					
					control(new TextFieldBuilder("component sx entry",Float.toString(current_component.size_x())){{
                		x("20%");
                		y("10%");
                		maxLength(5);
                		width("15%");
                	}});
					control(new TextFieldBuilder("component sy entry",Float.toString(current_component.size_y())){{
                		x("40%");
                		y("10%");
                		maxLength(5);
                		width("15%");
                	}});
					control(new TextFieldBuilder("component sz entry",Float.toString(current_component.size_z())){{
                		x("60%");
                		y("10%");
                		maxLength(5);
                		width("15%");
                	}});
					
					//rotation controls
					
					control(new TextFieldBuilder("component rx entry",Float.toString(current_component.rot_x())){{
						x("20%");
						y("15%");
						maxLength(5);
						width("15%");
					}});
					control(new TextFieldBuilder("component ry entry",Float.toString(current_component.rot_y())){{
						x("40%");
						y("15%");
						maxLength(5);
						width("15%");
					}});
					control(new TextFieldBuilder("component rz entry",Float.toString(current_component.rot_z())){{
						x("60%");
						y("15%");
						maxLength(5);
						width("15%");
					}});
					
					//extra parameters:
					
					
					
					//texture panel
					text(new TextBuilder(){{
						font("Interface/Fonts/Default.fnt");
						x("0%");
						y("30%");
						height("2%");
						width("20%");
						text("Texture");
					}});
					//if texture image is valid, show it in the window
					if(Process_Graphics.texture_library.texture_valid(current_component.texture())){
						image(new ImageBuilder(){{
							width("30%");
							height("12%");;
							x("0%");
							y("32%");
							filename("Textures/"+current_component.texture()+".png");
						}});
					}
					//u/v entry panel
					panel(new PanelBuilder(){{
						childLayoutHorizontal();
						x("50%");
						y("33%");
						width("50%");
						height("5%");
						
						text(new TextBuilder(){{
							font("Interface/Fonts/Console.fnt");
							text("u");
						}});
						control(new TextFieldBuilder("component u entry",Float.toString(current_component.texture_u())){{
							maxLength(5);
							width("30%");
						}});
						text(new TextBuilder(){{
							font("Interface/Fonts/Console.fnt");
							text("v");
						}});
						control(new TextFieldBuilder("component v entry",Float.toString(current_component.texture_v())){{
							maxLength(5);
							width("30%");
						}});
					}});
					//current texture name
					text(new TextBuilder(){{
						font("Interface/Fonts/Console.fnt");
						x("45%");
						y("30%");
						height("2%");
						width("20%");
						text("Current Texture: "+current_component.texture());
					}});
					//open texture select window
					control(new ButtonBuilder("Change","Change"){{
						x("80%");
						y("30%");
						height("3%");
						width("15%");
						interactOnClick("open_change_texture_window()");
					}});
					
					
					
					//restore button
					control(new ButtonBuilder("Restore","Restore"){{
						width("40%");
						height("5%");
						x("55%");
						y("95%");
						interactOnClick("restore_component_entries()");
					}});
					
					//change commit button
					control(new ButtonBuilder("Commit","Commit"){{
						width("40%");
						height("5%");
						x("5%");
						y("95%");
						interactOnClick("commit_changes()");
					}});
					
				}});
				
				//panels not always visible:
				
				if(load_model_window_open){
					panel(new PanelBuilder("load model"){{
						childLayoutVertical();
						x("35%");
						y("30%");
						width("30%");
						height("40%");
						style("nifty-panel");
						
						text(new TextBuilder(){{
	                		font("Interface/Fonts/Default.fnt");
	                        height("8%");
	                        width("100%");
	                        text("Load Model");
	                	}});
						
						control(new ListBoxBuilder("list model load"){{
							width("100%");
		            		height("60%");
		            		displayItems(10);
		            		optionalHorizontalScrollbar();
						}});
						
						//button panels
						panel(new PanelBuilder("load model buttons"){{
							childLayoutHorizontal();
							align(Align.Center);
							width("100%");
							height("10%");
							//buttons
							control(new ButtonBuilder("Load","Load"){{
								width("50%");
								height("100%");
								interactOnClick("load_selected_model()");
							}});
							control(new ButtonBuilder("Cancel","Cancel"){{
								width("50%");
								height("100%");
								interactOnClick("cancel_load_model()");
							}});
						}});
					}});
				}
				
				//change texture window
				if(change_texture_window_open){
					panel(new PanelBuilder("change texture"){{
						childLayoutVertical();
						width("30%");
						height("75%");
						x("35%");
						y("10%");
						style("nifty-panel");
						
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
						//panel to confirm change/close window
						panel(new PanelBuilder(){{
							childLayoutHorizontal();
							width("100%");
							height("5%");
							
							control(new ButtonBuilder("Change Texture","Change Texture"){{
								width("50%");
								height("100%");
								interactOnClick("change_texture()");
							}});
							control(new ButtonBuilder("Cancel","Cancel"){{
								width("50%");
								height("100%");
								interactOnClick("cancel_change_texture()");
							}});
						}});
						
					}});
				}
				
				
				//more panels
			}});
			//more layers
		}}.build(panel));
		//call niftyguisucks method
		niftyguisucks();
		//set start screen
		panel.gotoScreen("start");
	}
	
	private void niftyguisucks(){
		ListBox l;
		//populate components listbox
		l=panel.getScreen("start").findNiftyControl("list components",ListBox.class);
		for(Component c:scene.world.model.components()){
			l.addItem(Integer.toString(c.id())+":"+c.label());
		}
		//if load model window is open, populate models listbox
		if(load_model_window_open){
			l=panel.getScreen("start").findNiftyControl("list model load",ListBox.class);
			models_condensed=scene.world.library.models_condensed();
			for(Model m:models_condensed){
				l.addItem(m.name);
			}
		}
		//if select texture window is open, populate texture-related listboxes
		if(change_texture_window_open){
			//library listbox
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
	
	//dispose panel
	public void dispose_panel(){
		panel.removeScreen("start");
	}
	
	//window manipulation methods
	
	//load model window
	private boolean load_model_window_open;
	private ArrayList<Model> models_condensed; //list of condensed models, kept for reference
	
	public void open_window_load_model(){
		//open window
		load_model_window_open=true;
		refresh();
	}
	
	public void close_window_load_model(){
		//close window
		load_model_window_open=false;
		refresh();
	}
	
	public Model model_to_load(int index){
		return models_condensed.get(index);
	}
	
	//change texture window
	private boolean change_texture_window_open;
	private String current_texture_library;
	
	public void open_window_change_texture(){
		//open window
		change_texture_window_open=true;
		refresh();
	}
	
	public void close_window_change_texture(){
		//close window
		change_texture_window_open=false;
		refresh();
	}
	
	public void show_texture_library(String s){
		current_texture_library=s;
		refresh();
	}
	
}
