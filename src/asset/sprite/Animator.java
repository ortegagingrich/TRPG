package asset.sprite;

import com.jme3.texture.*;
import com.jme3.scene.*;

//animator object to be attached to a dynamic entity; controls the corresponding sprite
public class Animator {
	
	private Sprite sprite; //the sprite assigned to the animator
	
	private String current_animation="idle";
	private float current_frame=1;
	private boolean playing_animation=false;
	private boolean looping_animation=false;
	
	private float speed_multiplier=1;
	
	public Animator(){
		
	}
	
	public float speed(){
		return speed_multiplier;
	}
	
	public void set_speed(float s){
		speed_multiplier=s;
	}
	
	public void set_sprite(Sprite s){
		sprite=s;
	}
	
	public void set_sprite(String s){
		//find sprite
		sprite=game.system.Process_Graphics.spriteset.get_sprite(s);
	}
	
	//play animation
	public void play_animation(String anim,boolean loop){
		//first, check to see if the animation is valid
		if(sprite.animation_valid(anim)){
			//unload old animation
			sprite.unload(current_animation);
			//set new animation
			playing_animation=true;
			looping_animation=loop;
			current_animation=anim;
			current_frame=0.5f;
		}
	}
	
	//stop animation
	public void stop_animation(){
		playing_animation=false;
		looping_animation=false;
	}
	
	//main update method
	public void update(String direction,boolean moving,Geometry main_screen,Geometry over_screen){
		//if no sprite selected, do nothing
		if(sprite==null){
			return;
		}
		
		String newanimation;
		if(moving){
			newanimation="walk";
		}else if(playing_animation){
			newanimation=current_animation;
		}else{
			newanimation="idle";
		}
		if(!newanimation.equals(current_animation)){
			current_frame=0.5f;
			sprite.unload(current_animation);
			current_animation=newanimation;
		}
		if(current_frame>sprite.get_animation_length(current_animation)+0.5f){
			if(playing_animation){
				if(looping_animation){
					current_frame=0.5f;
				}else{
					stop_animation();
				}
			}else{
				current_frame=0.5f;
			}
		}
		//set texture here
		sprite.display_frame(main_screen,over_screen,current_animation,direction,Math.round(current_frame));
		
		current_frame+=0.3f*speed_multiplier;
	}
	
}
