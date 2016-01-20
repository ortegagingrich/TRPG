package asset.model;

import java.io.Serializable;
import com.jme3.scene.*;
import game.*;
import game.system.Process_Graphics;

public class Component_Quad extends Component implements Serializable {
	public static final long serialVersionUID=1L;
	
	private float x;
	private float y;
	private float z;
	private float w;
	private float h;
	private float xr;
	private float yr;
	private float zr;
	private String texture;
	private float u;
	private float v;
	
	public Component_Quad(float xi,float yi,float zi,float w,float h){
		super();
		set(xi,yi,zi,w,0,h,0,0,0);
	}
	
	//get type
	@Override
	public String type(){
		return "Quad";
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
		return w;
	}
	
	@Override
	public float size_z(){
		return h;
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
		w=xwi;
		h=zwi;
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
	public void set_uv(float tu,float tv){
		u=tu;
		v=tv;
	}
	
	//return spatial method
	@Override
	public Spatial get_spatial(){
		//first, make a quad
		Geometry g=Process_Graphics.$graphics.make_quad(x,y,z,w,h);
		//try to set a texture; if does not work, then will appear texture-less
		try{
			Process_Graphics.$graphics.set_texture(g,texture,u,v);
		}catch(Exception ex){
			System.out.println("could not load texture: "+texture);
		}
		//rotate to correct angle
		g.rotate((float)Math.toRadians(xr),(float)Math.toRadians(yr),(float)Math.toRadians(zr));
		//return the geometry
		return g;
	}
	
	//return hitbox
	@Override
	public Geometry get_hitbox(){
		//make quad
		Geometry g=Process_Graphics.$graphics.make_quad(x,y,z,w,h);
		//rotate to correct angle
		g.rotate((float)Math.toRadians(xr),(float)Math.toRadians(yr),(float)Math.toRadians(zr));
		//return the geometry
		return g;
	}
	
}
