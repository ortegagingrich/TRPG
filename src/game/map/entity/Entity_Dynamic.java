package game.map.entity;

import game.Game;
import game.map.Map;
import game.map.entity.mechanics.Move_Path;
import game.scene.Scene_Explore;
import game.scene.Scene_Initiative;
import game.scene.Scene_WorldEdit;
import game.system.Process_Graphics;
import game.system.Process_Main;
import game.system.graphics.Graphics;
import io.game_dump.Dump_Entity;

import java.util.ArrayList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.math.*;
import com.jme3.light.*;

public class Entity_Dynamic extends Entity {
	
	public Map map;
	
	public int target_x;
	public int target_z;
	
	
	private ArrayList<Entity_Dynamic> aware_of;  //entities of which the current entity is aware
	private ArrayList<Entity_Dynamic> seen_by;   //entities which are aware of the current entity
	private Entity_Dynamic concentrating_on;     //entity on which the current entity is concentrating
	private ArrayList<Entity_Dynamic> concentrated_on;  //entities concentrating on the current entity
	
	private float oldx;
	private float oldy;
	private float oldz;
	private int oldtarget_x;
	private int oldtarget_z;
	private float speed;
	private String order;
	private ArrayList<String> orders; //orders governing motion/direction
	
	protected String face; //string denoting the direction the character is facing: u,d,l,r
	private String oldface;
	protected byte frame; //the current animation frame number
	private byte oldframe;
	
	private Geometry screen;  //the screen onto which the sprite is projected
	private Geometry crosshairs;  //crosshairs 
	
	public Entity_Dynamic(float xi,float yi,float zi,Map m,float s){
		super(xi,yi,zi);
		oldx=xi;
		oldy=yi;
		oldz=zi;
		target_x=Math.round(xi);
		target_z=Math.round(zi);
		oldtarget_x=target_x;
		oldtarget_z=target_z;
		speed=s;
		order="";
		orders=new ArrayList<String>();
		map=m;
		aware_of=new ArrayList<Entity_Dynamic>();
		seen_by=new ArrayList<Entity_Dynamic>();
		concentrated_on=new ArrayList<Entity_Dynamic>();
	}
	
	//set position
	@Override
	public void set_position(float xi,float yi,float zi){
		super.set_position(xi,yi,zi);
		target_x=Math.round(xi);
		target_z=Math.round(zi);
	}
	
	//occupies tile
	@Override
	public boolean occupies_tile(){
		return true;
	}
	
	//method to get name
	public String name(){
		return "";
	}
	
	
	
	//update methods
	public void update(){
		
		//if not in battle, update path
		if(Process_Main.$scene instanceof Scene_Explore){
			update_path();
		}else if(Process_Main.$scene instanceof Scene_Initiative){
			//if in battle and coordinates are integers, stop movement
			if((0.01f>Math.abs(x-Math.round(x)))&&(0.01f>Math.abs(z-Math.round(z)))){
				set_target(Math.round(x),Math.round(z));
			}
		}
		
		//if not in edit mode, update awareness and order
		if(!(Process_Main.$scene instanceof Scene_WorldEdit)){
			update_awareness();
			update_order();
		}
		
		//update model
		update_model();
	}
	
	//update awareness of other dynamic entities
	private void update_awareness(){
		long lagmeter=System.currentTimeMillis();
		
		//cycle through entities of which this entity might be aware
		ArrayList<Entity_Dynamic> biglist=new ArrayList<Entity_Dynamic>();
		biglist.addAll(map.enemies);
		biglist.addAll(map.players());
		
		//take out all entities not in the big list and not being concentrated on
		ArrayList<Entity_Dynamic> l=new ArrayList<Entity_Dynamic>();
		for(Entity_Dynamic e:aware_of){
			if(!biglist.contains(e)){
				l.add(e);
			}
		}
		for(Entity_Dynamic e:l){
			make_forget_about(e);
		}
		l.clear();
		for(Entity_Dynamic e:seen_by){
			if(!biglist.contains(e)){
				l.add(e);
			}
		}
		for(Entity_Dynamic e:l){
			e.make_forget_about(this);
		}
		
		//add in appropriate elements from biglist
		for(Entity_Dynamic e:biglist){
			//if this entity can be seen
			if(facing_wide(e.x,e.z)&&map.visible_from(this,e,visibility())){
				make_aware_of(e);
			}else if(!map.visible_from(this,e,visibility())){// if vision completely impeded
				//if can forget about unseen entity, forget
				if(can_forget_about(e)){
					make_forget_about(e);
				}
			}
		}
		
		lagmeter-=System.currentTimeMillis();
		if(Game.print_lag){
			System.out.println("Awareness Lag: "+Long.toString(lagmeter));
		}
	}
	
