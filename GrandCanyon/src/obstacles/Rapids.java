package obstacles;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import java.util.Random;
import mygame.CanyonMode;
import mygame.CanyonRunMode;
import world.World;

public class Rapids extends Node{
    public static final Box RapidMesh = new Box(4f, 2f, 4f);
    
    private CanyonMode sa;
    private CanyonRunMode saM;
    private boolean runMode;
    private World world;
    
    private Geometry rapidGeom;
    private Material rapidMat;
    private RapidEmitter[] emit = new RapidEmitter[6];
            
    private AudioNode rapidSound;
    
    public Rapids(CanyonMode s, World w, Vector3f location){
        this.sa = s;
        this.world = w;
        
        initMat();
        initGeom();
        initEmitters();
        initPhysics();
        initAudio();
        
        if(location != null){
            this.setLocalTranslation(location);
            System.out.println("(Rapid added at location " + location );
        }
        
        if(sa instanceof CanyonRunMode){
            runMode = true;
            saM = (CanyonRunMode) sa;
        }
    }
    
    private void initGeom(){
        rapidGeom = new Geometry("Rapids", RapidMesh);
        rapidGeom.setMaterial(rapidMat);
        this.attachChild(rapidGeom);
        
        sa.getRootNode().attachChild(this);
        rapidGeom.setLocalTranslation(0, 0, 0);
        rapidGeom.setCullHint(Spatial.CullHint.Always); //This makes it invisible
    }
    
    private void initEmitters(){
        Random ran = new Random();
        
        emit[0] = new RapidEmitter(sa, this);
        emit[1] = new RapidEmitter(sa, this);
        emit[2] = new RapidEmitter(sa, this);
        emit[3] = new RapidEmitter(sa, this);
        emit[4] = new RapidEmitter(sa, this);
        emit[5] = new RapidEmitter(sa, this);
        
        for(int i = 0; i < 4; i ++){
            float ranX = (ran.nextFloat() * 10) - 5;
            float ranY = (ran.nextFloat() * 10) - 5;
            
            emit[i].setLocalTranslation(ranX,
                                     -.5f,
                                        ranY);
        }
       
    }
    
    private void initMat(){
        rapidMat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        rapidMat.setColor("Color", ColorRGBA.Blue);
    }
    
    private void initPhysics(){
        CollisionShape phyShell = CollisionShapeFactory.createDynamicMeshShape(rapidGeom);
        GhostControl ghost = new vortexControl(phyShell);
        rapidGeom.addControl(ghost);
        sa.bullet.getPhysicsSpace().add(ghost);  
//        ghost.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
//        ghost.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
    }
    
    private void initAudio(){
        rapidSound = new AudioNode(sa.getAssetManager(), "Sound/Rapids/Rapidsound.wav", false);
        rapidSound.setLooping(true);
        rapidSound.setReverbEnabled(false);
        rapidSound.setPositional(true);
        rapidSound.setVolume(1.20f);
        rapidSound.setRefDistance(10.0f);
        this.attachChild(rapidSound);
        rapidSound.setLocalTranslation(0, 0, 0);
        rapidSound.play();
    }
    
    public void stopAudio() {
        rapidSound.stop();
    }
    
    class vortexControl extends GhostControl implements PhysicsCollisionListener{

        public vortexControl(CollisionShape extent){
            super(extent);
            sa.bullet.getPhysicsSpace().addCollisionListener(this);
        }
        
        public void collision(PhysicsCollisionEvent event) {
           String objAName = event.getNodeA().getName();
           String objBName = event.getNodeB().getName();
   
           if(objAName.equals("Raft") && objBName.equals("Rapids")){
//               System.out.println("rapid collision");
               if(runMode && !saM.getRecovery()){
                   saM.setRecovery();
                   saM.decHitPoints();
               }
           }
           else if(objAName.equals("Rapids") && objBName.equals("Raft")){
//               System.out.println("rapid collision");
               if(runMode && !saM.getRecovery()){
                   saM.setRecovery();
                   saM.decHitPoints();
               }           
           }       
        }
        
        
    }
}