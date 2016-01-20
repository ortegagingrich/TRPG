package game.scripting.message;

import java.io.Serializable;
import game.*;
import game.scripting.*;
import game.system.Process_Main;

//fragment for printing the contents of a variable
public class Fragment_Variable extends Fragment implements Serializable {
	public static final long serialVersionUID=1L;
	
	//address of the variable referred to
	private String[] variable_address;
	
	public Fragment_Variable(String a){
		
		
		variable_address=new String[3];
	}
	
	//return string to print
	@Override
	protected String to_print(){
		//first retrieve the appropriate variable
		try{
			Variable v=Process_Main.quest.journal.find_variable(variable_address);
			//return string version of the variable's contents
			return Float.toString(v.value());
		}catch(Exception ex){
			return "[ERROR: variable not found]";
		}
	}
}
