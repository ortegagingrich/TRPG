package game.scene;

import game.battle.Battle_Manager;
import game.battle.ai.AI_Action;
import game.battle.battler.Battler;
import game.battle.battler.Character;
import game.battle.battler.Enemy;
import game.gui.in_game.HUD_Initiative;
import game.map.entity.Entity;
import game.map.entity.Entity_Enemy;
import game.map.entity.Entity_Player;
import game.map.entity.mechanics.Move_Path;
import game.system.Process_Graphics;
import game.system.Process_Main;


public class Scene_Initiative extends Scene {
	
	public World world;
	public Battle_Manager manager;
	public Entity camera_target;
	public HUD_Initiative hud;
	
	public Scene_Initiative(Scene_Explore s){
		super();
		//pause interpreter processing
		Process_Main.quest.interpreter.pause_all();
		//switch hud to appropriate screen
		//for later once hud issues are resolved: hud.switch_initiative();
		//set world
		world=s.world;
		manager=new Battle_Manager(world.map);
		camera_target=world.map.player();   //set player as camera target
		//show grid
		world.grid.show();
		//make battle hud
		hud=new HUD_Initiative(manager);
	}
	
	public void update(){
		
		super.update();
		
		//update world
		world.update();
		
		//if any orders are outstanding, update them
		if(manager.orders.size()>0){
			manager.update_order();
			return;
		}
		
		//update battle manager
		manager.update();  //note: gameover may occur at this step
		
		//update camera
		update_camera();
		
		//update hud
		hud.update();
		
		//otherwise, branch based on mode of battle manager
		if(manager.mode=="start"){
			update_start();
		}else if(manager.mode=="start set"){
			update_set();
		}else if(manager.mode=="next"){
			update_next();
		}else if(manager.mode=="turn start"){
			update_turn_start();
		}else if(manager.mode=="turn main"){
			update_turn_main();
		}else if(manager.mode=="turn move"){
			update_turn_move();
		}else if(manager.mode=="turn select"){
			update_turn_select();
		}else if(manager.mode=="turn face"){
			update_turn_face();
		}else if(manager.mode=="enemy move1"){
			update_enemy_move1();
		}else if(manager.mode=="enemy action"){
			update_enemy_action();
		}else if(manager.mode=="enemy move2"){
			update_enemy_move2();
		}else if(manager.mode=="enemy face"){
			update_enemy_face();
		}
		
		//don't add anything here
	}
	
	private void update_camera(){
		//temporary: set camera fixed relative to target
		Process_Graphics.$graphics.camx=camera_target.x;
		Process_Graphics.$graphics.camz=camera_target.z-15;
		Process_Graphics.$graphics.camy=camera_target.y+10;
	}
	
	//update methods
	private void update_start(){
		//temporary; just start "next" mode
		manager.mode="next";
	}
	
	private void update_next(){
		//choose next battler
		manager.set_next_battler();
		
		//if next battler is an enemy that will not act, do nothing (will choose another next frame)
		if(manager.current instanceof Enemy){
			if(!((Entity_Enemy)manager.current.entity).will_act()){
				return;
			}
		}
		
		//set pan order
		camera_target=manager.current.entity;
		manager.pan_camera_to(manager.current.entity);
		
		//set to start turn mode and reset movement
		manager.mode="turn start";
		manager.moved=false;
		manager.acted=false;
	}
	
	private void update_turn_start(){
		//temporary, wait 1 seconds
		loopcount++;
		if(loopcount<60){
			return;
		}
		loopcount=0;
		
		if(manager.current instanceof Character){  //if the current battler is a playable character
			//start main turn mode
			manager.mode="turn main";
		}else if(manager.current instanceof Enemy){   //if the current battler is an enemy
			//start AI move phase 1
			manager.mode="enemy move1";
		}else{  //bug handler; go to next battler
			manager.mode="next";
		}
	}
	
	private void update_turn_main(){
		//if space pressed, end initiative if allowed
		if((Process_Main.$input.trigger("Space")||hud.trigger("Initiative"))&&manager.can_end()){
			//set current character's entity as main player
			world.map.set_main_player((Entity_Player)manager.current.entity);
			end_initiative();
			return;
		}
		//finish turn if enter pressed, or if already moved and acted
		if(Process_Main.$input.trigger("Enter")||hud.trigger("Skip")||(manager.moved&&manager.acted)){
			manager.mode="turn face";
			//show selector
			world.selector.show();
		}
		//if M pressed, move, unless move already taken
		if((Process_Main.$input.trigger("M")||hud.trigger("Move"))&&!manager.moved){
			manager.mode="turn move";
			//show available move locations
			manager.show_available_moves(manager.current);
			//show selector
			world.selector.show();
		}
		//if N pressed, attack, unless main action already taken
		if((Process_Main.$input.trigger("N")||hud.trigger("Attack"))&&!manager.acted){
			manager.mode="turn select";
			//set potential attack battle action
			manager.proposed=manager.current.attack();
			//show available targets
			manager.show_available_targets(manager.current);
			//show selector
			world.selector.show();
		}
	}
	
