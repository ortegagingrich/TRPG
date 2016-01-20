package game.system.graphics;


import com.jme3.scene.Spatial;

public class Geometry_Order extends Spatial_Order {
	
	public Object param1,param2,param3;
	
	public Geometry_Order(String o,Object g,Object p1,Object p2,Object p3){
		super(o,g,g);
		param1=p1;
		param2=p2;
		param3=p3;
	}

}
