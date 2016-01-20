package game.scripting;

import java.io.Serializable;

//class of float-valued scripting variables
public class Variable implements Serializable {
	public final static long serialVersionUID=1L;
	
	private String[] handler_address; //indicates where the handler can be found
	
	private int id; //does not change; unique to variable handler
	private String name; //used for convenience in identifying; may change
	private float value;
	
	public Variable(int i,String n,String[] a){
		id=i;
		name=n;
		handler_address=a;
		value=0;
		System.out.println(address());
	}
	
	public Variable(int i,String n,String[] a,float init_value){
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
	
	public float value(){
		return value;
	}
	
	public void set(float new_value){
		value=new_value;
	}
	
}
