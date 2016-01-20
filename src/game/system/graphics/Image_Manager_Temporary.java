package game.system.graphics;

import java.util.ArrayList;
import game.system.*;
import com.jme3.texture.*;

//temporary image manager mainly to test preloaded sprite textures (to reduce lag when shifting textures?)
public class Image_Manager_Temporary {
	
	private static final String[] directions={"u","d","l","r"};
	private static final String[] namestoload={"Edith","enemy","Thomas"};
	
	private ArrayList<String> texture_names;
	private ArrayList<Texture> textures;
	
	public Image_Manager_Temporary(){
		texture_names=new ArrayList<String>();
		textures=new ArrayList<Texture>();
		
		//preload
		preload();
	}
	
	public Texture get_texture(String name){
		for(int i=0;i<texture_names.size();i++){
			if(texture_names.get(i).equals(name)){
				return textures.get(i);
			}
		}
		return null;
	}
	
	private void preload(){
		for(String name:namestoload){
			for(String dir:directions){
				for(int i=0;i<=8;i++){
					add_texture(name,dir+Integer.toString(i));
				}
			}
		}
		//temporary test
		//make a large cube at the origin and try to texture it
		/*com.jme3.scene.Geometry g=Process_Graphics.$graphics.draw_box(Process_Main.rootNode,0,0,0,2,2,2);
		Texture t=Process_Graphics.$graphics.get_texture("Sprite/test sprite/idle/d__000.png");
		Process_Graphics.$graphics.set_sprite(g,t);*/
	}
	
	private void add_texture(String name,String dirframe){
		Texture t=Process_Graphics.$graphics.get_texture(name+"/"+dirframe+name+".png");
		textures.add(t);
		texture_names.add(dirframe+name);
	}
}
