package game.system;

import java.util.ArrayList;

//input class
public class Input {
	
	private ArrayList<String> pressed_keys;
	private ArrayList<String> triggered_keys;
	private ArrayList<String> release_keys;
	private ArrayList<String> double_triggered_keys;
	private ArrayList<String> new_pressed_keys;
	private ArrayList<String> new_release_keys;
	private ArrayList<String> recent_triggered_keys;
	private ArrayList<Integer> recent_trigger_times;
	private int mousex;
	private int mousey;
	
	public Input(){
		pressed_keys=new ArrayList<String>();
		triggered_keys=new ArrayList<String>();
		release_keys=new ArrayList<String>();
		double_triggered_keys=new ArrayList<String>();
		new_pressed_keys=new ArrayList<String>();
		new_release_keys=new ArrayList<String>();
		recent_triggered_keys=new ArrayList<String>();
		recent_trigger_times=new ArrayList<Integer>();
	}
	
	public void update(){
		//clear old key codes
		release_keys.clear();
		triggered_keys.clear();
		double_triggered_keys.clear();
		//replace old key codes with new ones
		release_keys.addAll(new_release_keys);
		//handle triggered keys
		for(String newkey:new_pressed_keys){
			triggered_keys.add(newkey);
			//check for double click
			if(recent_triggered_keys.contains(newkey)){
				double_triggered_keys.add(newkey);
			}else{
				recent_triggered_keys.add(newkey);
				recent_trigger_times.add(0);
			}
		}
		//update recent triggered list
		for(int i=recent_triggered_keys.size()-1;i>=0;i--){
			recent_trigger_times.set(i,recent_trigger_times.get(i)+1);
			if(recent_trigger_times.get(i)>32){
				recent_triggered_keys.remove(i);
				recent_trigger_times.remove(i);
			}
		}
		//changes list of pressed keys
		pressed_keys.addAll(new_pressed_keys);
		for(String rkey:release_keys){
			pressed_keys.remove((Object)rkey);
		}
		//clear new key arrays
		new_pressed_keys.clear();
		new_release_keys.clear();
	}
	
	public boolean trigger(String keycode){
		return triggered_keys.contains(keycode);
	}
	
	public boolean double_trigger(String keycode){
		return double_triggered_keys.contains(keycode);
	}
	
	public boolean pressed(String keycode){
		return pressed_keys.contains(keycode);
	}
	
	public boolean released(String keycode){
		return release_keys.contains(keycode);
	}
	
	public int mouse_x(){
		return mousex;
	}
	
	public int mouse_y(){
		return mousey;
	}
	
	//state change methods; only used for interfacing with JMonkey
	
	public void add_press(String keycode){
		new_pressed_keys.add(keycode);
	}
	
	public void add_release(String keycode){
		new_release_keys.add(keycode);
	}
	
	public void set_mouse(int mx,int my){
		mousex=mx;
		mousey=my;
	}
	
	

}
