package game.scene;

import game.system.*;
import game.gui.animation_test.*;
import asset.sprite.*;
import com.jme3.light.*;
import com.jme3.math.ColorRGBA;

import com.jme3.scene.Geometry;

public class Scene_AnimationTest extends Scene {
	
	private HUD_Animation_Test hud;
	
	private String current;
	private byte frameno;
	private Geometry screen;
	private Geometry over_screen;
	
	private Sprite selected_sprite=null;
	
	public Scene_AnimationTest(){
		//clear root node
		game.system.graphics.Graphics.graphics_manager.detachAllChildren(Process_Main.rootNode);
		
		//make hud panel
		hud=new HUD_Animation_Test();
		
		current="enemy";
		//make a scene light
		Light l=new AmbientLight();
		l.setColor(ColorRGBA.White.mult(1f));
		game.system.graphics.Graphics.graphics_manager.addRootLight(l);
		
		//make screen
		screen=Process_Graphics.$graphics.draw_box(Process_Main.rootNode,0,0,0,0.41f,1f,0.0001f);
		over_screen=Process_Graphics.$graphics.draw_box(Process_Main.rootNode,2f,0,0.01f,1f,1f,0.0001f);
		//set camera
		Process_Graphics.$graphics.camx=1.1f;
		Process_Graphics.$graphics.camz=-8;
		Process_Graphics.$graphics.camy=5;
		
		//old stuff:
		/*//make a box
		box=Process_Graphics.$graphics.draw_box(Process_Main.rootNode,-8,1,0,0.5f,1f,0.0001f);
		//make floor
		Process_Graphics.$graphics.draw_box(Process_Main.rootNode,0,-1,0,1000,0.0001f,1000);
		//set initial animation
		frameno=1;
		Process_Graphics.$graphics.set_sprite(box,current,Integer.toString(frameno));*/
		
		
		loopcount=0;
	}
	
	public void update(){
		//update hud
		hud.update();
		
		//update action
		update_action();
		
		//animation update
		update_animation();
		
		loopcount++;
		
	}
	
	//update action
	private void update_action(){
		Input input=Process_Main.$input;
		
		//set sprite direction if wasd pressed
		if(input.trigger("W")){
			face="u";
		}
		if(input.trigger("A")){
			face="l";
		}
		if(input.trigger("S")){
			face="d";
		}
		if(input.trigger("D")){
			face="r";
		}
		
		//toggle animator speed
		if(input.trigger("Add")){
			animator.set_speed(animator.speed()*2);
		}
		if(input.trigger("Subtract")){
			animator.set_speed(animator.speed()*0.5f);
		}
		
	}
	
	//what appears below here is what will eventually appear in the Entity_Dynamic class
	
	//emulate other Entity_Dynamic methods/fields
	private boolean moving(){
		//temporary, always idle
		return true;
	}
	
	public String face="d";
	
	//to add
	public Animator animator=new Animator();
	
	private void update_animation(){
		
		//update the animator
		animator.update(face,moving(),screen,over_screen);
		
		
		//old stuff
		/*frameno+=1;
		if(frameno>8){
			frameno=1;
		}
		Process_Graphics.$graphics.set_sprite(box,current,"l"+Integer.toString(frameno));
		//box.move(0.4f,0,0);
		//game.system.graphics.Graphics.graphics_manager.move(box,0.4f,0,0);
		*/
	}

}
