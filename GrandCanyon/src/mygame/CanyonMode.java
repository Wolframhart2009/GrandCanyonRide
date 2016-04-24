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
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.Random;
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
    private static final int NUM_RAPIDS = 16;

    boolean addedRapid = false;
    
    public BulletAppState bullet;
    public static final float RESTITUTION = 0.5f;

    Main app;
    AppStateManager asm;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (Main) app;
        this.asm = stateManager;
        
//        this.app.getFlyByCamera().setEnabled(true);
        this.app.getInputManager().setCursorVisible(false);
        
        initPhysics();
                
        w = new World(this, "Scenes/Grand_Canyon.jpg", 4097);
        
        aps = new DisplaySettings(this, w);
        w.addShadows(aps.getMainLight());
        
        s = new Sky(this, "Textures/Sky/Bright/BrightSky.dds", false, aps.getMainLight());
        water = new Water(this, w,  aps.getMainLight());
        w.attachWater(water);
        raft = new Raft(this, w);
        
        initChaseCam();
        
//        initDebug();
   }
    
    private void initPhysics() {
        bullet = new BulletAppState();
        asm.attach(bullet);
    }
    
    public void initCourse() {
        course = new Course(this, w, NUM_RAPIDS);
        Vector3f course1Locs[] = new Vector3f[NUM_RAPIDS];
        
        course1Locs[0] = new Vector3f(-164, w.getWaterHeight(), -75);
        course1Locs[1] = new Vector3f(-185, w.getWaterHeight(), -140);
        course1Locs[2] = new Vector3f(130, w.getWaterHeight(), -360);
        course1Locs[3] = new Vector3f(158, w.getWaterHeight(), 404);
        course1Locs[4] = new Vector3f(275, w.getWaterHeight(), 762);
        course1Locs[5] = new Vector3f(727, w.getWaterHeight(), -1332);
        course1Locs[6] = new Vector3f(561, w.getWaterHeight(), -957);
        course1Locs[7] = new Vector3f(542, w.getWaterHeight(), -827);
        course1Locs[8] = new Vector3f(541, w.getWaterHeight(), -717);
        course1Locs[9] = new Vector3f(425, w.getWaterHeight(), -586);
        course1Locs[10] = new Vector3f(355, w.getWaterHeight(), -531);
        course1Locs[11] = new Vector3f(250, w.getWaterHeight(), -680);
        course1Locs[12] = new Vector3f(36, w.getWaterHeight(), -568);
        course1Locs[13] = new Vector3f(60, w.getWaterHeight(), -416);
        course1Locs[14] = new Vector3f(91, w.getWaterHeight(), -213);
        course1Locs[15] = new Vector3f(-36, w.getWaterHeight(), -280);
        
        course.addPositions(course1Locs);
        
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
            Vector3f pos = course.getNextPos();
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
   
   private void initChaseCam() {
       this.app.getFlyByCamera().setEnabled(false);
//       this.app.getInputManager().setCursorVisible(false);
       ChaseCamera chaseCam = new ChaseCamera(this.getCamera(), raft.getNode(), this.getInputManager());
       this.app.getInputManager().setCursorVisible(false);
       chaseCam.setSmoothMotion(true);
//       chaseCam.setTrailingEnabled(true);
       chaseCam.setDefaultDistance(20f);
       chaseCam.setLookAtOffset(new Vector3f(0,8,0));
       chaseCam.setDefaultVerticalRotation(-FastMath.DEG_TO_RAD * 8);
//       chaseCam.setDragToRotate(false);
   }
   
   private void initDebug(){
//        this.app.getFlyByCamera().setEnabled(true);
//        this.app.getFlyByCamera().setMoveSpeed(50.0f);
        bullet.setDebugEnabled(true);
                
//        getCamera().setLocation(new Vector3f(5f, w.getWaterHeight() + 5f, 5f));
//        getCamera().lookAt(new Vector3f(0f, w.getWaterHeight() + 1f, 20f), Vector3f.UNIT_Y);
        
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

    @Override
    public void update(float tpf) {
        app.getListener().setLocation(raft.getPos());
        app.getListener().setRotation(raft.getRot());
        System.out.println(app.getListener().getLocation());
    }
   
   
}
