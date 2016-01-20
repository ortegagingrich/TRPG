package game;

import game.system.Process_Graphics;
import game.system.Process_Main;

public class Game {
	
	//output controls
	public static final boolean print_lag=false;

	//threads
    public static Thread t1;//main thread
    public static Thread t2;//graphics thread
    
    public static boolean ready=false;
    public final static String directory="/storage/Software_Development/games/TRPG/data";
    
    public final static String ASSET_DIRECTORY="/storage/Software_Development/games/TRPG/assets/";
    
    public static void main(String[] args){
        //make threads
        t1=new Thread(new Process_Main("Main Process"));
        t2=new Thread(new Process_Graphics("Graphics Process"));
        //start threads
        try{
        	t1.start();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        try{
        	t2.start();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
    }
    
}
