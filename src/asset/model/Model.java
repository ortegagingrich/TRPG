package asset.model;

import java.io.Serializable;
import java.util.ArrayList;
import com.jme3.scene.Node;
import com.jme3.light.*;

//class of static entity models
public class Model implements Serializable {
	public static final long serialVersionUID=1L;
	
	public String name;
	public int id;
	public String library_id="test";
	
	private ArrayList<Component> components;
	private int next_id=0;
	
	public Model(String n){
		name=n;
		components=new ArrayList<Component>();
		id=-1;
	}
	
	//component methods
	
	public ArrayList<Component> components(){
		return components;
	}
	
	public void add_component(Component c){
		components.add(c);
		c.set_id(next_id);
		next_id++;
	}
	
	public void remove_component(Component c){
		components.remove(c);
	}
	
	//create spatial model for use (always positioned at x=y=z=0)
	public Node get_spatial(){
		return get_spatial(null);
	}
	
	public Node get_spatial(Component highlight){ //highlights the given component
		//first make a node, then attach component parts
		Node n=new Node();
		for(Component c:components){
			if(c.equals(highlight)){
				n.attachChild(c.get_highlighted_spatial());
			}else{
				n.attachChild(c.get_spatial());//(safe, since not attached to scene object)
			}
		}
		return n;
	}
	
	//create hitbox
	public Node get_hitbox(){
		Node n=new Node();
		//add hitboxes of components to node
		for(Component c:components){
			if(c.get_hitbox()!=null){
				n.attachChild(c.get_hitbox());
			}
		}
		return n;
	}
	
	//create light list
	public ArrayList<Light> get_light_list(){
		ArrayList<Light> list=new ArrayList<Light>();
		for(Component c:components){
			for(Light l:c.get_light_list()){
				list.add(l);
			}
		}
		return list;
	}

}
