package game.map;

import io.game_dump.Dump_Map;
import io.game_dump.Dump_Universe;

import java.util.ArrayList;



//handles collection of maps, etc.; created upon quest creation
public class Universe {
	
	private ArrayList<Map> maps;
	
	public int startm; //id of starting map
	public int startx; 
	public int starty;
	public int startz;
	
	public Universe(){
		maps=new ArrayList<Map>();
		
		//make initial map
		maps.add(new Map(0));
		set_start(0,0,0,0);
	}
	
	public void add_map(){
		maps.add(new Map(next_id()));
	}
	
	public void delete_map(int id){
		maps.remove(get_map(id));
	}
	
	public Map get_map(int id){
		for(Map m:maps){
			if(m.id()==id){
				return m;
			}
		}
		return null;
	}
	
	public ArrayList<Map> get_maps(){
		return maps;
	}
	
	private int next_id(){//keeps track of used ids and return the next available
		for(int idtest=0;idtest<=maps.size();idtest++){
			if(get_map(idtest)==null){
				return idtest;
			}
		}
		return maps.size();
	}
	
	
	public void set_start(int id,int x,int y,int z){
		remove_start();
		startm=id;
		startx=x;
		starty=y;
		startz=z;
		//add start entity to the correct map
		get_map(startm).add_starting(startx,starty,startz);
	}
	
	public void remove_start(){
		get_map(startm).remove_starting();
	}
	
	
	public void write(Dump_Universe d){
		for(Map m:maps){
			Dump_Map dm=new Dump_Map();
			m.write(dm);
			d.maps.add(dm);
		}
		
		//new:
		d.startm=startm;
		d.startx=startx;
		d.starty=starty;
		d.startz=startz;
	}
	
	public void read(Dump_Universe d){
		maps.clear();
		for(Dump_Map dm:d.maps){
			Map m=new Map(0);
			m.read(dm);
			maps.add(m);
		}
		set_start(d.startm,d.startx,d.starty,d.startz);
	}

}
