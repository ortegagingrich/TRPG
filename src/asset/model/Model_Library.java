package asset.model;

import java.io.Serializable;
import java.util.ArrayList;

//library class that contains a library of models
public class Model_Library implements Serializable {
	public static final long serialVersionUID=1L;
	
	private ArrayList<Model> models;//note; when models are deleted, there is a null placeholder, so a model's position is its id
	private String library_id="test";
	
	public Model_Library(String li){
		models=new ArrayList<Model>();
		library_id=li;
	}
	
	//model management
	public Model get_model(int id){
		return models.get(id);
	}
	
	//condensed list of models
	public ArrayList<Model> models_condensed(){
		ArrayList<Model> l=new ArrayList<Model>();
		for(Model m:models){
			if(m!=null){
				l.add(m);
			}
		}
		return l;
	}
	
	public void add_model(Model m){
		m.id=models.size();
		m.library_id=library_id;
		models.add(m);
	}
	
	public void save_model(Model m,int id){
		models.set(id,m);
	}
	
	public void remove_model(int id){
		models.set(id,null);
	}
	
}
