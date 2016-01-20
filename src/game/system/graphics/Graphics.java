package game.system.graphics;

import com.jme3.app.SimpleApplication;
import com.jme3.system.*;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Quaternion;
//import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
//import com.jme3.renderer.Camera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
//import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
//import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.geomipmap.TerrainQuad;
//import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
//import com.jme3.terrain.heightmap.HillHeightMap; // for exercise 2
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.math.Ray;
//import java.util.ArrayList;
//import java.util.List;
import com.jme3.export.binary.*;

import game.system.Process_Main;
import game.Game;

import java.util.logging.*;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.shadow.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;


public class Graphics extends SimpleApplication {
	
	//debug method: will show any spatial
	public static void debug_show(Spatial s){
		graphics_manager.attachChild(Process_Main.rootNode,s);
	}
	
	
	public static Graphics_Manager graphics_manager;
	public static Image_Manager_Temporary image_manager;
	
	
	public float camx;
	public float camy;
	public float camz;
	
	
	private String[] mappings;
    
	public Graphics(){
    	super();
    	make_settings();
    	Process_Main.rootNode=rootNode;
    	Process_Main.exporter=BinaryExporter.getInstance();
    	Process_Main.importer=BinaryImporter.getInstance();
    }
	
	//settings
	public static final int screen_resolution_x=1600;
	public static final int screen_resolution_y=900;
	
