package game.system.graphics;

import com.jme3.scene.Spatial;

//order to be carried out on a spatial
public class Spatial_Order {
	
	public Object subject;
	public Object target;
	public String operation;
	
	public Spatial_Order(String o,Object s,Object t){
		subject=s;
		target=t;
		operation=o;
	}

}
