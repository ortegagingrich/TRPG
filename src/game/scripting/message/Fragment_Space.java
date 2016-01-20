package game.scripting.message;

import java.io.Serializable;

//fragment for containing a space between words
public class Fragment_Space extends Fragment_Word implements Serializable {
	public static final long serialVersionUID=1L;
	
	public Fragment_Space(){
		super(" ");
	}
	
	//wrap not allowed for space, to prevent lines from starting with space
	@Override
	public boolean wrap_allowed(){
		return false;
	}
}
