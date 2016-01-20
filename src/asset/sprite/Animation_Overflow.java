package asset.sprite;

import game.system.Process_Graphics;

import java.io.Serializable;
import java.util.ArrayList;

import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

//class for animations that make use of the overlay layer to extend into neighboring tiles
public class Animation_Overflow extends Animation implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String overlay_directory;  //directory for the overlay layer
	
	//overlay textures
	private transient ArrayList<Texture> over_textures_u;
	private transient ArrayList<Texture> over_textures_d;
	private transient ArrayList<Texture> over_textures_l;
	private transient ArrayList<Texture> over_textures_r;
	
	public Animation_Overflow(String n,int l,String main_dir,String over_dir){
		super(n,l,main_dir);
		overlay_directory=over_dir;
	}
	
	//display texture to screens
	@Override
	public void display_texture(Geometry main_screen,Geometry over_screen,String direction,int frame){
		Texture main=get_texture(direction,frame);
		Texture over=get_overlay_texture(direction,frame);
		//set main screen
		game.system.Process_Graphics.$graphics.set_sprite(main_screen,main);
		//set over_screen to blank
		game.system.Process_Graphics.$graphics.set_sprite(over_screen,over);
	}
	
	//for actual sprites, use display_texture() instead
	public Texture get_overlay_texture(String direction,int frame){
		if(!loaded){
			load();
		}
		
		if(direction.equals("u")){
			return over_textures_u.get((frame%length));
		}else if(direction.equals("d")){
			return over_textures_d.get((frame%length));
		}else if(direction.equals("l")){
			return over_textures_l.get((frame%length));
		}else if(direction.equals("r")){
			return over_textures_r.get((frame%length));
		}
		return null;
	}
	
	//load/unload methods
	
	@Override
	protected void load(){
		over_textures_u=new ArrayList<Texture>();
		over_textures_d=new ArrayList<Texture>();
		over_textures_l=new ArrayList<Texture>();
		over_textures_r=new ArrayList<Texture>();
		
		//textures for up
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(overlay_directory+"u__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			over_textures_u.add(t);
		}
		//textures for down
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				System.out.println(i);
				t=Process_Graphics.$graphics.get_texture(overlay_directory+"d__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			over_textures_d.add(t);
		}
		//textures for left
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(overlay_directory+"l__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			over_textures_l.add(t);
		}
		//textures for right
		for(int i=0;i<length;i++){
			Texture t=null;
			try{
				t=Process_Graphics.$graphics.get_texture(overlay_directory+"r__"+game.Util.make_string(i,3));
			}catch(Exception ex){
				System.out.println("asset not found");
			}
			over_textures_r.add(t);
		}
		
		super.load();
	}
	
	@Override
	public void unload(){
		over_textures_u=null;
		over_textures_d=null;
		over_textures_l=null;
		over_textures_r=null;
		super.unload();
	}
	
}
