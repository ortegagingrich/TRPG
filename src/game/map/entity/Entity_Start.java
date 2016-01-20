package game.map.entity;

import game.system.Process_Graphics;

import com.jme3.scene.Node;

//entity for start position
public class Entity_Start extends Entity {
	
	public Entity_Start(int x,int y,int z){
		super(x,y,z);
		retrieve_model();
	}
	
	@Override
	public void retrieve_model(){
		model=new Node();
		Process_Graphics.$graphics.draw_box((Node)model,x+0.5f,y+1,z+0.5f,0.5f,1f,0.5f);
	}

}
