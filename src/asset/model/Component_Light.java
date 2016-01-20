package asset.model;

import com.jme3.light.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import java.util.ArrayList;
import game.*;
import game.system.Process_Graphics;

//area light component
public class Component_Light extends Component {
	public static final long serialVersionUID=1L;
	
	private float x;
	private float y;
	private float z;
	private float radius;
	private ColorRGBA color;
	
	//constructor for default light color (white)
	public Component_Light(float xi,float yi,float zi,float i,float r){
		super();
		radius=r;
		color=ColorRGBA.White.mult(i);
		set(xi,yi,zi,0,0,0,0,0,0);
	}
	
	@Override
	public String type(){
		return "Light";
	}
	
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
	public void set(float xi,float yi,float zi,float xs,float ys,float zs,float xr,float yr,float zr){
		x=xi;
		y=yi;
		z=zi;
	}
	
	//get hitbox
	@Override
	public Geometry get_hitbox(){
		Geometry g=Process_Graphics.$graphics.make_box(x,y,z,0.25f,0.25f,0.25f);
		return g;
	}
	
	//return light list
	@Override
	public ArrayList<Light> get_light_list(){
		ArrayList<Light> list=new ArrayList<Light>();
		PointLight l=new PointLight();
		l.setPosition(new Vector3f(x,y,z));
		l.setColor(color);
		l.setRadius(radius);
		list.add(l);
		return list;
	}

}
