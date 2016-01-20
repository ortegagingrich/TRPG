package game.scripting.message;

import java.io.Serializable;

//class of word-based message fragments
public class Fragment_Word extends Fragment implements Serializable {
	public static final long serialVersionUID=1L;
	
	private String word;
	
	public Fragment_Word(String w){
		word=w;
	}
	
	//returns the string to print
	@Override
	protected String to_print(){
		return word;
	}
	
	
}