	private void make_settings(){
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);// the most important line here
    	AppSettings gameSettings = null;
        gameSettings = new AppSettings(false);
        gameSettings.setResolution(screen_resolution_x,screen_resolution_y);
        gameSettings.setFullscreen(false);
        gameSettings.setVSync(false);
        gameSettings.setTitle("Star of Elysium");
        gameSettings.setUseInput(true);
        gameSettings.setFrameRate(500);
        gameSettings.setSamples(0);
        gameSettings.setRenderer("LWJGL-OpenGL2");
        setDisplayStatView(false);
        //setDisplayFps(false);
        settings = gameSettings;
        setShowSettings(false);
        this.getListener();
    }

    
    
    @Override
    public void simpleInitApp(){
    	//asset locator
    	assetManager.registerLocator(Game.ASSET_DIRECTORY, FileLocator.class);
    	
    	//set up managers
    	graphics_manager=new Graphics_Manager();
    	image_manager=new Image_Manager_Temporary();
        init_keys();
        init_camera();
    }
    
    //@Override
    public void stop(){
    	System.out.println("test");
    	super.stop();
    }
    
    private void init_keys(){
    	//inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
    	inputManager.addMapping("Mouse Right",new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    	inputManager.addMapping("Mouse Left",new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    	inputManager.addMapping("Space",new KeyTrigger(KeyInput.KEY_SPACE));
    	inputManager.addMapping("Enter",new KeyTrigger(KeyInput.KEY_RETURN));
    	inputManager.addMapping("Shift",new KeyTrigger(KeyInput.KEY_LSHIFT));
    	inputManager.addMapping("Control",new KeyTrigger(KeyInput.KEY_LCONTROL));
    	inputManager.addMapping("Delete",new KeyTrigger(KeyInput.KEY_DELETE));
    	inputManager.addMapping("Add",new KeyTrigger(KeyInput.KEY_ADD));
    	inputManager.addMapping("Subtract",new KeyTrigger(KeyInput.KEY_SUBTRACT));
    	inputManager.addMapping("Up",new KeyTrigger(KeyInput.KEY_UP));
    	inputManager.addMapping("Down",new KeyTrigger(KeyInput.KEY_DOWN));
    	inputManager.addMapping("Left",new KeyTrigger(KeyInput.KEY_LEFT));
    	inputManager.addMapping("Right",new KeyTrigger(KeyInput.KEY_RIGHT));
    	inputManager.addMapping("Comma",new KeyTrigger(KeyInput.KEY_COMMA));
    	inputManager.addMapping("Period",new KeyTrigger(KeyInput.KEY_PERIOD));
    	inputManager.addMapping("W",new KeyTrigger(KeyInput.KEY_W));
    	inputManager.addMapping("A",new KeyTrigger(KeyInput.KEY_A));
    	inputManager.addMapping("S",new KeyTrigger(KeyInput.KEY_S));
    	inputManager.addMapping("D",new KeyTrigger(KeyInput.KEY_D));
    	inputManager.addMapping("M",new KeyTrigger(KeyInput.KEY_M));
    	inputManager.addMapping("N",new KeyTrigger(KeyInput.KEY_N));
    	inputManager.addMapping("P",new KeyTrigger(KeyInput.KEY_P));
    	inputManager.addMapping("L",new KeyTrigger(KeyInput.KEY_L));
    	inputManager.addMapping("F",new KeyTrigger(KeyInput.KEY_F));
    	inputManager.addMapping("E",new KeyTrigger(KeyInput.KEY_E));
    	inputManager.addMapping("G",new KeyTrigger(KeyInput.KEY_G));
    	inputManager.addMapping("Q",new KeyTrigger(KeyInput.KEY_Q));
    	inputManager.addMapping("J",new KeyTrigger(KeyInput.KEY_J));
    	inputManager.addMapping("R",new KeyTrigger(KeyInput.KEY_R));
    	inputManager.addMapping("C",new KeyTrigger(KeyInput.KEY_C));
    	inputManager.addMapping("V",new KeyTrigger(KeyInput.KEY_V));
    	mappings=new String[]{"Mouse Left","Mouse Right","Space","Enter","Shift","Control","Delete","Add","Subtract","Up","Down","Left","Right","Comma","Period","W","A","S","D","M","N","P","L","F","E","G","Q","J","R","C","V"};
    	inputManager.addListener(actionListener, mappings);
    	
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
        	//bug prevention: if input is has not yet been set up, return
        	if(Process_Main.$input==null){return;}
        	
        	for(String map:mappings){
        		if((name==map)&&keyPressed){
        			Process_Main.$input.add_press(map);
        			return;
        		}
        		if((name==map)&&!keyPressed){
        			Process_Main.$input.add_release(map);
        			return;
        		}
        	}
        }
     };
     
     private void update_mouse(){
    	 Vector2f click2d=inputManager.getCursorPosition();
    	 Process_Main.$input.set_mouse(Math.round(click2d.x),screen_resolution_y-Math.round(click2d.y));
     }
    
    private void init_camera(){
    	flyCam.setEnabled(false);
    	camx=50f;
    	camy=10f;
    	camz=50f;
    	update_camera();
    	update_mouse();
    }


    @Override
    public void simpleUpdate(float tpf) {
    	Logger.getLogger("de.lessvoid.nifty").setLevel(Level.OFF); 
    	//update camera
    	update_camera();
    	//update mouse
    	update_mouse();
    	//update scene manager
    	graphics_manager.update();
    	//advance one frame
        nextframe();
    }
    
    private void update_camera(){
    	
    	//flyCam.setMoveSpeed(20+1.2f*Math.abs(cam.getLocation().y));
    	
    	//move camera to position
    	//cam.setFrustumPerspective(30,16f/9f,cam.getFrustumNear(),cam.getFrustumFar());
    	cam.setLocation(new Vector3f(camx,camy,camz));
    	cam.lookAtDirection(new Vector3f(0,-10,15),new Vector3f(0,1,0));
    	
    }
    
    private void nextframe(){
    	
        //synchronize with other threads;
		try{
			
			//wait for main thread
			Process_Main.graphics_ready=true;
			while(!(Process_Main.graphics_ready&&Process_Main.main_ready)){
				//Thread.sleep(1);
			}
			Thread.sleep(2);
			//have graphics act first; no sleeping;
			
			Process_Main.graphics_ready=false;
		}catch(Exception ex){ex.printStackTrace();}
    }
    
    public Node get_rootNode(){
        return rootNode;
    }
    
    public Ray get_camera_ray(){
    	return new Ray(cam.getLocation(),cam.getDirection());
    }
    
    public Vector3f get_camera_position(){
    	return cam.getLocation();
    }
    
    public Ray get_mouse_ray(){
    	Vector2f click2d=inputManager.getCursorPosition();
    	Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        return new Ray(click3d,dir);
    }
    
    
    
    /////graphics related methods:
    
    // make nifty gui:
    private NiftyJmeDisplay niftyDisplay;
    
    public Nifty make_nifty(){
    	//only make new nifty if one does not already exist
    	if(niftyDisplay==null){
    		niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        	guiViewPort.addProcessor(niftyDisplay);
    	}
    	
    	Nifty nifty=niftyDisplay.getNifty();
    	//nifty.setIgnoreMouseEvents(true);
    	
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.OFF);
		Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.OFF);
		Logger.getLogger("NiftyEventBusLog").setLevel(Level.OFF);
		Logger.getLogger("NiftyImageManager").setLevel(Level.OFF);
		nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
		
    	return nifty;
    }
    
    
    // make geometries:
    
    public Geometry draw_line(Node n,Vector3f start,Vector3f end,ColorRGBA color){
    	Geometry g=make_line(start,end);
    	Material m=new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    	m.setColor("Color",color);
    	g.setMaterial(m);
    	graphics_manager.attachChild(n,g);
    	return g;
    }
    
    public Geometry make_line(Vector3f start,Vector3f end){
    	Line l=new Line(start,end);
    	Geometry g=new Geometry("line",l);
    	return g;
    }
    
    public Geometry draw_quad(Node n,float xi,float yi,float zi,float w,float h){
    	Geometry g=make_quad(xi,yi,zi,w,h);
    	graphics_manager.attachChild(n,g);
    	return g;
    }
    
    public Geometry make_quad(float xi,float yi,float zi,float w,float h){
    	Quad q=new Quad(w,h);
    	Geometry g=new Geometry("q test",q);
    	g.move(xi,yi,zi+h);
    	g.rotate((float)Math.toRadians(270f),0,0);
    	Material m=new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        m.setTransparent(true);
        g.setMaterial(m);
        return g;
    }
    
    public Geometry draw_box(Node n,float x,float y,float z,float xw,float yw,float zw){
        Geometry g=make_box(x,y,z,xw,yw,zw);
        graphics_manager.attachChild(n,g);
        return g;
    }
    
    public Geometry make_box(float x,float y,float z,float xw,float yw,float zw){  //makes a box without drawing it to a node
    	Box b=new Box(new Vector3f(x,y,z),xw,yw,zw);
        Geometry g=new Geometry("g test",b);
        Material m=new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        m.setTransparent(true);
        g.setMaterial(m);
        return g;
    }
    
    public Spatial draw_box_wireframe(Node n,float x,float y,float z,float xw,float yw,float zw,ColorRGBA color){
    	Node g=new Node();
    	
    	float[] xi={x,x+xw};
		float[] yi={y,y+yw};
		float[] zi={z,z+zw};
		for(float xit:xi){
			for(float zit:zi){
				draw_line(g,new Vector3f(xit,y,zit),new Vector3f(xit,y+yw,zit),color);
			}
		}
		for(float xit:xi){
			for(float yit:yi){
				draw_line(g,new Vector3f(xit,yit,z),new Vector3f(xit,yit,z+zw),color);
			}
		}
		for(float zit:zi){
			for(float yit:yi){
				draw_line(g,new Vector3f(x,yit,zit),new Vector3f(x+xw,yit,zit),color);
			}
		}
    	graphics_manager.attachChild(n,g);
    	return g;
    }
    
    public Geometry draw_cylinder(Node n,float x,float y,float z,float rad,float h){
    	Geometry g=make_cylinder(x,y,z,rad,h);
        //n.attachChild(g);
        graphics_manager.attachChild(n,g);
        return g;
    }
    
    public Geometry make_cylinder(float x,float y,float z,float rad,float h){
    	Cylinder c=new Cylinder(10,10,rad,h,true);
    	Geometry g=new Geometry("cylinder",c);
    	g.move(x,y,z);
    	g.rotateUpTo(new Vector3f(0,0,1));
    	Material m=new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        m.setTransparent(true);
        g.setMaterial(m);
        return g;
    }
    
    public Geometry draw_sphere(Node n,float x,float y,float z,float rad){
    	Geometry g=make_sphere(x,y,z,rad);
    	graphics_manager.attachChild(n,g);
    	return g;
    }
    
    public Geometry make_sphere(float x,float y,float z,float rad){
    	Sphere s=new Sphere(10,10,rad);
    	Geometry g=new Geometry("sphere",s);
    	g.move(x,y,z);
    	Material m=new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
    	m.setTransparent(true);
    	g.setMaterial(m);
    	return g;
    }
    
    public TerrainQuad draw_terrain(Node n,String heightmap_name,String texr,String texg,String texb){
    	TerrainQuad t=make_terrain(heightmap_name,texr,texg,texb);
    	graphics_manager.attachChild(n,t);
    	return t;
    }
    
    public TerrainQuad make_terrain(String heightmap_name,String texr,String texg,String texb){
    	//first try and load the height map;
    	AbstractHeightMap heightmap=null;
    	Texture heightMapImage;
    	try{
    		heightMapImage = assetManager.loadTexture("HeightMaps/"+heightmap_name+".png");
    	}catch(Exception ex){
    		heightMapImage=assetManager.loadTexture("Textures/flat512.png");
    	}
    	heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
    	heightmap.load();
    	//make quad
    	TerrainQuad t=new TerrainQuad("",65,513,heightmap.getHeightMap());
    	t.setLocalTranslation(50,-0.2f,50);
    	t.setLocalRotation(new Quaternion().fromAngles(0,(float)Math.toRadians(180f),0));
    	t.setLocalScale(0.2f,0.06f,0.2f);
    	
    	Material mat=new Material(assetManager,"Common/MatDefs/Terrain/Terrain.j3md");
    	//Material mat=new Material(assetManager,"Common/MatDefs/Terrain/TerrainLighting.j3md");
    	//mat.setTexture("AlphaMap",assetManager.loadTexture("HeightMaps/#test.png"));
    	try{
    		mat.setTexture("Alpha",assetManager.loadTexture("Heightmaps/#"+heightmap_name+".png"));
    		//mat.setTexture("AlphaMap",assetManager.loadTexture("HeightMaps/#"+heightmap_name+".png"));
    	}catch(Exception ex){
    		mat.setTexture("Alpha",assetManager.loadTexture("HeightMaps/#test.png"));
    		//mat.setTexture("AlphaMap",assetManager.loadTexture("HeightMaps/#test.png"));
    	}
    	Texture grass=assetManager.loadTexture("Textures/"+texr+".png");
    	grass.setWrap(WrapMode.Repeat);
    	/*mat.setTexture("DiffuseMap",grass);
    	mat.setFloat("DiffuseMap_0_scale",64f);*/
    	mat.setTexture("Tex1",grass);
    	mat.setFloat("Tex1Scale",64f);
    	Texture dirt=assetManager.loadTexture("Textures/"+texg+".png");
    	dirt.setWrap(WrapMode.Repeat);
    	/*mat.setTexture("DiffuseMap_1",dirt);
    	mat.setFloat("DiffuseMap_1_scale",64f);*/
    	mat.setTexture("Tex2",dirt);
    	mat.setFloat("Tex2Scale",64f);
    	Texture path=assetManager.loadTexture("Textures/"+texb+".png");
    	path.setWrap(WrapMode.Repeat);
    	/*mat.setTexture("DiffuseMap_2",path);
    	mat.setFloat("DiffuseMap_2_scale",64f);*/
    	mat.setTexture("Tex3",path);
    	mat.setFloat("Tex3Scale",64f);
    	
    	
    	/*Material m=new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    	Texture tex=assetManager.loadTexture("Textures/"+name+".png");
    	tex.setWrap(WrapMode.Repeat);
    	m.setTexture("DiffuseMap",tex);*/
    	t.setMaterial(mat);
    	return t;
    }
    
    //texture-related methods:
    
    
    public void set_texture(Geometry g,String name,float u,float v){  //for shaded textures
    	Material m=new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    	//Material m=new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
    	Texture t=assetManager.loadTexture("Textures/"+name+".png");
    	t.setWrap(WrapMode.Repeat);
    	m.setTexture("DiffuseMap",t);
    	//m.setTexture("ColorMap",t);
    	//temporary test
    	/*
    	Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1*v,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1*v,1);
    	g.getMesh().setBuffer(com.jme3.scene.VertexBuffer.Type.TexCoord, 2, com.jme3.util.BufferUtils.createFloatBuffer(texCoord));
    	*/
    	//end temporary test
    	g.getMesh().scaleTextureCoordinates(new Vector2f(v,u));
    	graphics_manager.setMaterial(g,m);
    }
    
    public Texture get_texture(String filename){
    	//if extension missing, assume png
    	Texture t;
    	if(!filename.contains(".")){
    		t=assetManager.loadTexture(filename+".png");
    	}else{
    		t=assetManager.loadTexture(filename);
    	}
    	return t;
    }
    
    public void set_sprite(Geometry g,Texture t){
    	Material m=new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
    	
    	m.setTexture("DiffuseMap",t);
    	m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    	///
    	graphics_manager.setMaterial(g,m);
    	graphics_manager.allowTransparency(g);
    }
    
    public void set_sprite(Geometry g,String name,String dir){  //note: unshaded
    	//experiment: try shaded sprites - experiment successful
    	//Material m=new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
    	
    	Texture t;
    	t=image_manager.get_texture(dir+name);
    	set_sprite(g,t);
    	
    }
   
    
    
}

