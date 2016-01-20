package asset.model;

import java.io.Serializable;
import java.util.ArrayList;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Geometry;
import com.jme3.light.*;

//super class for model components
public class Component implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String label; //can attach a label to a component,if wanted
	private int id;
	
	public Component(){
		label=this.type(); //label is, by default, set to the type
	}
	
	//get type
	public String type(){
		return "";
	}
	
	public int id(){ //unique id assigned to each component within a model (really just to compensate for how dumb nifty-gui can sometimes be)
		return id;
	}
	
	public void set_id(int i){
		id=i;
	}
	
	public String label(){
		return label;
	}
	
	public void set_label(String s){
		label=s;
	}
	
	//all components have give coordinate values; base methods to be overwritten:
	public float x(){
		return 0;
	}
	
	public float y(){
		return 0;
	}
	
	public float z(){
		return 0;
	}
	
	public float size_x(){
		return 0;
	}
	
	public float size_y(){
		return 0;
	}
	
	public float size_z(){
		return 0;
	}
	
	public float rot_x(){
		return 0;
	}
	
	public float rot_y(){
		return 0;
	}
	
	public float rot_z(){
		return 0;
	}
	
	public void set(float xi,float yi,float zi,float xs,float ys,float zs,float xr,float yr,float zr){
		//do nothing
	}
	
	//texture methods (to be overriden by other classes)\
	public String texture(){
		return "";
	}
	
	public float texture_u(){
		return 1;
	}
	
	public float texture_v(){
		return 1;
	}
	
	public void set_texture(String t){
		//do nothing
	}
	
	public void set_uv(float u,float v){
		//do nothing
	}
	
	//default spatial method (overriden by other classes)
	public Spatial get_spatial(){
		return new Node();
	}
	
	public Spatial get_highlighted_spatial(){
		Spatial s=this.get_spatial();
		AmbientLight l=new AmbientLight();
		l.setColor(ColorRGBA.Green.mult(3));
		s.addLight(l);
		return s;
	}
	
	//default get hitbox (none)
	public Geometry get_hitbox(){
		return null;
	}
	
	//default get light list
	public ArrayList<Light> get_light_list(){
		return new ArrayList<Light>();
	}

}
