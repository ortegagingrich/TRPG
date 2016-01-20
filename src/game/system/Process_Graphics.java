package game.system;

import asset.model.*;
import asset.sprite.*;
import game.system.graphics.Graphics;
import io.File;

public class Process_Graphics implements Runnable {
	
	public static Graphics $graphics;
	
	//model libraries
	private static Model_Library test_library;
	//texture library
	public static Texture_Library texture_library;
	
	public static void load_model_libraries(){
		//make texture library
		texture_library=new Texture_Library();
		//load model libraries; if does not work, make new ones
		try{
			Model_Library m=(Model_Library)File.open("/model/test_library.mlb");
			test_library=m;
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Could not locate model library.");
			test_library=new Model_Library("test");
		}
	}
	
	public static void save_model_libraries(){
		try{
			File.export(test_library,"/model/test_library.mlb");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static Model_Library get_library(String li){
		//temporary; always return test library
		return test_library;
	}
	
	public static Model get_model(String li,int bi){
		//first get the correct library
		Model_Library lib=get_library(li);
		//return the correct model
		return lib.get_model(bi);
	}
	
	//handle loading/saving spritesets
	public static Spriteset spriteset;
	
	public static void load_spriteset(){
		//try to load the spriteset
		try{
			//load spriteset
			Spriteset s=(Spriteset)File.open("/data/sprite_data.dat");
			spriteset=s;
			//make changes to spriteset based on text document
			spriteset.modify_sprites();
			//save it; in the future, a new copy will be saved only if there is a modification
			save_spriteset();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Could not locate spriteset");
			spriteset=new Spriteset();
		}
		
	}
	
	public static void save_spriteset(){
		//export it to file
		try{
			File.export(spriteset,"/data/sprite_data.dat");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public String name;
	
	public Process_Graphics(String n){
		name=n;
	}

	public void run() {
		System.out.println(name+" is starting.");
		try{
			$graphics=new Graphics();
		}catch(Exception ex){
			ex.printStackTrace();
		}
			
		$graphics.start();
	}

}