	private void update_set(){ //deprecated; will never be used
		//if clicked, move to the location of the selector
		if(Process_Main.$input.trigger("Mouse Left")){
			
			//immediately move entity
			manager.current.entity.x=world.selector.x;
			manager.current.entity.y=world.selector.y;
			manager.current.entity.z=world.selector.z;
			
			//move to next battler
			manager.mode="next";
			//hide available move locations
			manager.hide_available_moves();
			//hide selector
			world.selector.hide();
		}
	}
	
	private void update_turn_move(){
		//if M pressed, return from move select
		if((Process_Main.$input.trigger("M")||hud.trigger("Move"))){
			manager.mode="turn main";
			//hide available move locations
			manager.hide_available_moves();
			//hide selector
			world.selector.hide();
		}
		
		//if clicked, move to the location of the selector
		if(Process_Main.$input.trigger("Mouse Left")){
			
			//set move order
			manager.move_entity_to(manager.current.entity,world.selector.x,world.selector.z);
			
			//return to main turn mode
			manager.mode="turn main";
			//hide available move locations
			manager.hide_available_moves();
			//hide selector
			world.selector.hide();
		}
	}
	
	private void update_turn_select(){
		//if N pressed, return from target select (cancel attack)
		if((Process_Main.$input.trigger("N")||hud.trigger("Attack"))){
			manager.mode="turn main";
			//clear proposed battle action
			manager.proposed=null;
			//hide available target locations
			manager.hide_available_targets();
			//hide selector
			world.selector.hide();
		}
		
		//if clicked and target at selector, try to attack the target
		if(Process_Main.$input.trigger("Mouse Left")){
			//get target
			Battler target=manager.get_battler(world.selector.x,world.selector.z);
			if(target!=null){
				//set execute action order
				manager.execute_action(manager.proposed,target.entity,manager.current);

				//return to main turn mode
				manager.mode="turn main";
				//clear proposed battle action
				manager.proposed=null;
				//hide available targets
				manager.hide_available_targets();
				//hide selector
				world.selector.hide();
				
			}
		}
	}
	
	private void update_turn_face(){
		//have the current player forget what it is concentrating on and face towards the selector
		manager.current.entity.make_lose_concentration();
		manager.current.entity.face_towards(world.selector.x,world.selector.z);
		
		//return if canceled and action available
		if(hud.trigger("Skip")&&!(manager.acted&&manager.moved)){
			manager.mode="turn main";
			world.selector.hide();
			return;
		}
		
		//if clicked, end turn
		if(Process_Main.$input.trigger("Mouse Left")){
			//if over a battler that the current entity is aware of, set concentration
			Battler target=manager.get_battler(world.selector.x,world.selector.z);
			if(target!=null){
				if(manager.current.entity.aware_of().contains(target.entity)){
					manager.current.entity.make_concentrate_on(target.entity);
				}
			}
			
			manager.mode="next";
			//hide selector
			world.selector.hide();
		}
	}
	
	//enemy turn update methods
	
	private void update_enemy_move1(){
		//get proposed move path
		Move_Path proposed=((Entity_Enemy)manager.current.entity).decide_move1();
		//if proposed path is valid, enact it
		if(proposed!=null){
			manager.move_entity_along(manager.current.entity,proposed);
			manager.moved=true;
		}
		
		manager.mode="enemy action";
	}
	
	private void update_enemy_action(){
		//get proposed AI action
		AI_Action proposed=((Entity_Enemy)manager.current.entity).decide_action();
		//if proposed action is valid, enact it
		if(proposed!=null){
			manager.execute_action(proposed.action,proposed.target,manager.current);
			manager.acted=true;
		}
		
		manager.mode="enemy move2";
	}
	
	private void update_enemy_move2(){
		//if not already moved
		if(!manager.moved){
			Move_Path proposed=((Entity_Enemy)manager.current.entity).decide_move2();
			//if proposed path is valid, enact it
			if(proposed!=null){
				manager.move_entity_along(manager.current.entity,proposed);
				manager.moved=true;
			}
		}
		manager.mode="enemy face";
	}
	
	private void update_enemy_face(){
		//get proposed face
		String dir=((Entity_Enemy)manager.current.entity).decide_face();
		
		if(dir.contains("l")){
			manager.current.entity.face_left();
		}
		if(dir.contains("r")){
			manager.current.entity.face_right();
		}
		if(dir.contains("u")){
			manager.current.entity.face_up();
		}
		if(dir.contains("d")){
			manager.current.entity.face_down();
		}
		
		manager.mode="next";
	}
	
	//end initiative script:
	private void end_initiative(){
		//dispose battler manager
		manager.dispose();
		//dispose of HUD
		hud.dispose_panel();
		//hide grid
		world.grid.hide();
		//run battle end scripts for players
		for(Entity_Player p:world.map.players()){
			p.end_initiative();
		}
		//make new scene object
		Process_Main.$scene=new Scene_Explore(this);
	}

}
