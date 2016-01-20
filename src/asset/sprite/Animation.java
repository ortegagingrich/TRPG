package asset.sprite;

import java.io.*;
import java.util.ArrayList;
import com.jme3.texture.*;
import com.jme3.scene.*;
import game.system.*;

public class Animation implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String name;
	
	private String local_directory;
	protected int length;  //number of frames in animation
	
	//transient arraylists of textures (by default empty - preloaded when called for)
	protected transient boolean loaded;
	private transient ArrayList<Texture> textures_u;
	private transient ArrayList<Texture> textures_d;
	private transient ArrayList<Texture> textures_l;
	private transient ArrayList<Texture> textures_r;
	
	public Animation(String n,int l,String dir){
		name=n;
		length=l;
		local_directory=dir+name+"/";
		loaded=false;
	}
	
	public String name(){
		return name;
	}
	
	public int length(){
		return length;
	}
	
	//display texture to 
	public void display_texture(Geometry main_screen,Geometry over_screen,String direction,int frame){
		Texture t=get_texture(direction,frame);
		//set main screen
		game.system.Process_Graphics.$graphics.set_sprite(main_screen,t);
		//set over_screen to blank
		
	}
	
	//for actual sprites, use display_texture() instead
	public Texture get_texture(String direction,int frame){
		if(!loaded){
			load();
		}
		
		if(direction.equals("u")){
			return textures_u.get((frame%length));
		}else if(direction.equals("d")){
			return textures_d.get((frame%length));
		}else if(direction.equals("l")){
			return textures_l.get((frame%length));
		}else if(direction.equals("r")){
			return textures_r.get((frame%length));
		}
		return null;
	}
	
	protected void load(){
		textures_u=new ArrayList<Texture>();
		textures_d=new ArrayList<Texture>();
		textures_l=new ArrayList<Texture>();
		textures_r=new ArrayList<Texture>();
		
		//textures for up
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(local_directory+"u__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			textures_u.add(t);
		}
		//textures for down
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				System.out.println(i);
				t=Process_Graphics.$graphics.get_texture(local_directory+"d__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			textures_d.add(t);
		}
		//textures for left
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(local_directory+"l__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			textures_l.add(t);
		}
		//textures for right
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(local_directory+"r__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			textures_r.add(t);
		}
		
		loaded=true;
	}
	
	public void unload(){
		textures_u=null;
		textures_d=null;
		textures_l=null;
		textures_r=null;
		loaded=false;
	}
	
	//override serialization read method
	private void readObject(ObjectInputStream inputStream)throws IOException, ClassNotFoundException{
		inputStream.defaultReadObject();
		loaded=false;
	}
}
