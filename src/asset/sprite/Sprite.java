package asset.sprite;

import java.io.Serializable;
import com.jme3.scene.*;
import java.util.*;
import com.jme3.texture.*;

//sprite data class
public class Sprite implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String name;  //this is also used for importing textures; do not change after instance is created
	public String name(){
		return name;
	}
	
	private String category="";
	
	public String category(){
		return category;
	}
	
	private String local_directory;
	
	//main animations
	private Animation idle_animation;
	private Animation walk_animation;
	
	//other animations
	private ArrayList<Animation> other_animations;
	
	public Sprite(String n,int idle_length,int walk_length){
		name=n;
		local_directory="Sprite/"+name+"/";
		//make main animations
		idle_animation=new Animation("idle",idle_length,local_directory);
		walk_animation=new Animation("walk",walk_length,local_directory);
		//make list for other animations
		other_animations=new ArrayList<Animation>();
	}
	
	public Sprite(String n,String c,int idle_length,int walk_length){
		name=n;
		local_directory="Sprite/"+name+"/";
		//make main animations
		idle_animation=new Animation("idle",idle_length,local_directory);
		walk_animation=new Animation("walk",walk_length,local_directory);
		//make list for other animations
		other_animations=new ArrayList<Animation>();
		//set category
		category=c;
	}
	
	//make a new animation
	public void add_animation(String n,int length){
		other_animations.add(new Animation(n,length,local_directory));
	}
	
	//private get animation
	private Animation get_animation(String anim){
		if(anim.equals("idle")){
			return idle_animation;
		}else if(anim.equals("walk")){
			return walk_animation;
		}else{
			for(Animation a:other_animations){
				if(anim.equals(a.name())){
					return a;
				}
			}
			return null;
		}
	}
	
	//check if an animation is valid
	public boolean animation_valid(String anim){
		return get_animation(anim)!=null;
	}
	
	//retrieve animation length
	public int get_animation_length(String anim){
		Animation a=get_animation(anim);
		if(a==null){
			return 1;
		}else{
			return a.length();
		}
	}
	
	//retrieve animation images (note: usually, display_frame() should be used instead)
	public Texture get_frame(String anim,String dir,int frame){
		Animation a=get_animation(anim);
		if(a==null){
			return idle_animation.get_texture(dir,frame);
		}else{
			return a.get_texture(dir,frame);
		}
	}
	
	//display an animation on a character screen
	public void display_frame(Geometry main_screen,Geometry over_screen,String anim,String dir,int frame){
		Animation a=get_animation(anim);
		if(a==null){
			idle_animation.display_texture(main_screen,over_screen,dir,frame);
		}else{
			a.display_texture(main_screen,over_screen,dir,frame);
		}
	}
	
	//unload an animation
	public void unload(String anim){
		Animation a=get_animation(anim);
		if(a!=null){
			a.unload();
		}
	}
	
	//unload all animations
	public void unload_all(){
		idle_animation.unload();
		walk_animation.unload();
		for(Animation a:other_animations){
			a.unload();
		}
	}
	
}
