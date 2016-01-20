package asset.model;

import java.util.ArrayList;

//texture library for model components (really just stores strings for textures; mainly used to populate texture lists)
public class Texture_Library {
	
	//category lists
	private ArrayList<String> test;
	
	public Texture_Library(){
		//make categories
		test=new ArrayList<String>();
		//populate categories
		populate_categories();
	}
	
	//retrieve list of category names
	public ArrayList<String> get_categories(){
		ArrayList<String> c=new ArrayList<String>();
		//add category names
		c.add("test");
		//return list
		return c;
	}
	
	//retrieve texture list for a category
	public ArrayList<String> get_textures(String category){
		//if category is null, return an empty list
		if(category==null){
			return new ArrayList<String>();
		}
		//determine which category list to return
		if(category.equals("test")){
			return test;
		}else{// if all fails, return an empty list
			return new ArrayList<String>();
		}
	}
	
	//retrieve list of all textures
	public ArrayList<String> get_all_textures(){
		ArrayList<String> l=new ArrayList<String>();
		//add all textures from all categories
		for(String category:get_categories()){
			for(String tex:get_textures(category)){
				l.add(tex);
			}
		}
		return l;
	}
	
	//check to see if a given texture name is valid
	public boolean texture_valid(String texture){
		return get_all_textures().contains(texture);
	}
	
	//method to populate categories; every time a new texture is added, put it here
	private void populate_categories(){
		//add test textures
		test.add("grass");
		test.add("grass2");
		test.add("metal");
	}
	
}
