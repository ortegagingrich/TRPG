package game.scripting.message;

import java.io.Serializable;
import java.util.ArrayList;

//class of messages to be displayed in message window; consists of an arraylist of message fragments along with settings
public class Message implements Serializable {
	public static final long serialVersionUID=1L;
	
	private ArrayList<Fragment> contents;
	private String raw_text;
	private String preview;  //short preview of message contents;
	
	public Message(){
		contents=new ArrayList<Fragment>();
		preview="";
	}
	
	//convert string to message
	public void parse_string(String s){
		//first clear contents
		contents.clear();
		//set raw text
		raw_text=s;
		//set preview:
		if(raw_text.length()<50){
			preview=raw_text;
		}else{
			preview=raw_text.substring(0,50);
		}
		//parse string
		//start by separating the string at spaces
		String[] parts=s.split(" ");
		//cycle through separated parts and parse for variable references, etc.
		for(String part:parts){
			//check for variable references, etc.
			if(part.contains("@")){
				//check if money
				if(part.equals("@money")){
					contents.add(new Fragment_Money());
				}else{
					//parse for variable reference, etc.
					part=part.replace("@",".");
					contents.add(new Fragment_Variable(part));
				}
				
			}else{
				//just make a word fragment, if no references found
				contents.add(new Fragment_Word(part));
			}
			//add a space fragment
			contents.add(new Fragment_Space());
		}
	}
	
	//return preview
	public String preview(){
		return preview;
	}
	
	//return raw text
	public String text(){
		return raw_text;
	}
	
	//return contents
	public ArrayList<Fragment> contents(){
		return contents;
	}
	
	//return contents split into rows
	public ArrayList<ArrayList<Fragment>> split(int rowmax){
		ArrayList<ArrayList<Fragment>> rows=new ArrayList<ArrayList<Fragment>>();
		ArrayList<Fragment> current_row=new ArrayList<Fragment>();
		int current_count=0;
		rows.add(current_row);
		for(Fragment f:contents){
			//if necessary, advance to the next row
			if(current_count+f.size()>rowmax&&f.wrap_allowed()){
				current_row=new ArrayList<Fragment>();
				current_count=0;
				rows.add(current_row);
			}
			current_count+=f.size();
			//add fragment to current row
			current_row.add(f);
		}
		return rows;
	}
}
