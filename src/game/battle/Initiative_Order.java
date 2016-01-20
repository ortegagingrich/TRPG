package game.battle;

import game.map.entity.Entity_Dynamic;
import game.map.entity.mechanics.Move_Path;
import game.system.Process_Graphics;

import com.jme3.math.Vector3f;

//class of passive orders in initiative mode
public class Initiative_Order {
	
	public String type;  //type of order
	public boolean finished;
	
	private Object arg1;
	private Object arg2;
	private Object arg3;
	
	public Initiative_Order(String t,Object a1,Object a2,Object a3){
		finished=false;
		type=t;
		arg1=a1;
		arg2=a2;
		arg3=a3;
	}
	
	public void update(){
		//branch based on type
		if(type.contains("camera")){
			update_camera();
		}else if(type.contains("move")){
			update_move();
		}else{
			finished=true;
		}
	}
	
	private void update_camera(){
		if(type.contains("to")){//for panning to a target
			Vector3f camvec,tarvec,diff;
			float tarx,tary,tarz,dist;
			tarx=Process_Graphics.$graphics.camx;
			tary=Process_Graphics.$graphics.camy;
			tarz=Process_Graphics.$graphics.camz;
			camvec=new Vector3f(tarx,tary,tarz);
			tarvec=new Vector3f((Float)arg1,(Float)arg2,(Float)arg3);
			diff=tarvec.subtract(camvec);
			dist=diff.length();
			if(dist<0.5){
				tarx=(Float)arg1;
				tary=(Float)arg2;
				tarz=(Float)arg3;
				finished=true;
			}else{
				diff.normalizeLocal().multLocal(0.15f);
				tarx+=diff.x;
				tary+=diff.y;
				tarz+=diff.z;
			}
			Process_Graphics.$graphics.camx=tarx;
			Process_Graphics.$graphics.camy=tary;
			Process_Graphics.$graphics.camz=tarz;
		}else{
			finished=true;
		}
	}
	
	private void update_move(){
		if(type.contains("path")){
			Entity_Dynamic entity=(Entity_Dynamic)arg2;
			//load in orders to the entity
			if(arg1!=null){
				Move_Path path=(Move_Path)arg1;
				entity.move_queue(path);
				arg1=null;
			}
			//note: entity movement will be updated automatically with world update
			//have camera follow entity
			Process_Graphics.$graphics.camx=entity.x;
			Process_Graphics.$graphics.camy=entity.y+10;
			Process_Graphics.$graphics.camz=entity.z-15;
			//if entity's order queue is done, finish
			if(!entity.moving()){
				finished=true;
			}
		}
	}

}
