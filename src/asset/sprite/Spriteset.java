package asset.sprite;

import java.io.*;
import java.util.ArrayList;

//basically just a wrapper for an arraylist of sprites; used for saving
public class Spriteset implements Serializable {
	public static final long serialVersionUID=1L;
	
	public ArrayList<Sprite> sprites=new ArrayList<Sprite>();
	
	public Sprite get_sprite(String name){
		for(Sprite s:sprites){
			if(s.name().equals(name)){
				return s;
			}
		}
		return null;
	}
	
	//batch unload sprite textures to save memory (used for map changes, etc.)
	public void unload_all_sprites(){
		for(Sprite s:sprites){
			s.unload_all();
		}
	}
	
	//loads and parses script for changing sprites
	public void modify_sprites(){
		try{
			FileReader ryt=new FileReader(game.Game.directory+"/assets/Sprites/editsprites.txt");
			BufferedReader in=new BufferedReader(ryt);
			
			while(true){
				String line=in.readLine();
				if(line==null){
					break;
				}
				parse_line(line);
			}
			in.close();
			//replace with empty file
			FileWriter wyt=new FileWriter(game.Game.directory+"/assets/Sprites/editsprites.txt");
			BufferedWriter out=new BufferedWriter(wyt);
			out.close();
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	private void parse_line(String line){
		try{
			line=line.substring(0,line.length()-1);
			String[] s=line.split("\\(");
			if(s[0].equals("sprite")){
				String[] args=s[1].split(",");
				int il=Integer.parseInt(args[2]);
				int wl=Integer.parseInt(args[3]);
				//add new sprite
				sprites.add(new Sprite(args[0],args[1],il,wl));
			}else if(s[0].equals("animation")){
				String[] args=s[1].split(",");
				int l=Integer.parseInt(args[2]);
				get_sprite(args[0]).add_animation(args[1],l);
			}else{
				System.out.println("failed to read sprite command: "+line);
			}
		}catch(Exception ex){
			System.out.println("ERROR: failed to read sprite command: "+line+":");
			ex.printStackTrace();
		}
	}
}
