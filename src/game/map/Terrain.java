package game.map;

import game.system.Process_Graphics;

import java.io.Serializable;
import com.jme3.scene.*;

//class of terrains for maps
public class Terrain implements Serializable {
	public static final long serialVersionUID=1L;
	
	public boolean visible;
	public String heightmap_name;
	public String texturer;
	public String textureg;
	public String textureb;
	public float base_height;
	
	public Terrain(){
		visible=false;
		heightmap_name="flat";
	}
	
	public void set_texture(String type,String tex){
		if(type.equals("r")){
			texturer=tex;
		}else if(type.equals("g")){
			textureg=tex;
		}else if(type.equals("b")){
			textureb=tex;
		}
	}
	
	//return spatial
	public Spatial get_spatial(){
		
		Node n=new Node();
		if(!visible){
			return n;
		}
		//make new terrain quad (temp: just a flat box)
		//Process_Graphics.$graphics.draw_box(n,0,0,0,200,0.001f,200);
		Process_Graphics.$graphics.draw_terrain(n,heightmap_name,texturer,textureg,textureb);
		//raise to set height
		n.move(0,base_height,0);
		
		return n;
	}
	
}
