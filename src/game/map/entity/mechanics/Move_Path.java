package game.map.entity.mechanics;

import com.jme3.math.Vector2f;
import java.util.ArrayList;

//move path for entities
public class Move_Path {
	
	public Vector2f destination;
	public ArrayList<Vector2f> moves;
	
	public Move_Path(Vector2f d){
		destination=d;
		moves=new ArrayList<Vector2f>();
	}
	
	public Move_Path(Move_Path p,Vector2f mo){
		destination=p.destination.add(mo);
		moves=(ArrayList<Vector2f>)p.moves.clone();
		moves.add(mo);
	}
	
	//returns the direction of the given step
	public String get_dir(int step){
		
		Vector2f move=moves.get(step);
		String dir="";
		if(move.y==0){
			if(move.x==1){
				dir="l";
			}else{
				dir="r";
			}
		}else{
			if(move.y==1){
				dir="u";
			}else{
				dir="d";
			}
		}
		return dir;
	}

}
