package game.gui.in_game;

import de.lessvoid.nifty.*;
import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.button.builder.*;
import de.lessvoid.nifty.effects.impl.TextColor;
import game.scripting.message.*;

import java.util.ArrayList;

//window which displays messages
public class Window_Message extends Window {
	
	public static final int rowmax=100;  //number of characters allowed per row
	
	private Nifty nifty;
	private Element base;
	private ArrayList<Element> rows;
	
	private Message message;  //message currently being shown
	
	//message construction variables
	private ArrayList<ArrayList<Fragment>> split;
	private boolean constructing;
	private int construct_row;
	private int construct_pos;
	
	public Window_Message(HUD_Main hu,Nifty n){
		super(hu,false,400,650,1200,250);
		nifty=n;
		nifty.subscribeAnnotations(this);
		constructing=false;
		construct_row=0;
		construct_pos=0;
		//just to be safe
		rows=new ArrayList<Element>();
	}
	
	//update
	@Override
	public void update(){
		//if window not visible, or if not constructing, return without doing anything
		if(!visible||!constructing||rows.size()==0){
			return;
		}
		//continue construction of message (one fragment at a time)
		//check to see if all rows are displayed
		if(construct_row>=split.size()){
			//set to not constructing, show continue button and return
			constructing=false;
			//show continue button
			new ButtonBuilder("continue","Continue..."){{
				width("75%");
				height("20%");
				x("25%");
				y("80%");
			}}.build(nifty,nifty.getScreen("start"),base);
			return;
		}
		
		//check to see if all fragments of current row are displayed
		if(construct_pos>=split.get(construct_row).size()){
			//move to the next row and set position to 0
			construct_row++;
			construct_pos=0;
			return;
		}
		
		//if this point is reached, print the next fragment
		Fragment f=split.get(construct_row).get(construct_pos);
		f.print(nifty,rows.get(construct_row));
		//advance to next position
		construct_pos++;
		
	}
	
	//refresh
	@Override
	public void refresh(Nifty n,Element parent){
		//if window is not visible, just return
		if(!visible){
			return;
		}
		//make base window
		base=new PanelBuilder("message panel"){{
			childLayoutAbsolute();
			x(Integer.toString(screen_x)+"px");
			y(Integer.toString(screen_y)+"px");
			width(Integer.toString(width)+"px");
			height(Integer.toString(height)+"px");
			style("nifty-panel");
		}}.build(nifty,nifty.getScreen("start"),parent);
		
		//show sub-panels inside of the base window
		//make arraylist of rows
		rows=new ArrayList<Element>();
		//line one panel
		Element r1=new PanelBuilder(""){{
			childLayoutHorizontal();
			x("25%");
			y("10%");
			width("75%");
			height("15%");
		}}.build(nifty,nifty.getScreen("start"),base);
		rows.add(r1);
		//line two panel
		Element r2=new PanelBuilder(""){{
			childLayoutHorizontal();
			x("25%");
			y("25%");
			width("75%");
			height("15%");
		}}.build(nifty,nifty.getScreen("start"),base);
		rows.add(r2);
		//line three panel
		Element r3=new PanelBuilder(""){{
			childLayoutHorizontal();
			x("25%");
			y("40%");
			width("75%");
			height("15%");
		}}.build(nifty,nifty.getScreen("start"),base);
		rows.add(r3);
		//line four panel
		Element r4=new PanelBuilder(""){{
			childLayoutHorizontal();
			x("25%");
			y("55%");
			width("75%");
			height("15%");
		}}.build(nifty,nifty.getScreen("start"),base);
		rows.add(r4);
		//line five panel
		Element r5=new PanelBuilder(""){{
			childLayoutHorizontal();
			x("25%");
			y("70%");
			width("90%");
			height("15%");
		}}.build(nifty,nifty.getScreen("start"),base);
		rows.add(r5);
	}
	
	//button methods
	@NiftyEventSubscriber(id="continue")
	public void continue_message(String id,ButtonClickedEvent event){
		//clear message
		clear_message();
	}
	
	//message-methods
	public void show_message(Message m){
		//first clear the previous message
		clear_message();
		//next, set message and reset message construction procedure
		message=m;
		split=m.split(rowmax);
		constructing=true;
		construct_row=0;
		construct_pos=0;
		//show window
		show();
	}
	
	public void clear_message(){
		message=null;
		//hide window
		hide();
	}
	
	public void wait_until_dismissed(){
		while(message!=null){
			//wait until message disappears
			try{
				Thread.sleep(5);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
