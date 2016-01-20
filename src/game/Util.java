package game;

//just a collection of useful static methods
public class Util {
	
	public static String make_string(int i,int digits){
		String s=Integer.toString(i);
		if(s.length()>digits){
			return s;
		}
		while(s.length()<digits){
			s="0"+s;
		}
		return s;
	}
	
}