	//awareness handling
	
	public int visibility(){
		return 8;
	}
	
	public Entity_Dynamic concentrating_on(){
		return concentrating_on;
	}
	
	public ArrayList<Entity_Dynamic> aware_of(){
		return aware_of;
	}
	
	public ArrayList<Entity_Dynamic> seen_by(){
		return seen_by;
	}
	
	public void make_aware_of(Entity_Dynamic e){
		if(!aware_of.contains(e)){
			aware_of.add(e);
			e.seen_by.add(this);
		}
	}
	
	public void make_concentrate_on(Entity_Dynamic e){
		make_aware_of(e);
		make_lose_concentration();
		concentrating_on=e;
		e.concentrated_on.add(this);
	}
	
	public void make_lose_concentration(){
		if(concentrating_on!=null){
			concentrating_on.concentrated_on.remove(this);
		}
		concentrating_on=null;
	}
	
	public void make_forget_about(Entity_Dynamic e){
		if(e==concentrating_on){
			make_lose_concentration();
		}
		if(aware_of.contains(e)){
			aware_of.remove(e);
			e.seen_by.remove(this);
		}
	}
	
	public boolean can_forget_about(Entity_Dynamic e){
		return e!=concentrating_on;
	}
	
	//update pathfinding to target
	private void update_path(){
		//check if target has not changed and path is still working, do nothing
		if(target_x==oldtarget_x&&target_z==oldtarget_z){
			if(moving()||(order==""&&orders.size()>0)||(Math.round(x)==target_x&&Math.round(z)==target_z)){
				return;
			}
		}
		
		oldtarget_x=target_x;
		oldtarget_z=target_z;
		
		//find a new path
		find_path(target_x,target_z);
	}
	
	public void update_order(){  //temporary: make private void later
		String s=order;
		float unit=speed;
		if(order.length()!=0){
			unit=0.05f*Math.round((20*speed/Math.sqrt(order.length())));
		}
		
		if(order.contains("u")){
			z+=unit;
		}
		if(order.contains("d")){
			z-=unit;
		}
		if(order.contains("l")){
			x+=unit;
		}
		if(order.contains("r")){
			x-=unit;
		}
		
		// if coordinates are integers, advance to next order or stop movement
		if((0.01f>Math.abs(x-Math.round(x)))&&(0.01f>Math.abs(z-Math.round(z)))){
			order="";
			if(orders.size()>0){
				//get next order if tile available, otherwise clear orders
				if(set_order(orders.get(0))){
					orders.remove(0);
				}else{
					orders.clear();
				}
			}
		}
		
		//face correct direction (towards concentration target, if set, otherwise in the move direction)
		if(s==""&&concentrating_on!=null){
			face_towards(concentrating_on);
		}else if(s.contains("l")){
			face="l";
		}else if(s.contains("r")){
			face="r";
		}else if(s.contains("u")){
			face="u";
		}else if(s.contains("d")){
			face="d";
		}
	}
	
	protected void update_model(){
		//update animation; very important that this is done before the model is moved!
		update_animation();
		//update crosshairs
		update_crosshairs();
		
		
		//relocate model (and hitbox and crosshairs, if not visible)
		Graphics.graphics_manager.move(model,x-oldx,y-oldy,z-oldz);
		Graphics.graphics_manager.move(hitbox,x-oldx,y-oldy,z-oldz);
		oldx=x;
		oldy=y;
		oldz=z;
	}
	
	private boolean crosshairs_visible;
	private void update_crosshairs(){
		//if in edit mode
		if(Process_Main.$scene instanceof Scene_WorldEdit){
			//always show crosshairs
			if(!crosshairs_visible){
				show_crosshairs();
			}
		}
		//if not in battle
		if(Process_Main.$scene instanceof Scene_Explore){
			//show crosshairs if and only if the main player is aware
			if(seen_by.contains(map.player())){
				if(!crosshairs_visible){
					show_crosshairs();
				}
			}else{
				if(crosshairs_visible){
					hide_crosshairs();
				}
			}
		}
		//if in battle
		if(Process_Main.$scene instanceof Scene_Initiative){
			//if current is defined, show crosshairs if and only if the active battler is ware; if not defined, do nothing
			if(((Scene_Initiative)Process_Main.$scene).manager.current!=null){
				if(seen_by.contains(((Scene_Initiative)Process_Main.$scene).manager.current.entity)){
					if(!crosshairs_visible){
						show_crosshairs();
					}
				}else{
					if(crosshairs_visible){
						hide_crosshairs();
					}
				}
			}
		}
	}
	
