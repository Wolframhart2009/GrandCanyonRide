/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import obstacles.FallingRock;
import obstacles.FloatingLog;
import obstacles.Rapids;
import raft.Raft;
import world.Course;
import world.Sky;
import world.Water;
import world.World;

/**
 *
 * @author Graeme
 */
public class CanyonMode extends AbstractAppState{
        
    DisplaySettings aps;
    World w;
    Sky s;
    Water water;
    Raft raft;
    Course course;
    
    private Rapids rapids[];
    private FallingRock rocks[];
    private FloatingLog logs[];
     
    private static final int NUM_ROCKS = 1;
    private static final int NUM_LOGS = 1;
    private static final int NUM_RAPIDS = 1;

    boolean addedRapid = false;
    
    public BulletAppState bullet;
    public static final float RESTITUTION = 0.5f;

    Main app;
    AppStateManager asm;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (Main) app;
        this.asm = stateManager;
        
        this.app.getFlyByCamera().setEnabled(true);
        this.app.getInputManager().setCursorVisible(false);
        
        initPhysics();
                
        w = new World(this, "Scenes/Grand_Canyon.jpg", 4097);
        
        aps = new DisplaySettings(this, w);
        w.addShadows(aps.getMainLight());
        
        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false, aps.getMainLight());
        water = new Water(this, w,  aps.getMainLight());
        w.attachWater(water);
        raft = new Raft(this, w);
        
        initDebug();
   }
    
    private void initPhysics() {
        bullet = new BulletAppState();
        asm.attach(bullet);
    }
    
    public void initCourse() {
        course = new Course(this, w);
    }
    
    public void initFallingRocks() {
        rocks = new FallingRock[NUM_ROCKS];
        
        for(int i = 0; i < NUM_ROCKS; i++) {
            rocks[i] = new FallingRock(this, w);
        }
    }
    
    public void initRapids() {
        rapids = new Rapids[NUM_RAPIDS];
        
        for(int i = 0; i < NUM_RAPIDS; i++) {
            float yPos = w.getWaterHeight();
            float xPos = 0;
            float zPos = 20;
            
            Vector3f pos = new Vector3f(xPos, yPos, zPos); 
            
            rapids[i] = new Rapids(this, w, pos);
        }
    }
    
    public void initFloatingLogs() {
        logs = new FloatingLog[NUM_LOGS];
        
        for(int i = 0; i < NUM_LOGS; i++) {
            logs[i] = new FloatingLog(this, w);
        }
    }
    
   public Node getRootNode(){
       return app.getRootNode();
   }
   
   
   public ViewPort getViewPort(){
       return app.getViewPort();
   }
   
   public AssetManager getAssetManager(){
       return app.getAssetManager();
   }
   
   public InputManager getInputManager(){
       return app.getInputManager();
   }
   
   public Camera getCamera(){
       return app.getCamera();
   }
   
   private void initDebug(){
        this.app.getFlyByCamera().setMoveSpeed(50.0f);
        bullet.setDebugEnabled(true);
                
        getCamera().setLocation(new Vector3f(5f, w.getWaterHeight() + 5f, 5f));
        getCamera().lookAt(new Vector3f(0f, w.getWaterHeight() + 1f, 20f), Vector3f.UNIT_Y);
        
//        getCamera().setLocation(new Vector3f(0,10,15));
//        getCamera().lookAt(new Vector3f(-5,0.5f,10), Vector3f.UNIT_Y);
        
        // looks at start line
//        cam.setLocation(new Vector3f(-190f, w.getWaterHeight() + 20f, -100f));
//        cam.lookAt(new Vector3f(-190f, w.getWaterHeight(), -135f), Vector3f.UNIT_Y);
        
        // looks at finish line
//        this.getCamera().setLocation(new Vector3f(2090f, w.getWaterHeight() + 20f, 845f));
//        this.getCamera().lookAt(new Vector3f(2090f, w.getWaterHeight(), 820f), Vector3f.UNIT_Y);
        
   }
    
   @Override
   public void cleanup(){
        super.cleanup();
        app.getStateManager().detach(bullet);
        app.getStateManager().detach(this);
        app.getGuiNode().detachAllChildren();
        app.getRootNode().detachAllChildren();
    }
}
