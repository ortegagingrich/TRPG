package game.system.graphics;

import game.system.Process_Main;

import java.util.ArrayList;
import jme3tools.optimize.GeometryBatchFactory;
import com.jme3.math.*;
import com.jme3.light.*;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.Bucket;

//scene manager; handles operations on scene objects; run by graphics thread to avoid conflicts
public class Graphics_Manager {
	
	private ArrayList<Spatial_Order> orders;
	
	private boolean safe_modify;
	
	public Graphics_Manager(){
		orders=new ArrayList<Spatial_Order>();
		safe_modify=true;
	}
	
	public void update(){
		//execute orders
		safe_modify=false;
		try{Thread.sleep(1);}catch(Exception ex){};
		for(Spatial_Order order:orders){
			try{execute(order);}catch(Exception ex){};
		}
		orders.clear();
		safe_modify=true;
	}
	
	//clears all outstanding orders
	public void clear(){
		wait_until_safe();
		orders.clear();
	}
	
	private void execute(Spatial_Order order){
		if(order.operation=="detachAllChildren"){
			((Node)order.subject).detachAllChildren();
		}
		if(order.operation=="detachChild"){
			((Node)order.subject).detachChild((Spatial)order.target);
		}
		if(order.operation=="attachChild"){
			((Node)order.subject).attachChild((Spatial)order.target);
		}
		if(order.operation=="addLight"){
			((Spatial)order.subject).addLight((Light)((Geometry_Order)order).param1);
		}
		if(order.operation=="removeLight"){
			((Spatial)order.subject).removeLight((Light)((Geometry_Order)order).param1);
		}
		if(order.operation=="setColor"){
			((Light)order.subject).setColor((ColorRGBA)((Geometry_Order)order).param1);
		}
		if(order.operation=="setDirection"){
			((DirectionalLight)order.subject).setDirection((Vector3f)((Geometry_Order)order).param1);
		}
		if(order.operation=="move"){
			Geometry_Order go=(Geometry_Order)order;
			((Spatial)go.subject).move((Float)go.param1,(Float)go.param2,(Float)go.param3);
		}
		if(order.operation=="rotate"){
			Geometry_Order go=(Geometry_Order)order;
			((Spatial)go.subject).rotate((Float)go.param1,(Float)go.param2,(Float)go.param3);
		}
		if(order.operation=="setMaterial"){
			Geometry_Order go=(Geometry_Order)order;
			((Spatial)go.subject).setMaterial((Material)go.param1);
		}
		if(order.operation=="allowTransparency"){
			Geometry_Order go=(Geometry_Order)order;
			((Spatial)go.subject).setQueueBucket(Bucket.Transparent);
		}
		if(order.operation=="optimize"){
			Geometry_Order go=(Geometry_Order)order;
			GeometryBatchFactory.optimize((Node)go.subject);
		}
	}
	
	private void wait_until_safe(){
		while(!safe_modify){
			//loop until safe to modify;
			try{
				Thread.sleep(1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void detachAllChildren(Node n){
		wait_until_safe();
		orders.add(new Spatial_Order("detachAllChildren",n,n));
	}
	
	public void detachChild(Node n,Spatial t){
		wait_until_safe();
		orders.add(new Spatial_Order("detachChild",n,t));
	}
	
	public void attachChild(Node n,Spatial t){
		wait_until_safe();
		orders.add(new Spatial_Order("attachChild",n,t));
	}
	
	public void addLight(Node n,Light l){
		wait_until_safe();
		orders.add(new Geometry_Order("addLight",n,l,null,null));
	}
	
	public void removeLight(Node n,Light l){
		wait_until_safe();
		orders.add(new Geometry_Order("removeLight",n,l,null,null));
	}
	
	public void addRootLight(Light l){
		wait_until_safe();
		orders.add(new Geometry_Order("addLight",Process_Main.rootNode,l,null,null));
	}
	
	public void removeRootLight(Light l){
		wait_until_safe();
		orders.add(new Geometry_Order("removeLight",Process_Main.rootNode,l,null,null));
	}
	
	public void setColor(Light l,ColorRGBA c){
		wait_until_safe();
		orders.add(new Geometry_Order("setColor",l,c,null,null));
	}
	
	public void setDirection(DirectionalLight l,Vector3f v){
		wait_until_safe();
		orders.add(new Geometry_Order("setDirection",l,v,null,null));
	}
	
	public void move(Spatial s,float x,float y,float z){
		wait_until_safe();
		orders.add(new Geometry_Order("move",s,x,y,z));
	}
	
	public void rotate(Spatial s,float xr,float yr,float zr){
		wait_until_safe();
		orders.add(new Geometry_Order("rotate",s,xr,yr,zr));
	}
	
	public void setMaterial(Spatial s,Material m){
		wait_until_safe();
		orders.add(new Geometry_Order("setMaterial",s,m,null,null));
	}
	
	public void allowTransparency(Spatial s){
		wait_until_safe();
		orders.add(new Geometry_Order("allowTransparency",s,null,null,null));
	}
	
	public void optimize(Node n){
		wait_until_safe();
		orders.add(new Geometry_Order("optimize",n,null,null,null));
	}

}