	private void show_crosshairs(){
		Graphics.graphics_manager.attachChild((Node)model,crosshairs);
		crosshairs_visible=true;
	}
	
	private void hide_crosshairs(){
		Graphics.graphics_manager.detachChild((Node)model,crosshairs);
		crosshairs_visible=false;
	}
	
	//update animation; warning: temporary override by Player class
	private byte animloop=0;
	protected void update_animation(){
		//determine animation frame
		//check if moving
		if(moving()){
			//if moving, but frame still stationary, set at starting frame (4)
			if(frame==0){
				frame=3;
			}
			//check if 5 frames have passed for animation frame update
			if(animloop>5){//set back to 5
				frame++;
				if(frame>8){
					frame=1;
				}
				animloop=0;
			}
			animloop++;
		}else{
			//if not moving, just set frame as 0;
			frame=0;
		}
		//change sprite texture based on facing, if the sprite is facing a different direction
		if(face!=oldface||frame!=oldframe){
			//stress test attempt
			for(int j=1;j<=1;j++){
				try{Process_Graphics.$graphics.set_sprite(screen,name(),face+Integer.toString(frame));}catch(Exception ex){System.out.println("animation flub");};
			}
			
		}
		oldface=face;
		oldframe=frame;
	}
	
	
	public void retrieve_model(){
		model=new Node();
		//temporary; just make a quad at the location
		screen=Process_Graphics.$graphics.draw_box((Node)model,x+0.5f,y+0.92f,z+0.65f,0.5f,1,0.00001f);
		try{Process_Graphics.$graphics.set_sprite(screen,name(),"d0");}catch(Exception ex){}//ex.printStackTrace();}
		//all dynamic entities have an ambient light to help them stand out from the environment
		AmbientLight l=new AmbientLight();
		l.setColor(ColorRGBA.White.mult(0.3f));
		model.addLight(l);
		//make crosshairs
		crosshairs=Process_Graphics.$graphics.make_sphere(x+0.5f,y+2,z+0.5f,0.15f);
		//make default hitbox; should be overriden by subclass
		hitbox=Process_Graphics.$graphics.make_box(x+0.5f,y+0.5f,z+0.5f,0.5f,0.5f,0.5f);
		//set initial animation frame
		face="d";
		oldface=face;
		frame=0;
		oldframe=frame;
		animloop=0;
	}
	
	//movement/position methods:
	
	//methods to retrieve face
	public String face(){
		return face;
	}
	
	//method to check if the entity is facing given coordinates (narrow: 90 degree window; wide: 180 degree window)
	public boolean facing_narrow(float xtest,float ztest){
		float xdiff=xtest-x;
		float zdiff=ztest-z;
		if(face.contains("u")){
			return (zdiff>Math.abs(xdiff));
		}else if(face.contains("d")){
			return (zdiff<-Math.abs(xdiff));
		}else if(face.contains("l")){
			return (xdiff>Math.abs(zdiff));
		}else if(face.contains("r")){
			return (xdiff<-Math.abs(zdiff));
		}
		//if all else fails, return false
		return false;
	}
	
	public boolean facing_wide(float xtest,float ztest){
		float xdiff=xtest-x;
		float zdiff=ztest-z;
		if(face.contains("u")){
			return (zdiff>0);
		}else if(face.contains("d")){
			return (zdiff<0);
		}else if(face.contains("l")){
			return (xdiff>0);
		}else if(face.contains("r")){
			return (xdiff<0);
		}
		//if all else fails, return false
		return false;
	}
	
	public void face_up(){
		face="u";
	}
	
	public void face_down(){
		face="d";
	}
	
	public void face_left(){
		face="l";
	}
	
	public void face_right(){
		face="r";
	}
	
	public void face_set(String s){
		face=s;
	}
	
	public void face_towards(int xt,int zt){
		float xdiff=xt-x;
		float zdiff=zt-z;
		
		if(xdiff>Math.abs(zdiff)){
			face_left();
		}
		if(-xdiff>Math.abs(zdiff)){
			face_right();
		}
		if(zdiff>Math.abs(xdiff)){
			face_up();
		}
		if(-zdiff>Math.abs(xdiff)){
			face_down();
		}
	}
	
	public void face_towards(Entity e){
		face_towards(Math.round(e.x),Math.round(e.z));
	}
	
	public boolean moving(){
		return (oldx!=x||oldy!=y||oldz!=z||!order.equals("")||orders.size()!=0);
	}
	
