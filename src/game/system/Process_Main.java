package game.system;
import game.Game;
import game.save.Quest;
import game.scene.Scene;
import game.scene.Scene_Initial;
import io.File;
import io.game_dump.Dump_Quest;

import com.jme3.scene.Node;
import com.jme3.export.binary.*;

public class Process_Main implements Runnable {
	
	public static Node rootNode;
	
	
	public static boolean main_ready;
	public static boolean graphics_ready;
	
	public static Input $input;
	public static Scene $scene;
	public static Quest quest;
	
	public static void load_default_quest(){
		try{
			Dump_Quest d=(Dump_Quest)File.open("/data/game_data.dat");
			Process_Main.quest.read(d);
		}catch(Exception ex){
			System.out.println("Load Failed");
			ex.printStackTrace();
			//throw new RuntimeException();
		}
	}
	
	public static void save_default_quest(){
		//first, also save current model libraries
		Process_Graphics.save_model_libraries();
		//save file
		try{
			Dump_Quest d=new Dump_Quest();
			quest.write(d);
			File.export(d,"/data/game_data.dat");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public static BinaryExporter exporter;
	public static BinaryImporter importer;
	
	public String name;
	
	public static long lasttime;
	
	public Process_Main(String n){
		name=n;
		main_ready=false;
		lasttime=System.currentTimeMillis();
	}
	
	public void run() {
		System.out.println(name+" is starting.");
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		//insert setup
		
		//make scene object
        $scene=new Scene_Initial();
        //make input object
        $input=new Input();
		
		//main loop:
		try{
            nextframe();
            //main loop
            try{while(!$scene.finished){
            	//update input
            	$input.update();
            	//update scene
            	$scene.update();
            	//advance one frame
            	nextframe();
            }}catch(Exception ex){ex.printStackTrace();}
		}catch(Exception ex){ex.printStackTrace();}
		System.out.println("out");
	}
	
	
	private int skipframe_count=32; //counts the number of times the main thread goes by without waiting
	
	private void nextframe(){
        //synchronize with other threads
		
		
		try{
			//determine how long to sleep to try and maintain 60 fps
	    	long diff=System.currentTimeMillis()-Process_Main.lasttime;
	    	long wait=16-diff;
	    	if(wait<0){
	    		//System.out.println(wait);
	    		wait=0;
	    	}
			Thread.sleep(wait);
			Process_Main.lasttime=System.currentTimeMillis();
			
			main_ready=true;
			int waitcount=0;
			while(!(graphics_ready&&main_ready)){
				Thread.sleep(1);
				waitcount++;
				skipframe_count++;
				if(waitcount>0&&skipframe_count<$scene.allowed_skip()){
					//update input, since graphics thread is too busy
					//Process_Graphics.$graphics.getInputManager().update(Process_Graphics.$graphics.getTimer().getTimePerFrame());
					
					break;
				}
				if(waitcount>6000){
					System.out.println("Graphics Thread has stopped responding at");
					StackTraceElement[] s=Game.t2.getStackTrace();
					for(StackTraceElement e:s){
						System.out.println("-"+e.toString());
					}
					throw new RuntimeException();
				}
			}
			if(graphics_ready){
				skipframe_count=0;
			}
			Thread.sleep(2);
			main_ready=false;
			
		}catch(Exception ex){
			System.out.println("shutting down");
			$scene.finished=true;
		}
    }

}
