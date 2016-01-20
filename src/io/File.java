package io;

import game.Game;
import game.system.*;
import java.io.*;
import com.jme3.export.*;
import com.jme3.asset.plugins.*;

public class File {
	
	//standard serialization
	public static void export(Object o,String name){ //name must include the extension
		try{
			FileOutputStream f=new FileOutputStream(Game.directory+name);
			ObjectOutputStream os=new ObjectOutputStream(f);
			os.writeObject(o);
			os.close();
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	public static Object open(String name){ //note: name must include the extension
		try{
			FileInputStream f=new FileInputStream(Game.directory+name);
			ObjectInputStream is=new ObjectInputStream(f);
			Object o=is.readObject();
			is.close();
			return o;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static Object copy_serializable(Object orig){ //produces a deep copy of a serializable object
		 Object obj = null;
	        try {
	            // Write the object out to a byte array
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(bos);
	            out.writeObject(orig);
	            out.flush();
	            out.close();

	            // Make an input stream from the byte array and read
	            // a copy of the object back in.
	            ObjectInputStream in = new ObjectInputStream(
	                new ByteArrayInputStream(bos.toByteArray()));
	            obj = in.readObject();
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }
	        return obj;
	}
	
}

