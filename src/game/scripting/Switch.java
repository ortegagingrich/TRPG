package game.scripting;

import java.io.Serializable;

//class of boolean-valued scripting variables
public class Switch implements Serializable {
	public final static long serialVersionUID=1L;
	
	private String[] handler_address;
	
	private int id;
	private String name;
	private boolean value;
	
	public Switch(int i,String n,String[] a){
		id=i;
		name=n;
		handler_address=a;
		value=false;
	}
	
	public Switch(int i,String n,String[] a,boolean init_value){
		id=i;
		name=n;
		handler_address=a;
		value=init_value;
	}
	
	public int id(){
		return id;
	}
	
	public String[] address(){
		 String[] a=new String[3];
		 a[0]=handler_address[0];
		 a[1]=handler_address[1];
		 a[2]=Integer.toString(id);
		 return a;
	}
	
	public String name(){
		return name;
	}
	
	public void rename(String n){
		name=n;
	}
	
	public boolean value(){
		return value;
	}
	
	public void set(boolean new_value){
		value=new_value;
	}
	
	public void toggle(){
		value=!value;
	}
	
}