	//set target
	public void set_target(int xi,int zi){
		target_x=xi;
		target_z=zi;
	}
	
	public void move(String dir){
		if(dir==""){
			return;
		}
		int tarx,tarz;
		tarx=Math.round(x);
		tarz=Math.round(z);
		if(dir.contains("u")){
			tarz++;
		}
		if(dir.contains("d")){
			tarz--;
		}
		if(dir.contains("l")){
			tarx++;
		}
		if(dir.contains("r")){
			tarx--;
		}
		target_x=tarx;
		target_z=tarz;
	}
	
	//boolean returned indicates if move was successful
	private boolean set_order(String dir){
		if(order.equals("")){
			//check to make sure potential tile is available
			int ox,oz,px,pz;
			ox=Math.round(x);
			oz=Math.round(z);
			px=ox;
			pz=oz;
			if(dir.contains("u")){
				pz++;
			}
			if(dir.contains("d")){
				pz--;
			}
			if(dir.contains("l")){
				px++;
			}
			if(dir.contains("r")){
				px--;
			}
			//if tile is available, set as target
			if(map.tile_open(px,pz)){
				order=dir;
				return true;
			}
		}
		return false;
	}
	
	//allows for queueing, but doesn't immediately check for tile availability
	private void move_queue(String dir){
		orders.add(dir);
	}
	
	public void move_queue(Move_Path p){//allows direct input of move path
		for(Vector2f step:p.moves){
			String dir="";
			if(step.x==0){
				if(step.y>0){
					dir="u";
				}else{
					dir="d";
				}
			}else{
				if(step.x>0){
					dir="l";
				}else{
					dir="r";
				}
			}
			move_queue(dir);
		}
	}
	
	//lists available moves for a given number of steps
	
	public ArrayList<Move_Path> available_moves(int iterations){
		ArrayList<Move_Path> availables=new ArrayList<Move_Path>();
		
		//make direction vectors:
		ArrayList<Vector2f> directions=new ArrayList<Vector2f>();
		directions.add(new Vector2f(0,1));
		directions.add(new Vector2f(0,-1));
		directions.add(new Vector2f(1,0));
		directions.add(new Vector2f(-1,0));
		
		//make list of available tiles and add current tile
		availables=new ArrayList<Move_Path>();
		availables.add(new Move_Path(new Vector2f(x,z)));
		ArrayList<Vector2f> used=new ArrayList<Vector2f>();
		used.add(new Vector2f(x,z));
		
		//iterate moves
		for(int i=1;i<=iterations;i++){
			ArrayList<Move_Path> toadd=new ArrayList<Move_Path>();
			for(Move_Path tile:availables){
				for(Vector2f dir:directions){
					Move_Path pot=new Move_Path(tile,dir);
					if(map.tile_open(Math.round(pot.destination.x),Math.round(pot.destination.y))&&(!used.contains(pot.destination))){
						toadd.add(pot);
						used.add(pot.destination);
					}
				}
			}
			availables.addAll(toadd);
		}
		
		return availables;
	}
	
	//attempts to find an open path to the given target and takes first move step, if a path can be found
	public void find_path(int tarx,int tarz){
		//if already at correct location, do nothing
		if(Math.round(x)==tarx&&Math.round(z)==tarz){
			return;
		}
		
		long lagmeter=System.currentTimeMillis();
		
		Move_Path p=null;
		//look for the shortest path in available paths
		int length=this.taxi_distance_from(tarx,tarz)+1;
		for(Move_Path m:available_moves(length)){
			if(Math.round(m.destination.x)==tarx&&Math.round(m.destination.y)==tarz){
				p=m;
			}
		}
		//if a path is found, set it to move commands
		if(p!=null){
			move_queue(p);
		}
		
		lagmeter-=System.currentTimeMillis();
		if(Game.print_lag){
			System.out.println("Pathfinding Lag: "+Long.toString(lagmeter));
		}
		
	}
	
	//dispose method; only for actual removal from map (e.g. defeated)
	
	public void dispose(){
		//make all other entities forget about it
		for(Entity_Dynamic e:(ArrayList<Entity_Dynamic>)seen_by.clone()){
			e.make_forget_about(this);
		}
		//make model disappear
		Graphics.graphics_manager.detachChild(model.getParent(),model);
	}
	
	//read/write methods; to ensure that proper old coordinates are set
	@Override
	public void read(Dump_Entity de){
		oldx=de.x;
		oldy=de.y;
		oldz=de.z;
		super.read(de);
	}

}
