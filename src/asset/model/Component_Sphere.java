package asset.model;

import java.io.Serializable;
import com.jme3.scene.Spatial;
import com.jme3.scene.Geometry;
import game.*;
import game.system.Process_Graphics;

//class of sphere-shaped components
public class Component_Sphere extends Component implements Serializable {
	public static final long serialVersionUID=1L;
	
	private float x;
	private float y;
	private float z;
	private float radius;
	private String texture;
	private float u;
	private float v;
	
	public Component_Sphere(float xi,float yi,float zi,float rad){
		super();
		set(xi,yi,zi,rad,rad,rad,0,0,0);
	}
	
	//get type
	@Override
	public String type(){
		return "Sphere";
	}
	
	//coordinate/movement methods
	@Override
	public float x(){
		return x;
	}
	
	@Override
	public float y(){
		return y;
	}
	
	@Override
	public float z(){
		return z;
	}
	
	@Override
	public float size_x(){
		return radius;
	}
	
	@Override
	public float size_y(){
		return radius;
	}
	
	@Override
	public float size_z(){
		return radius;
	}
	
	@Override
	public void set(float xi,float yi,float zi,float xwi,float ywi,float zwi,float xri,float yri,float zri){
		x=xi;
		y=yi;
		z=zi;
		radius=xwi;
	}
	
	//texture methods
	@Override
	public String texture(){
		return texture;
	}
	
	@Override
	public float texture_u(){
		return u;
	}
	
	@Override
	public float texture_v(){
		return v;
	}
	
	@Override
	public void set_texture(String t){
		texture=t;
	}
	
	@Override
	public void set_uv(float ui,float vi){
		u=ui;
		v=vi;
	}
	
	//return spatial method
	@Override
	public Spatial get_spatial(){
		//first, make a sphere geometry
		Geometry g=Process_Graphics.$graphics.make_sphere(x,y,z,radius);
		//try to set texture; if does not work, then will appear texture-less
		try{
			Process_Graphics.$graphics.set_texture(g,texture,u,v);
		}catch(Exception ex){
			System.out.println("could not load texture: "+texture);
		}
		//return the geometry
		return g;
	}
	
	//get hitbox
	@Override
	public Geometry get_hitbox(){
		Geometry g=Process_Graphics.$graphics.make_sphere(x,y,z,radius);
		//return the geometry
		return g;
	}
	
}
