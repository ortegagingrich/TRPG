package game.map.entity;

import asset.model.*;

import com.jme3.light.*;
import game.system.Process_Graphics;
import game.system.graphics.Graphics;
import io.game_dump.Dump_Entity_Static;

import java.util.ArrayList;

//class of static entities; basically scenery objects, but with parameters, such as height; created a priori contained in map
public class Entity_Static extends Entity {
	
	public boolean impedable;  //denotes whether this entity's hitbox impedes vision
	
	private int blueprint_id;
	private String library_id;
	private float yrot;
	
	public Entity_Static(int xi,int yi,int zi,float yr,int bi,String li){
		super(xi,yi,zi);
		blueprint_id=bi;
		library_id=li;
		yrot=yr;
		retrieve_model();
	}
	
	
	public void dispose(){
		//remove lights
		hide_lights();
	}
	
	@Override
	public void retrieve_model(){
		try{
			Model blueprint=Process_Graphics.get_model(library_id,blueprint_id);
			model=blueprint.get_spatial();
			//move the model to the correct location
			Graphics.graphics_manager.move(model,x,y,z);
			//rotate it to the correct orientation
			model.rotate(0,(float)Math.toRadians(yrot),0);
			//get light list
			lights=blueprint.get_light_list();
			//move lights to the correct location and add to scene
			for(Light l:lights){
				if(l instanceof PointLight){
					PointLight p=(PointLight)l;
					p.setPosition(p.getPosition().add(coordinates()));
				}
				if(l instanceof SpotLight){
					SpotLight s=(SpotLight)l;
					s.setPosition(s.getPosition().add(coordinates()));
				}
			}
			//show lights
			show_lights();
			//get the model's hitbox
			hitbox=blueprint.get_hitbox();
			Graphics.graphics_manager.move(hitbox,x,y,z);
			//hitbox=Process_Graphics.$graphics.make_box(x+0.5f,y+0.5f,z+0.5f,0.5f,0.5f,0.5f);
			impedable=true; //by defaut
		}catch(Exception ex){
			ex.printStackTrace();
			model=Process_Graphics.$graphics.make_box(x+0.5f,y+0.5f,z+0.5f,0.5f,0.5f,0.5f);
			hitbox=Process_Graphics.$graphics.make_box(x+0.5f,y+0.5f,z+0.5f,0.5f,0.5f,0.5f);
			impedable=true;
		}
	}
	
	public void write(Dump_Entity_Static d){
		d.blueprint_id=blueprint_id;
		d.library_id=library_id;
		d.yrot=yrot;
		retrieve_model();
		super.write(d);
	}
	
	public void read(Dump_Entity_Static d){
		blueprint_id=d.blueprint_id;
		library_id=d.library_id;
		yrot=d.yrot;
		retrieve_model();
		super.read(d);
	}

}
