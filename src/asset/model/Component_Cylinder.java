package asset.model;

import java.io.Serializable;
import com.jme3.scene.Spatial;
import com.jme3.scene.Geometry;
import game.*;
import game.system.Process_Graphics;

//class of cylinder-shaped components
public class Component_Cylinder extends Component implements Serializable {
	public static final long serialVersionUID=1L;
	
	private float x;
	private float y;
	private float z;
	private float radius;
	private float height;
	private float xr;
	private float yr;
	private float zr;
	private String texture;
	private float u;
	private float v;
	
	public Component_Cylinder(float xi,float yi,float zi,float rad,float h){
		super();
		set(xi,yi,zi,rad,h,rad,0,0,0);
	}
	
	//get type
	@Override
	public String type(){
		return "Cylinder";
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
		return height;
	}
	
	@Override
	public float size_z(){
		return radius;
	}
	
	@Override
	public float rot_x(){
		return xr;
	}
	
	@Override
	public float rot_y(){
		return yr;
	}
	
	@Override
	public float rot_z(){
		return zr;
	}
	
	@Override
	public void set(float xi,float yi,float zi,float xwi,float ywi,float zwi,float xri,float yri,float zri){
		x=xi;
		y=yi;
		z=zi;
		radius=xwi;
		height=ywi;
		xr=xri;
		yr=yri;
		zr=zri;
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
		//first, make a cylinder geometry
		Geometry g=Process_Graphics.$graphics.make_cylinder(x,y,z,radius,height);
		//try to set texture; if does not work, then will appear texture-less
		try{
			Process_Graphics.$graphics.set_texture(g,texture,u,v);
		}catch(Exception ex){
			System.out.println("could not load texture: "+texture);
		}
		//rotate the geometry according to set rotations
		g.rotate((float)Math.toRadians(xr),(float)Math.toRadians(yr),(float)Math.toRadians(zr));
		//return the geometry
		return g;
	}
	
	//get hitbox
	@Override
	public Geometry get_hitbox(){
		//first, make a cylinder geometry
		Geometry g=Process_Graphics.$graphics.make_cylinder(x,y,z,radius,height);
		//rotate the geometry according to set rotations
		g.rotate((float)Math.toRadians(xr),(float)Math.toRadians(yr),(float)Math.toRadians(zr));
		//return the geometry
		return g;
	}
	
}
